package it.polimi.ingsw.model.deck;
import it.polimi.ingsw.model.card.*;
import it.polimi.ingsw.model.exceptions.*;

/**
 * Interface that implements method draw() for deck or deck buffer
 */
public interface Drawable {
    /**
     * Method for drawing cards
     * @return a card drawn from where specified
     */
    ResourceCard draw() throws EmptyDeckException, EmptyBufferException;
}
