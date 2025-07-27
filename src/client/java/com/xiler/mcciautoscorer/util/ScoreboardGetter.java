package com.xiler.mcciautoscorer.util;

import net.minecraft.client.MinecraftClient;
import net.minecraft.scoreboard.ScoreboardDisplaySlot;
import net.minecraft.scoreboard.ScoreboardEntry;
import net.minecraft.scoreboard.ScoreboardObjective;
import net.minecraft.text.Text;

import java.util.Collection;

public class ScoreboardGetter {

    public String searchEntries(String message) {

        net.minecraft.scoreboard.Scoreboard scoreboard = MinecraftClient.getInstance().world.getScoreboard();
        ScoreboardObjective sidebar = scoreboard.getObjectiveForSlot(ScoreboardDisplaySlot.SIDEBAR);
        Collection<ScoreboardEntry> entries = scoreboard.getScoreboardEntries(sidebar);

        for (ScoreboardEntry entry : entries) {
            Text nameText = entry.name();
            String rawName = nameText.getString();
            int score = entry.value();

            String line = rawName + score;

            if (line.contains(message)) {
                return line;
            }

        }
        return "";

    }

    public String[] getAllLines() {
        net.minecraft.scoreboard.Scoreboard scoreboard = MinecraftClient.getInstance().world.getScoreboard();
        ScoreboardObjective sidebar = scoreboard.getObjectiveForSlot(ScoreboardDisplaySlot.SIDEBAR);

        if (sidebar == null) return new String[0];

        Collection<ScoreboardEntry> entries = scoreboard.getScoreboardEntries(sidebar);
        String[] lines = new String[entries.size()];

        int index = 0;
        for (ScoreboardEntry entry : entries) {
            Text nameText = entry.name();
            String rawName = nameText.getString(); // Only the visible string
            lines[index++] = rawName;
        }

        return lines;
    }
}
