package it.polimi.ingsw.model.player;

import java.io.Serializable;
import java.util.Objects;

/**
 * Coordinates Class
 * used as key for the position of a card in the Field matrix map
 */
public class Coords implements Serializable {
    private final int x;
    private final int y;

    /**
     * Class constructor
     * @param x first coordinate of the matrix. positive and negative values correspond to positions to the east and west respectively
     * @param y second coordinate of the matrix. positive and negative values correspond to positions to the north and south respectively
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

    /**
     * pairs of coordinates are equal to each other if and only if both the x and y values of the first pair are equal to the second pair's
     * @param o object to compare with the caller
     * @return boolean
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        var that = (Coords) o;
        return this.x == that.x && this.y == that.y;
    }

    @Override
    public String toString() { return "(" + x + ", " + y + ")"; }

    @Override
    public int hashCode() {
        return Objects.hash(x, y);
    }
}
