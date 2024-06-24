package it.polimi.ingsw.model.goal;

import it.polimi.ingsw.model.commonItem.Kingdom;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class DiagonalGoalTest {

    @Test
    public void testGetColor() {
        Kingdom expectedColor=Kingdom.ANIMAL;
        DiagonalGoal diagonalGoal= new DiagonalGoal(0,0,expectedColor,DiagonalGoalType.UPWARD);
        assertEquals(expectedColor,diagonalGoal.getColor());
    }

    @Test
    public void testGetType() {
        DiagonalGoalType expectedType=DiagonalGoalType.UPWARD;
        DiagonalGoal diagonalGoal= new DiagonalGoal(0,0,Kingdom.ANIMAL,expectedType);
        assertEquals(expectedType,diagonalGoal.getType());
    }
}