package it.polimi.ingsw.model.deck;

import it.polimi.ingsw.model.card.*;
import it.polimi.ingsw.model.exceptions.EmptyDeckException;
import org.junit.Test;
import static org.junit.Assert.*;
import java.io.IOException;

public class DeckTest {
    @Test
    public void ShuffleAndDrawTest() throws IOException, EmptyDeckException {
        Deck ResourceDeck = new Deck(DeckType.RESOURCE);
        assertFalse(ResourceDeck.getDeck().isEmpty());
        ResourceDeck.shuffle();

        Deck GoldenDeck = new Deck(DeckType.GOLDEN);
        assertFalse(GoldenDeck.getDeck().isEmpty());
        GoldenDeck.shuffle();

        System.out.println("Resource Deck:");
        for (int i = 0; i < ResourceDeck.getDeck().size(); i++) {
            System.out.print("[" + ResourceDeck.getDeck().get(i).getCardID() + "]");
        }
        System.out.println("\n\nGolden Deck:");
        for (int i = 0; i < GoldenDeck.getDeck().size(); i++) {
            System.out.print("[" + GoldenDeck.getDeck().get(i).getCardID() + "]");
        }

        Card card1 = ResourceDeck.draw();
        assertNotNull(card1);
        Card card2 = GoldenDeck.draw();
        assertNotNull(card2);

        System.out.println("\n\nCards drawn: [" + card1.getCardID() + "] & [" + card2.getCardID() + "]\n");

        System.out.println("Resource Deck:");
        for (int i = 0; i < ResourceDeck.getDeck().size(); i++) {
            System.out.print("[" + ResourceDeck.getDeck().get(i).getCardID() + "]");
        }
        System.out.println("\n\nGolden Deck:");
        for (int i = 0; i < GoldenDeck.getDeck().size(); i++) {
            System.out.print("[" + GoldenDeck.getDeck().get(i).getCardID() + "]");
        }
    }
}