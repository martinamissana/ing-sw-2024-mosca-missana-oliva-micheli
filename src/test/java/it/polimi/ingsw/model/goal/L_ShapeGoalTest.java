package it.polimi.ingsw.model.goal;

import it.polimi.ingsw.model.commonItem.Kingdom;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;


public class L_ShapeGoalTest{

    @Test
    public void testGetType() {
        L_ShapeGoalType expectedType= L_ShapeGoalType.UP_RIGHT;
        L_ShapeGoal l_shapeGoal= new L_ShapeGoal(0,0, Kingdom.ANIMAL,Kingdom.INSECT,L_ShapeGoalType.UP_RIGHT);
        assertEquals(expectedType,l_shapeGoal.getType());
    }

    @Test
    public void testGetMainColor() {
        Kingdom expectedMainColor= Kingdom.ANIMAL;
        L_ShapeGoal l_shapeGoal= new L_ShapeGoal(0,0, Kingdom.ANIMAL,Kingdom.INSECT,L_ShapeGoalType.UP_RIGHT);
        assertEquals(expectedMainColor,l_shapeGoal.getMainColor());
    }


    @Test
    public void testGetSecondaryColor() {
        Kingdom expectedSecondaryColor=Kingdom.INSECT;
        L_ShapeGoal l_shapeGoal= new L_ShapeGoal(0,0, Kingdom.ANIMAL,Kingdom.INSECT,L_ShapeGoalType.UP_RIGHT);
        assertEquals(expectedSecondaryColor,l_shapeGoal.getSecondaryColor());
    }
}