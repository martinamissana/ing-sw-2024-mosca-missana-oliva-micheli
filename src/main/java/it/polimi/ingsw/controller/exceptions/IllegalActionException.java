package it.polimi.ingsw.controller.exceptions;

/**
 * thrown when either playCard or drawCard are called when the game's Action state isn't set to PLAY or DRAW respectively
 */
public class IllegalActionException extends Exception {
}