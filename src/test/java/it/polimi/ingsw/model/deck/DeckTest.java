package it.polimi.ingsw.model.deck;

import org.junit.Test;
import static org.junit.Assert.*;
import java.io.IOException;

public class DeckTest {
    @Test
    public void shuffleTest() throws IOException {
        Deck resource = new Deck(DeckType.RESOURCE);
        Deck other = new Deck(DeckType.RESOURCE);

        resource.shuffle();
        assertFalse(resource.equals(other));
    }
}