package it.polimi.ingsw.model.exceptions;

import java.io.Serializable;

public class EmptyDeckException extends Exception implements Serializable {
    public EmptyDeckException(String s){
        super();
    }
}
