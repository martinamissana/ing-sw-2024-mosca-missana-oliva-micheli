package it.polimi.ingsw.model.game;

import junit.framework.TestCase;

public class CardsPresetTest extends TestCase {

    public void testGetResourceCards() {
        assertFalse(CardsPreset.getResourceCards().isEmpty());
    }
    public void testGetGoldenCards() {
        assertFalse(CardsPreset.getGoldenCards().isEmpty());
    }
}