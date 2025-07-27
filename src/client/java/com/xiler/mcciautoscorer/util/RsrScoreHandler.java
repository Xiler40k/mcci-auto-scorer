package com.xiler.mcciautoscorer.util;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RsrScoreHandler {
    //take score from chat for kills and scoreboard for placements when game over/died.

    private int placement;
    private int kills;
    public ArrayList<String> cleanPatternsKills = new ArrayList<>();
    ScoreboardGetter scoreboardEntries = new ScoreboardGetter();

    public void initialisePatterns(GameHandler game) {
        cleanPatternsKills.add("was eliminated by .*?" + Pattern.quote(game.getUsername()));
    }

    public void scoreMessage(String message, GameHandler game) {
        System.out.println("Message received: " + message);
        for(String cleanPattern : cleanPatternsKills) {
            if(message.matches(".*" + cleanPattern + ".*") && SystemMessageTracker.isSystem(message)) {
                incrementKills();
                System.out.println("KILL");
            }
        }
    }

    public void scorePlacement(String rawMessage, GameHandler game) {

        Pattern pattern = Pattern.compile(game.getUsername() + ", you were eliminated in (\\d+)(th|st|nd|rd)");
        Matcher matcher = pattern.matcher(rawMessage);

        if (matcher.find()) {
            try {
                placement = Integer.parseInt(matcher.group(1));
                System.out.println("Placement number: " + placement);
            } catch (NumberFormatException e) {
                System.err.println("Failed to parse placement from message: " + rawMessage);
            }
        } else {
            System.err.println("No placement match found in message: " + rawMessage);
        }
    }

    public void endGame(GameHandler game) {
        DelayedTaskManager.schedule(() -> {

            Map<String, Object> data = new LinkedHashMap<>();
            data.put("placement", placement);
            data.put("kills", kills);
            new Thread(() -> HttpPostSender.sendScore(data)).start();

        }, 20);
    }

    public void incrementKills() {
        kills++;
    }

    public void reset() {
        placement = 0;
        kills = 0;
    }

}
