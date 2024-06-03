package it.polimi.ingsw.model.exceptions;

/**
 * thrown when trying to set a cart to a position which is either
 * already occupied by a card or blocked by any adjacent card's corner
 */
public class OccupiedCoordsException extends IllegalCoordsException {
    public OccupiedCoordsException() { super(); }
    public OccupiedCoordsException(String string) { super(string);}
}
