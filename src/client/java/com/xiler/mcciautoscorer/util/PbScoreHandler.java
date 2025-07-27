package com.xiler.mcciautoscorer.util;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.regex.Pattern;

public class PbScoreHandler {
    //bb but counts all other stuff.

    private int kills;
    private int deaths;
    private int rounds;
    private int survived;
    private int penaltyCount;
    private int bonusCount;
    private int roundWins;
    private int roundLosses;
    public ArrayList<String> cleanPatternsKills = new ArrayList<>();
    public ArrayList<String> cleanPatternsDeaths = new ArrayList<>();
    public ArrayList<String> cleanPatternsPenalties = new ArrayList<>();
    public ArrayList<String> cleanPatternsBonuses = new ArrayList<>();
    public ScoreboardGetter scoreboardEntries = new ScoreboardGetter();

    public void initialisePatterns(GameHandler game) {
        cleanPatternsKills.add("was slain by .*?" + Pattern.quote(game.getUsername()));
        cleanPatternsKills.add("was blown up by .*?" + Pattern.quote(game.getUsername()));
        cleanPatternsKills.add("was shot by .*?" + Pattern.quote(game.getUsername()));
        cleanPatternsKills.add("lava to escape .*?" + Pattern.quote(game.getUsername())); //Grey area
        cleanPatternsKills.add("was eliminated with magic by .*?" + Pattern.quote(game.getUsername()) + " using [Splash Potion of Harming]");
        cleanPatternsKills.add("was eliminated with magic by .*?" + Pattern.quote(game.getUsername()) + " using .*?Orb.*?");

        cleanPatternsDeaths.add(Pattern.quote(game.getUsername()) + " was eliminated");
        cleanPatternsDeaths.add(Pattern.quote(game.getUsername()) + " was slain by .*?");
        cleanPatternsDeaths.add(Pattern.quote(game.getUsername()) + " was shot by .*?");
        cleanPatternsDeaths.add(Pattern.quote(game.getUsername()) + " tried to swim in lava to escape .*?");
        cleanPatternsDeaths.add(Pattern.quote(game.getUsername()) + " was blown up by .*?");
        cleanPatternsDeaths.add(Pattern.quote(game.getUsername()) + " was eliminated with magic by .*? using [Splash Potion of Harming]");
        cleanPatternsDeaths.add(Pattern.quote(game.getUsername()) + " was eliminated with magic by .*? using .*?Orb.*?");


        cleanPatternsPenalties.add(".*? was slain by .*?" + Pattern.quote(game.getUsername()));
        cleanPatternsPenalties.add("was blown up by .*?" + Pattern.quote(game.getUsername()));
        cleanPatternsPenalties.add("was eliminated with magic by .*?" + Pattern.quote(game.getUsername()) + " using .*?Orb.*?");

        cleanPatternsBonuses.add(Pattern.quote(game.getUsername()) + " was slain by .*?");
        cleanPatternsBonuses.add(Pattern.quote(game.getUsername()) + " was blown up by .*?");
        cleanPatternsBonuses.add(Pattern.quote(game.getUsername()) + " was eliminated with magic by .*? using .*?Orb.*?");
    }

    public void scoreMessage(String message, GameHandler game) {
        System.out.println("Message received: " + message);

        for (String cleanPattern : cleanPatternsPenalties) {
            if (message.matches(".*" + cleanPattern + ".*") && SystemMessageTracker.isSystem(message)) {
                incrementPenalty();
                System.out.println("PENALTY");
            }
        }

        for (String cleanPattern : cleanPatternsBonuses) {
            if (message.matches(".*" + cleanPattern + ".*") && SystemMessageTracker.isSystem(message)) {
                incrementBonus();
                System.out.println("BONUS");
            }
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

        //CAN BE OPTIMISED
    }

    public void endGame(GameHandler game) {
        DelayedTaskManager.schedule(() -> {

            String line = scoreboardEntries.searchEntries("ROUNDS");
            boolean firstDfound = false;
            for (char c : line.toCharArray()) {
                if (c == 'W') roundWins++;
                if (c == 'L') roundLosses++;
                if (c == 'D' && firstDfound) { roundLosses++ ;}
                else if (c == 'D') firstDfound = true;
            }

            rounds = roundLosses + roundWins;

            int killCount = getKills();
            survived = rounds - deaths;

            Map<String, Object> data = new LinkedHashMap<>();
            data.put("username", game.getUsername());
            data.put("game", game.getCurrentGame());
            data.put("kills", killCount);
            data.put("survivals", survived);
            data.put("Wins", roundWins);
            data.put("Penalty", penaltyCount);
            data.put("Bonus", bonusCount);
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
        deaths = 0;
        rounds = 0;
        survived = 0;
        penaltyCount = 0;
        bonusCount = 0;
        roundWins = 0;
        roundLosses = 0;
    }

}
