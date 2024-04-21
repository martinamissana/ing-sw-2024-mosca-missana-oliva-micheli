package it.polimi.ingsw.model.deck;

import it.polimi.ingsw.model.exceptions.EmptyBufferException;
import it.polimi.ingsw.model.exceptions.EmptyDeckException;
import org.junit.Test;

import java.io.IOException;

public class DeckBufferTest {
    @Test(expected = EmptyBufferException.class)
    public void DrawTest() throws IOException, EmptyDeckException, EmptyBufferException {
        Deck deck = new Deck(DeckType.RESOURCE);
        DeckBuffer db = new DeckBuffer(deck);

        while(!deck.getCards().isEmpty()) deck.draw();
        db.refill();
        db.draw();
    }
}