package it.polimi.ingsw.model.deck;

import it.polimi.ingsw.model.card.Card;
import it.polimi.ingsw.model.exceptions.EmptyDeckException;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class DeckTest {

    @Test
    public void ShuffleAndDrawTest() throws IOException, EmptyDeckException {
        Deck ResourceDeck = new Deck(DeckType.RESOURCE);
        assertFalse(ResourceDeck.getCards().isEmpty());
        ResourceDeck.shuffle();

        Deck GoldenDeck = new Deck(DeckType.GOLDEN);
        assertFalse(GoldenDeck.getCards().isEmpty());
        GoldenDeck.shuffle();

        Card card1 = ResourceDeck.draw();
        assertNotNull(card1);
        Card card2 = GoldenDeck.draw();
        assertNotNull(card2);
    }
}