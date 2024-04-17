package it.polimi.ingsw.controller.exceptions;

import java.io.Serializable;

/**
 * thrown when either playCard or drawCard are called when the game's Action state isn't set to PLAY or DRAW respectively
 */
public class IllegalActionException extends Exception implements Serializable {
    public IllegalActionException() { super(); }
}