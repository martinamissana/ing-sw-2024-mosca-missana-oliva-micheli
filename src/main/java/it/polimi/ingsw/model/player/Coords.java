package it.polimi.ingsw.model.player;

import java.io.Serializable;

/**
 * Coordinates Class
 * used as key for the position of a card in the Field matrix map
 */
public class Coords implements Serializable {
    private final int x;
    private final int y;

    /**
     * Class constructor
     * @param x
     * @param y
     */
    public Coords(int x, int y) {
        this.x = x;
        this.y = y;
    }

    /**
     * gets X coordinate
     * @return int
     */
    public int getX() {
        return x;
    }

    /**
     * gets Y coordinate
     * @return int
     */
    public int getY() {
        return y;
    }
}
