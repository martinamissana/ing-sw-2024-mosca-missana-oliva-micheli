package it.polimi.ingsw.model.goal;

import it.polimi.ingsw.model.commonItem.Kingdom;
import junit.framework.TestCase;

public class DiagonalGoalTest extends TestCase {

    public void testGetColor() {
        Kingdom Expectedcolor=Kingdom.ANIMAL;
        DiagonalGoal diagonalGoal= new DiagonalGoal(0,0,Expectedcolor,DiagonalGoalType.UPWARD);
        assertEquals(Expectedcolor,diagonalGoal.getColor());
    }

    public void testGetType() {
        DiagonalGoalType Expectedtype=DiagonalGoalType.UPWARD;
        DiagonalGoal diagonalGoal= new DiagonalGoal(0,0,Kingdom.ANIMAL,Expectedtype);
        assertEquals(Expectedtype,diagonalGoal.getType());
    }
}