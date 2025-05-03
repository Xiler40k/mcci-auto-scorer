package com.xiler.mcciautoscorer.util;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.regex.Pattern;

public class ExtrScoreHandler {

    private int kills;
    private int rounds;
    private int survived;
    private int penaltyCount;
    private int bonusCount;
    private int deaths;
    public ArrayList<String> cleanPatternsKills = new ArrayList<>();
    public ArrayList<String> cleanPatternsDeaths = new ArrayList<>();
    ScoreboardGetter scoreboardEntries = new ScoreboardGetter();

    public void initialisePatterns(GameHandler game) {
        cleanPatternsKills.add("was shot by .*?" + Pattern.quote(game.getUsername()));
        cleanPatternsKills.add("lava to escape .*?" + Pattern.quote(game.getUsername()));
        cleanPatternsDeaths.add(Pattern.quote(game.getUsername()) + " was slain by .*?");
        cleanPatternsDeaths.add(Pattern.quote(game.getUsername()) + " was shot by .*?");
        cleanPatternsDeaths.add(Pattern.quote(game.getUsername()) + " tried to swim in lava to escape .*?");
    }

    public void scoreMessage(String message, GameHandler game) {
        System.out.println("Message received: " + message);

        if (message.matches(".*? was slain by .*?" + Pattern.quote(game.getUsername()))) {
            incrementPenalty();
        } else if (message.matches( ".*?" + Pattern.quote(game.getUsername())+ " was slain by .*?")) {
            incrementBonus();
        }

        for (String cleanPattern : cleanPatternsKills) {
            if(message.matches(".*" + cleanPattern + ".*") && SystemMessageTracker.isSystem(message)) {
                incrementKills();
                System.out.println("KILL");
            }
        }

        for (String cleanPattern : cleanPatternsDeaths) {
            if(message.matches(".*" + cleanPattern + ".*") && SystemMessageTracker.isSystem(message)) {
                incrementDeaths();
                System.out.println("DEATH");
            }
        }
    }

    public void endGame(GameHandler game) {
        DelayedTaskManager.schedule(() -> {

            String line = scoreboardEntries.searchEntries("ROUNDS");
            boolean firstDfound = false;
            for (char c : line.toCharArray()) {
                if (c == 'W' || c == 'L' || c == 'D' && firstDfound) { rounds++; }
                else if (c == 'D') firstDfound = true;
            }

            int killCount = getKills();
            survived = rounds - deaths;

            Map<String, Object> data = new LinkedHashMap<>();
            data.put("username", game.getUsername());
            data.put("game", game.getCurrentGame());
            data.put("kills", killCount);
            data.put("survivals", survived);
            //data.put("team", 0);
            //data.put("Penalty", penaltyCount);
            //data.put("Bonus", bonusCount);
            new Thread(() -> HttpPostSender.sendScore(data)).start();
        }, 20);
    }

    public void incrementKills() {
        kills++;
    }

    public void incrementPenalty() {
        penaltyCount++;
    }

    public void incrementBonus() {
        bonusCount++;
    }

    public void incrementDeaths() {
        deaths++;
    }

    public int getKills() {
        return kills;
    }

    public void reset() {
        kills = 0;
        survived = 0;
        penaltyCount = 0;
        bonusCount = 0;
        rounds = 0;
        deaths = 0;
    }

}
