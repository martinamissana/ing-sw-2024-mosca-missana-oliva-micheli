package it.polimi.ingsw.model.exceptions;

import java.io.Serializable;

public class LobbyDoesNotExistsException extends Exception implements Serializable {
    public LobbyDoesNotExistsException(String s){ super(); }
}
