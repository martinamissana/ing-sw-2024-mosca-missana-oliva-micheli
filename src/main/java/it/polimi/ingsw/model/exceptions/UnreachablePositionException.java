package it.polimi.ingsw.model.exceptions;

/**
 * Thrown when trying to set a card to a position in the field
 * which is not adjacent to any non-blocking corner, meaning
 * the position is not connected to other cards.
 */
public class UnreachablePositionException extends IllegalCoordsException {
}