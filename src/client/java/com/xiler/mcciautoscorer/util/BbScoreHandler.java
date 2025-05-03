package com.xiler.mcciautoscorer.util;

import com.xiler.mcciautoscorer.util.*;

import net.minecraft.client.MinecraftClient;
import net.minecraft.scoreboard.Scoreboard;
import net.minecraft.scoreboard.ScoreboardDisplaySlot;
import net.minecraft.scoreboard.ScoreboardEntry;
import net.minecraft.scoreboard.ScoreboardObjective;
import net.minecraft.text.Text;

import java.util.*;
import java.util.regex.Pattern;

public class BbScoreHandler {

    private int kills;
    private double roundWins;
    private double roundLosses;
    private double rounds;
    public ArrayList<String> cleanPatterns = new ArrayList<>();
    ScoreboardGetter scoreboardEntries = new ScoreboardGetter();

    public void initialisePatterns(GameHandler game) {
        cleanPatterns.add("was slain by .*?" + Pattern.quote(game.getUsername()));
        cleanPatterns.add("was shot by .*?" + Pattern.quote(game.getUsername()));
        cleanPatterns.add("lava to escape .*?" + Pattern.quote(game.getUsername()));
    }

    public void scoreMessage(String message, GameHandler game) { //SHOULD Probs check that this message was sent by the server
        System.out.println("Message received: " + message);

        for (String cleanPattern : cleanPatterns) {
            if(message.matches(".*" + cleanPattern + ".*") && SystemMessageTracker.isSystem(message)) {
                incrementKills();
                System.out.println("KILL");
            }
        }
    }

    public void incrementKills() {
        kills++;
    }

    public int getKills() {
        return kills;
    }

    public void endGame(GameHandler game) {
        // Schedule logic to run 20 ticks (~1 second) later
        DelayedTaskManager.schedule(() -> {


            String line = scoreboardEntries.searchEntries("ROUNDS");
            boolean firstDfound = false;
            for (char c : line.toCharArray()) {
                if (c == 'W') roundWins++;
                if (c == 'L') roundLosses++;
                if (c == 'D' && firstDfound) { roundWins += 0.5; roundLosses += 0.5; }
                else if (c == 'D') firstDfound = true;
            }

            rounds = roundLosses + roundWins;
            int killCount = getKills();

            System.out.println("BB OVERVIEW: \nKills: " + killCount + "\nRound wins: " + roundWins + "/" + rounds);

            Map<String, Object> data = new LinkedHashMap<>();
            data.put("username", game.getUsername());
            data.put("game", game.getCurrentGame());
            data.put("kills", kills);
            data.put("wins", roundWins);
            new Thread(() -> HttpPostSender.sendScore(data)).start();
        }, 20); // 20 ticks = 1 second
    }

    public void reset() {
        kills = 0;
        roundWins = 0;
        roundLosses = 0;
        rounds = 0;
    }

}
