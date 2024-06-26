package it.polimi.ingsw.model.exceptions;

/**
 * Thrown when trying to set a cart to a position which is either
 * already occupied by a card or blocked by any of the adjacent card's blocking corner.
 */
public class OccupiedCoordsException extends IllegalCoordsException {
}
