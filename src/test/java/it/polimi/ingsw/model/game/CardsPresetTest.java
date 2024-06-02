package it.polimi.ingsw.model.game;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertFalse;

public class CardsPresetTest {

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