package it.polimi.ingsw.model.exceptions;

/**
 * thrown when trying to set a card to a position which is not adjacent to any non-blocking corner
 */
public class UnreachablePositionException extends IllegalCoordsException {
    public UnreachablePositionException() { super(); }
}
