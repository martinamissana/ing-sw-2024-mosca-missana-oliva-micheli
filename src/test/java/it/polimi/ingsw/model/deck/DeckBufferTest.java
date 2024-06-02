package it.polimi.ingsw.model.deck;

import it.polimi.ingsw.model.exceptions.EmptyBufferException;
import it.polimi.ingsw.model.exceptions.EmptyDeckException;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertThrows;

public class DeckBufferTest {
    @Test
    public void DrawTest() throws IOException, EmptyDeckException, EmptyBufferException {
        Deck deck = new Deck(DeckType.RESOURCE);
        DeckBuffer db = new DeckBuffer(deck);
        while(!deck.getCards().isEmpty()) deck.draw();
        db.refill();
        assertThrows(EmptyBufferException.class, () -> {
            db.draw();
        });
    }
}