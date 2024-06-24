package it.polimi.ingsw.model.game;

import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertFalse;

public class CardsPresetTest {

    @Test
    public void testGetResourceCards() throws IOException {
        assertFalse(CardsPreset.getResourceCards().isEmpty());
    }
    @Test
    public void testGetGoldenCards() throws IOException {
        assertFalse(CardsPreset.getGoldenCards().isEmpty());
    }

    @Test
    public void testGetStarterCards() throws IOException {
        assertFalse(CardsPreset.getStarterCards().isEmpty());
    }
}