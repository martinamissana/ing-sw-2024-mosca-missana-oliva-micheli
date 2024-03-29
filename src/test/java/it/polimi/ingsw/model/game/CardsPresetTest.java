package it.polimi.ingsw.model.game;

import junit.framework.TestCase;

import java.io.IOException;

public class CardsPresetTest extends TestCase {

    public void testGetResourceCards() throws IOException {
        assertFalse(CardsPreset.getResourceCards().isEmpty());
    }
    public void testGetGoldenCards() throws IOException {
        assertFalse(CardsPreset.getGoldenCards().isEmpty());
    }

    public void testGetStarterCards() throws IOException {
        assertFalse(CardsPreset.getStarterCards().isEmpty());
    }
}