package com.xiler.mcciautoscorer.util;

import com.xiler.mcciautoscorer.util.*;

import java.util.*;
import java.util.regex.Pattern;

public class HitwScoreHandler {
    //take info from scoreboard once you've died!

    private int placement;


    public void scoreMessage(String message, GameHandler game) {
        if (message.matches(".*Xiler40k, you were eliminated in (\\d+)(th|st|nd|rd)")) {
            //get the number at \\d+ and set it to placement
            String[] parts = message.split(" ");

            if (parts.length > 6) {
                try {
                    System.out.println("Placement number: " + Integer.parseInt(parts[6].replaceAll("[^0-9]", "")));
                    placement = Integer.parseInt(parts[6].replaceAll("[^0-9]", ""));
                } catch (NumberFormatException e) {
                    System.err.println("Failed to parse placement from message: " + message);
                }
            } else {
                System.err.println("Unexpected message format: " + message);
            }
            endGame(game);
        }
    }

    public void endGame(GameHandler game) {
        DelayedTaskManager.schedule(() -> {

            Map<String, Object> data = new LinkedHashMap<>();
            data.put("username", game.getUsername());
            data.put("game", game.getCurrentGame());
            data.put("placement", placement);

            System.out.println("HITW OVERVIEW: \nPlacement: " + placement);

            // Send the data to the server
            new Thread(() -> HttpPostSender.sendScore(data)).start();

        }, 20);
    }

    public void endGame(GameHandler game, int placementInput) {
        DelayedTaskManager.schedule(() -> {

            Map<String, Object> data = new LinkedHashMap<>();
            data.put("username", game.getUsername());
            data.put("game", game.getCurrentGame());
            data.put("placement", placementInput);

            System.out.println("HITW OVERVIEW: \nPlacement: " + placementInput);

            // Send the data to the server
            new Thread(() -> HttpPostSender.sendScore(data)).start();

        }, 20);
    }

    public void reset() {
        placement = 0;
    }

}
