package it.polimi.ingsw.model.goal;

import it.polimi.ingsw.model.commonItem.Kingdom;

import static org.junit.jupiter.api.Assertions.assertEquals;


public class L_ShapeGoalTest{

    public void testGetType() {
        L_ShapeGoalType Expectedtype= L_ShapeGoalType.UP_RIGHT;
        L_ShapeGoal l_shapeGoal= new L_ShapeGoal(0,0, Kingdom.ANIMAL,Kingdom.INSECT,L_ShapeGoalType.UP_RIGHT);
        assertEquals(Expectedtype,l_shapeGoal.getType());
    }

    public void testGetMainColor() {
        Kingdom Expectedmaincolor= Kingdom.ANIMAL;
        L_ShapeGoal l_shapeGoal= new L_ShapeGoal(0,0, Kingdom.ANIMAL,Kingdom.INSECT,L_ShapeGoalType.UP_RIGHT);
        assertEquals(Expectedmaincolor,l_shapeGoal.getMainColor());
    }

    public void testGetSecondaryColor() {
        Kingdom Expectedsecondarycolor=Kingdom.INSECT;
        L_ShapeGoal l_shapeGoal= new L_ShapeGoal(0,0, Kingdom.ANIMAL,Kingdom.INSECT,L_ShapeGoalType.UP_RIGHT);
        assertEquals(Expectedsecondarycolor,l_shapeGoal.getSecondaryColor());
    }
}