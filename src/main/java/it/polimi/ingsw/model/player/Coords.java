package it.polimi.ingsw.model.player;

import java.io.Serializable;
import java.util.Objects;

/**
 * Coordinates Class<br>
 * Used as key for the position of a card in the {@code Field} matrix map
 */
public class Coords implements Serializable {
    private final int x;
    private final int y;

    /**
     * Class constructor
     * @param x first coordinate of the matrix.<br> positive and negative values
     *          correspond to positions to the {@code EAST} and {@code WEST} respectively
     * @param y second coordinate of the matrix.<br> positive and negative values
     *          correspond to positions to the {@code NORTH} and {@code SOUTH} respectively
     */
    public Coords(int x, int y) {
        this.x = x;
        this.y = y;
    }

    /**
     * Gets the {@code X} coordinate
     * @return int
     */
    public int getX() { return x; }

    /**
     * Gets the {@code Y} coordinate
     * @return int
     */
    public int getY() { return y; }


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
    public int hashCode() { return Objects.hash(x, y); }
}
