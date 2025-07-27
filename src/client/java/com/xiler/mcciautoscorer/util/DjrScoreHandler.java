package com.xiler.mcciautoscorer.util;

import net.minecraft.scoreboard.ScoreboardEntry;

import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DjrScoreHandler {
    private int course;
    private int minutes;
    private int seconds;
    ScoreboardGetter scoreboardEntries = new ScoreboardGetter();
    //var for storing best times for 10 mins.
    private int[] bestMinutes = {10, 10, 10, 10};
    private int[] bestSeconds = new int[4];

    public void scoreMessage(String message, GameHandler game) { //NEED TO ADD A TIME CHECK HERE??

        Pattern pattern = Pattern.compile("Time: (\\d{2}):(\\d{2})\\.\\d{3}");
        Pattern pattern2 = Pattern.compile("Course #(\\d{1})");
        Matcher matcher = pattern.matcher(message);
        Matcher matcher2;

        if (matcher.find()) {
            minutes = Integer.parseInt(matcher.group(1));
            seconds = Integer.parseInt(matcher.group(2));
        }

        String line = scoreboardEntries.searchEntries("Course");
        matcher2 = pattern2.matcher(line);
        if (matcher2.find()) {
            course = Integer.parseInt(matcher2.group(1));
        }
        if (course > 3) {
            return;
        }

        if (minutes < bestMinutes[course] || (minutes == bestMinutes[course] && seconds < bestSeconds[course]) && SystemMessageTracker.isSystem(message)) {

            Map<String, Object> data = new LinkedHashMap<>();
            data.put("username", game.getUsername());
            data.put("game", game.getCurrentGame());
            data.put("course", course);
            data.put("minutes", minutes);
            data.put("seconds", seconds);

            new Thread(() -> HttpPostSender.sendScore(data)).start();

            bestMinutes[course] = minutes;
            bestSeconds[course] = seconds;
        }

    }

    public void reset() {
        for (int i = 0; i < 4; i++) {
            bestMinutes[i] = 0;
            bestSeconds[i] = 0;
        }
    }
}
