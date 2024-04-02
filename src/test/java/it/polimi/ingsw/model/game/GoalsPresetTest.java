package it.polimi.ingsw.model.game;

import junit.framework.TestCase;

import java.io.IOException;

public class GoalsPresetTest extends TestCase {

    public void testGetDiagonalGoals() throws IOException {
        assertFalse(GoalsPreset.getDiagonalGoals().isEmpty());
    }

    public void testGetLShapeGoals() throws IOException {
        assertFalse(GoalsPreset.getLShapeGoals().isEmpty());
    }

    public void testGetResourceGoals() throws IOException {
        assertFalse(GoalsPreset.getResourceGoals().isEmpty());
    }
}