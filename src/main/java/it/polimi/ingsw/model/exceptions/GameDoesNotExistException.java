package it.polimi.ingsw.model.exceptions;

import java.io.Serializable;

public class GameDoesNotExistException extends Exception implements Serializable {
    public GameDoesNotExistException(String s){super();}
}
