package it.polimi.ingsw.model.exceptions;

import java.io.Serializable;

public class EmptyBufferException extends Exception implements Serializable {
    public EmptyBufferException(String s){
        super(s);
    }
}
