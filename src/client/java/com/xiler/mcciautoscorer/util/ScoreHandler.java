package com.xiler.mcciautoscorer.util;

public class ScoreHandler {

    private boolean gameActive = false;
    private String gameName = "";
    private int kills = 0;
    private int deaths = 0;

    public ScoreHandler() {}

    public void reset() {
        gameActive = false;
        gameName = "";
        kills = 0;
        deaths = 0;
    }

    public void setGameActive(boolean gameActive) {
        this.gameActive = gameActive;
    }

    public boolean isGameActive() {
        return gameActive;
    }

    public void setGameName(String gameName) {
        this.gameName = gameName;
    }

    public void setGameName(String gameName, boolean gameActive) {
        this.gameName = gameName;
        this.gameActive = gameActive;
    }

    public String getGameName() {
        return gameName;
    }

    public void scoreMessage() {

    }

    public void postScores() {
        if(gameName.equals("Sky Battle")) {
            System.out.println("Kills: " + kills);
        } else if ( gameName.equals("Battle Box")) {
            System.out.println("Kills: " + kills + " Deaths: " + deaths);
            //count round wins too
        }
        reset();
    }

    public void incrementKill() {
        kills++;
    }

    public void incrementDeath() {
        deaths++;
    }

}
