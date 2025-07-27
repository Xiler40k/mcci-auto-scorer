package com.xiler.mcciautoscorer.util;
import java.util.*;
import java.util.regex.Pattern;
import java.util.regex.Matcher;
import com.xiler.mcciautoscorer.util.GameHandler;
import com.xiler.mcciautoscorer.util.HttpPostSender;


public class SbScoreHandler {
    //read outlived and kills from various chat places. Team placement may be tricky??? But chat is good.

    private int kills;
    private int outlived;
    private int teamPlacement; //LATER
    private boolean gameWinner; //LATER
    public ArrayList<String> cleanPatterns = new ArrayList<>();

    public void initialisePatterns(GameHandler game) {
        cleanPatterns.add("was slain by .*?" + Pattern.quote(game.getUsername()));
        cleanPatterns.add("was shot by .*?" + Pattern.quote(game.getUsername()));
        cleanPatterns.add("lava to escape .*?" + Pattern.quote(game.getUsername()));
        cleanPatterns.add("was eliminated with magic by .*?" + Pattern.quote(game.getUsername()) + " using [Splash Potion of Harming]");
        cleanPatterns.add("was eliminated with magic by .*?" + Pattern.quote(game.getUsername()) + " using .*?Orb.*?");
        cleanPatterns.add("didn't want to live in the same world as .*?" + Pattern.quote(game.getUsername()));
        cleanPatterns.add("was spleefed by .*?" + Pattern.quote(game.getUsername()));
        cleanPatterns.add("was blown up by .*?" + Pattern.quote(game.getUsername()));
        cleanPatterns.add("suffocated in a wall whilst fighting .*?" + Pattern.quote(game.getUsername()));
        cleanPatterns.add("lava to escape .*?" + Pattern.quote(game.getUsername()));
        cleanPatterns.add("was hooked to death by .*?" + Pattern.quote(game.getUsername()));
    }

    public void scoreMessage(String message, GameHandler game) {
        System.out.println("Message received: " + message);

        Pattern pattern = Pattern.compile("Outlived (\\d+) players\\.");
        Matcher matcher = pattern.matcher(message);

        if (matcher.find()) {
            try {
                outlived = Integer.parseInt(matcher.group(1));
                System.out.println("Outlived: " + outlived);
            } catch (NumberFormatException e) {
                System.err.println("Failed to parse outlived players: " + e.getMessage());
            }
        }

        for (String cleanKill : cleanPatterns) {
            if (message.matches(".*?" + cleanKill + ".*?") && SystemMessageTracker.isSystem(message)) {
                incrementKills();
                System.out.println("KILL");
            }
        }
    }

    public void endGame(GameHandler game) {
        DelayedTaskManager.schedule(() -> {

            Map<String, Object> data = new LinkedHashMap<>();
            data.put("username", game.getUsername());
            data.put("game", game.getCurrentGame());
            data.put("outlived", outlived);
            data.put("kills", kills);

            new Thread(() -> HttpPostSender.sendScore(data)).start();

        }, 20);
    }

    public void incrementKills() {
        kills++;
    }

    public void reset() {
        kills = 0;
        outlived = 0;
        teamPlacement = 0;
        gameWinner = false;
    }

}
