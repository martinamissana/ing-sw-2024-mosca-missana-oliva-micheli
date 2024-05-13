package it.polimi.ingsw.controller.exceptions;

/**
 * thrown when a user who isn't connected to any lobby requests an action that can only be performed while in a lobby
 */
public class NotConnectedToLobbyException extends Exception {
    public NotConnectedToLobbyException() { super(); }
}
