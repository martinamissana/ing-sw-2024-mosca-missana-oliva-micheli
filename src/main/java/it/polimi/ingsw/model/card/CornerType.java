package it.polimi.ingsw.model.card;

import java.io.Serializable;

/**
 * Enumeration used to indicate the position of the corner on the card;
 * north being on the top-left and continuing clockwise, as shown here:
 * <pre>
 * north---east
 * |          |
 * west---south
 * </pre>
 */
public enum CornerType implements Serializable {
    NORTH,
    EAST,
    SOUTH,
    WEST
}
