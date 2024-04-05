package it.polimi.ingsw.model.deck;

import it.polimi.ingsw.model.card.*;
import it.polimi.ingsw.model.game.*;
import org.junit.Test;
import static org.junit.Assert.*;
import java.io.IOException;

public class DeckTest {
    @Test
    public void shuffleTest() throws IOException {
        Deck deck = new Deck(DeckType.RESOURCE);
        Deck shuffled = new Deck(DeckType.RESOURCE);

        shuffled.shuffle();
        assertNotEquals(deck, shuffled);
    }


    @Test
    public void drawTest() throws IOException {
        Deck ResourceDeck = new Deck(DeckType.RESOURCE);
        assertFalse(ResourceDeck.getDeck().isEmpty());
        ResourceDeck.shuffle();

        Deck GoldenDeck = new Deck(DeckType.GOLDEN);
        assertFalse(GoldenDeck.getDeck().isEmpty());
        GoldenDeck.shuffle();

        for (ResourceCard resourceCard : ResourceDeck.getDeck()) {
            String s = "Card ID: " + resourceCard.getCardID();
            System.out.println(s);
        }
        // GoldenDeck.getDeck().stream().map(goldenCard -> "Card ID: " + goldenCard.getCardID()).forEach(System.out::println);


        Card card1 = ResourceDeck.draw();
        assertNotNull(card1);
        Card card2 = GoldenDeck.draw();
        assertNotNull(card2);
    }
}