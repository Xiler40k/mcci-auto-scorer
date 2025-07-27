package com.xiler.mcciautoscorer.util;

import java.util.*;

public class TgttosScoreHandler {
    //scoreboard at end of game.
    private ScoreboardGetter scoreboardEntries = new ScoreboardGetter();
    public int rounds = 0;
    private ArrayList<Integer> roundPlacements = new ArrayList<>(Arrays.asList(0, 0, 0, 0, 0, 0));

    public void endGame(GameHandler game)  {
        DelayedTaskManager.schedule(() -> {
            System.out.println("TGTTOS: End game called");

            String[] allLines = scoreboardEntries.getAllLines();
            List<String> placementLines = new ArrayList<>();
            List<String> allTokens = new ArrayList<>();
            int tokenNumber = 0;

            for (String line : allLines) {
                if (((line.matches(".*\\[\\d+(st|nd|rd|th)].*")) || line.matches(".*\\[/].*") || line.matches(".*\\[DNF].*"))) {
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
                        rounds++;
                    }
                }
            }

            if (roundPlacements.size() != 6) {
                System.out.println("Unexpected number of placements: " + roundPlacements);
            }

            System.out.println("TGTTOS placements: " + roundPlacements);

            Map<String, Object> data = new LinkedHashMap<>();
            data.put("username", game.getUsername());
            data.put("game", game.getCurrentGame());

            for (int i = 1; i <= 6; i++) {
                data.put("round" + i, roundPlacements.get(i - 1));
            }

            new Thread(() -> HttpPostSender.sendScore(data)).start();
            System.out.println("TGTTOSScoreHandler: Data sent to server: " + data);
            reset();
        }, 20);
    }

    public void reset() {
        roundPlacements = new ArrayList<>(Arrays.asList(0, 0, 0, 0, 0, 0));
    }

}
