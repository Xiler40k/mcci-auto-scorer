package com.xiler.mcciautoscorer.util;

public class GameHandler {

    //current game is name of current game. GameActive = true if a game is currently being played.
    private String currentGame;
    public boolean gameActive;
    public int currentRound;
    public String username;

    public void setCurrentGame(String currentGame) {
        this.currentGame = currentGame;
    }

    public String getCurrentGame() {
        return currentGame;
    }

    public void setGameActive(boolean gameActive) {
        this.gameActive = gameActive;
    }

    public boolean isGameActive() {
        return gameActive;
    }

    public void reset() {
        gameActive = false;
        currentGame = "";
        currentRound = 1;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUsername() {
        return username;
    }

    public void incrementCurrentRound() {
        currentRound++;
    }



}
