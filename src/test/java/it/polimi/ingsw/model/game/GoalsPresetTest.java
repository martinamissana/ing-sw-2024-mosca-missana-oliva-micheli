package it.polimi.ingsw.model.game;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertFalse;

public class GoalsPresetTest{

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