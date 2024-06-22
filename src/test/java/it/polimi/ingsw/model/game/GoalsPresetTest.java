package it.polimi.ingsw.model.game;

import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertFalse;

public class GoalsPresetTest{

    @Test
    public void testGetDiagonalGoals() throws IOException {
        assertFalse(GoalsPreset.getDiagonalGoals().isEmpty());
    }

    @Test
    public void testGetLShapeGoals() throws IOException {
        assertFalse(GoalsPreset.getLShapeGoals().isEmpty());
    }

    @Test
    public void testGetResourceGoals() throws IOException {
        assertFalse(GoalsPreset.getResourceGoals().isEmpty());
    }
}