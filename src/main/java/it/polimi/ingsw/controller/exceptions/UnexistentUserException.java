package it.polimi.ingsw.controller.exceptions;

public class UnexistentUserException extends Exception {
    public UnexistentUserException() {}
    public UnexistentUserException(String message) { super(message); }
}
