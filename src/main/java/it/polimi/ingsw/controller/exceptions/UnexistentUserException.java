package it.polimi.ingsw.controller.exceptions;

/**
 * thrown if the user does not exist
 */
public class UnexistentUserException extends Exception {
    public UnexistentUserException() {}
    public UnexistentUserException(String message) { super(message); }
}
