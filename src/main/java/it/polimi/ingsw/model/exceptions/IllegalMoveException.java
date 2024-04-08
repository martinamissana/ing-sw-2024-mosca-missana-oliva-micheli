package it.polimi.ingsw.model.exceptions;

/**
 * thrown when violating the game's rules when placing a card
 */
public class IllegalMoveException extends Exception {
    public IllegalMoveException() { super(); }
}