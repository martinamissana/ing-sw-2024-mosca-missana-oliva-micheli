package it.polimi.ingsw.model.exceptions;

import java.io.Serializable;

/**
 * thrown when trying to set a card to an illegal position on the field
 */
public class IllegalCoordsException extends IllegalMoveException implements Serializable {
    public IllegalCoordsException() { super(); }
}
