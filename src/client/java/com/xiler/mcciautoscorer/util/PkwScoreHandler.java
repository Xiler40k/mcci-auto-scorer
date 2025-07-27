package com.xiler.mcciautoscorer.util;

import com.xiler.mcciautoscorer.util.*;

import java.util.*;
import java.util.regex.Pattern;
import java.util.regex.Matcher;

public class PkwScoreHandler {
    //take info from leaderboard when died/game finished

    private ArrayList<Integer> roundPlacements = new ArrayList<>(Arrays.asList(0, 0, 0, 0, 0, 0, 0, 0));
    String deathPattern;
    ScoreboardGetter scoreboardEntries = new ScoreboardGetter();
    public int leaps = 0;
    private int placement = 0;

    public void initialisePatterns(GameHandler game) {
        deathPattern = ".*" + Pattern.quote(game.getUsername()) + ", you were eliminated in .*";
    }

    public String getDeathPattern() {
        return deathPattern;
    }

    public void printScoreboard() {
        DelayedTaskManager.schedule(() -> {
            String line = scoreboardEntries.searchEntries("[");
            if (line != null) {
                System.out.println("Scoreboard LEAPS: " + line);
            }
        }, 20);
    }

    public void endGame(String message, GameHandler game)  {
        DelayedTaskManager.schedule(() -> {
            System.out.println("PkwScoreHandler: End game called");

            Pattern pattern = Pattern.compile(game.getUsername() + ", you were eliminated in (\\d+)(th|st|nd|rd)");
            Matcher matcher = pattern.matcher(message);

            if (matcher.find()) {
                try {
                    placement = Integer.parseInt(matcher.group(1));
                    System.out.println("Placement number: " + placement);
                } catch (NumberFormatException e) {
                    System.err.println("Failed to parse placement from message: " + message);
                }
            } else {
                System.err.println("No placement match found in message: " + message);
            }

            String[] allLines = scoreboardEntries.getAllLines();
            List<String> placementLines = new ArrayList<>();
            List<String> allTokens = new ArrayList<>();
            int tokenNumber = 0;

            for (String line : allLines) {
                if (((line.matches(".*\\[\\d+(st|nd|rd|th)].*")) || line.matches(".*\\[/].*"))) {
                    placementLines.add(line);
                    if (placementLines.size() == 2) break; // we want only two lines
                }
            }

            Collections.reverse(placementLines);

            for (String line : placementLines) {
                System.out.println("Found line: " + line);
                String[] tokens = line.split(" ");
                for (String token : tokens) {
                    allTokens.add(token.replaceAll("\uE005", ""));
                }
            }

            for (String token : allTokens) {
                if (token.matches("\\[\\d+(st|nd|rd|th)]")) {
                    String numStr = token.replaceAll("\\D+", "");
                    if (tokenNumber < roundPlacements.size()) {
                        roundPlacements.set(tokenNumber++, Integer.parseInt(numStr));
                        leaps++;
                    }
                }
            }

            if (roundPlacements.size() != 8) {
                System.out.println("Unexpected number of placements: " + roundPlacements);
            }

            System.out.println("Pkw placements: " + roundPlacements);
            if (roundPlacements.size() >= 8 && roundPlacements.get(7) == 1) {
                placement = 1;
            }

            Map<String, Object> data = new LinkedHashMap<>();
            data.put("username", game.getUsername());
            data.put("game", game.getCurrentGame());
            data.put("placement", placement);
            data.put("leaps", leaps);

            for (int i = 1; i <= 8; i++) {
                data.put("leap" + i, roundPlacements.get(i - 1));
            }

            new Thread(() -> HttpPostSender.sendScore(data)).start();
            System.out.println("PkwScoreHandler: Data sent to server: " + data);
            reset();
            game.setCurrentGame(null);
        }, 100);
    }



    public void reset() {
        roundPlacements = new ArrayList<>(Arrays.asList(0, 0, 0, 0, 0, 0, 0, 0));
        placement = 0;
        leaps = 0;
    }
}
