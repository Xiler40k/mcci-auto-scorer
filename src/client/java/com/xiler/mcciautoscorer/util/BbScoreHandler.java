package com.xiler.mcciautoscorer.util;

import net.minecraft.client.MinecraftClient;
import net.minecraft.scoreboard.Scoreboard;
import net.minecraft.scoreboard.ScoreboardDisplaySlot;
import net.minecraft.scoreboard.ScoreboardEntry;
import net.minecraft.scoreboard.ScoreboardObjective;
import net.minecraft.text.Text;

import java.util.Collection;
import java.util.regex.Pattern;

public class BbScoreHandler {

    private int kills;
    private int roundWins;
    private int roundLossess;
    int rounds;

    public void scoreMessage(String message, GameHandler game) {
        String cleanPattern = "was slain by .*?" + Pattern.quote(game.getUsername());
        System.out.println("Message received: " + message);

        if(message.matches(".*" + cleanPattern + ".*")) {
            kills++;
            System.out.println("KILL");
        }
    }

    public void endGame(GameHandler game) {
        // Schedule logic to run 20 ticks (~1 second) later
        DelayedTaskManager.schedule(() -> {
            Scoreboard scoreboard = MinecraftClient.getInstance().world.getScoreboard();
            ScoreboardObjective sidebar = scoreboard.getObjectiveForSlot(ScoreboardDisplaySlot.SIDEBAR);
            Collection<ScoreboardEntry> entries = scoreboard.getScoreboardEntries(sidebar);

            for (ScoreboardEntry entry : entries) {
                Text nameText = entry.name();
                String rawName = nameText.getString();
                int score = entry.value();

                String line = rawName + score;

                if (line.contains("ROUNDS")) {
                    for (char c : line.toCharArray()) {
                        if (c == 'W') roundWins++;
                        if (c == 'L') roundLossess++;
                    }
                }
            }

            rounds = roundLossess + roundWins;

            System.out.println("BB OVERVIEW: \nKills: " + kills + "\nRound wins: " + roundWins + "/" + rounds);
            HttpPostSender.sendScore(game.getUsername(), game.getCurrentGame(), kills, roundWins);
        }, 20); // 20 ticks = 1 second
    }

    public void reset() {
        kills = 0;
        roundWins = 0;
        roundLossess = 0;
        rounds = 0;
    }

}
