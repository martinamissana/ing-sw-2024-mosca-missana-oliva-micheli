package it.polimi.ingsw.model.exceptions;

/**
 * thrown when trying to set a card to an illegal position on the field
 */
public class IllegalCoordsException extends IllegalMoveException {
    public IllegalCoordsException() { super(); }
}
