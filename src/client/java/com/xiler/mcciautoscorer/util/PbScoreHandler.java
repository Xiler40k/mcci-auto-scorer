package com.xiler.mcciautoscorer.util;

public class PbScoreHandler {

    private int kills;
    private int deaths;
    private int roundWins; //BE CAREFUL AS TIES CAN HAPPEN
    private int roundLosses;
    private int rounds;

    public void scoreMessage() {

    }

    public void endGame() {

    }

    public void reset() {
        kills = 0;
        deaths = 0;
        roundWins = 0;
        roundLosses = 0;
        rounds = 0;
    }

}
