package com.xiler.mcciautoscorer.util;

import java.util.ArrayList;
import java.util.Arrays;

public class TgttosScoreHandler {

    private ArrayList<Integer> roundPlacements = new ArrayList<>(Arrays.asList(0, 0, 0, 0, 0, 0));

    public void scoreMessage() {

    }

    public void endGame() {

    }

    public void reset() {
        roundPlacements = new ArrayList<>(Arrays.asList(0, 0, 0, 0, 0, 0));
    }

}
