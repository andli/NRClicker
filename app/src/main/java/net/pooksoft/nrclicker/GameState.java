package net.pooksoft.nrclicker;

import android.util.Log;

import net.pooksoft.nrclicker.model.DataEntry;

import java.util.List;

/**
 * Tracks the game state of a game of Netrunner.
 */
public class GameState {

    public static final int CORP = 0;
    private int activePlayer = CORP;
    public static final int RUNNER = 1;
    private GameStateListener gameStateListener;
    private int numTurns = 0;

    public GameState(GameStateListener listener) {
        this.activePlayer = CORP;
        this.gameStateListener = listener;
    }

    private static String toStringFromNumber(double d) {
        if (d == (long) d)
            return String.format("%d", (long) d);
        else
            return String.format("%s", d);
    }

    public int getNumTurns() {
        return numTurns;
    }

    public int getActivePlayer() {
        return activePlayer;
    }

    public void switchPlayer() {
        activePlayer ^= 1;
    }

    public void startNextTurn() {
        numTurns++;
        gameStateListener.onNextTurn();
    }

    public String getNumRoundsAsString() {
        return toStringFromNumber(Math.floor((numTurns - this.numTurns % 2) / 2) + 1);
    }

    public void saveData(List<DataEntry> data) {
        Log.d("test", data.toString());

    }
}
