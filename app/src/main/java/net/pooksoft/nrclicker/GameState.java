package net.pooksoft.nrclicker;

/**
 * Tracks the game state of a game of Netrunner.
 */
public class GameState {

    public static final int CORP = 0;
    public static final int RUNNER = 1;

    private int activePlayer = CORP;
    private GameStateListener gameStateListener;

    public int getNumTurns() {
        return numTurns;
    }

    public int getActivePlayer() {
        return activePlayer;
    }

    private int numTurns = 0;

    public GameState(GameStateListener listener) {
        this.activePlayer = CORP;
        this.gameStateListener = listener;
    }

    public void startNextTurn() {
        activePlayer ^= 1;
        numTurns++;
        gameStateListener.onNextTurn();
    }

    public void end() {
        // TODO: save stats
    }

    public String getNumRoundsAsString() {
        return fmt(Math.floor((numTurns - this.numTurns % 2) / 2) + 1);
    }

    private static String fmt(double d)
    {
        if(d == (long) d)
            return String.format("%d",(long)d);
        else
            return String.format("%s",d);
    }
}
