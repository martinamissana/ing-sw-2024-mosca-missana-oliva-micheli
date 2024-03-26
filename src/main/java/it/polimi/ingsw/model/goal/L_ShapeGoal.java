package it.polimi.ingsw.model.goal;

import it.polimi.ingsw.model.commonItem.Kingdom;
import it.polimi.ingsw.model.player.Player;

public class L_ShapeGoal extends Goal{
    private final Kingdom mainColor;
    private final Kingdom secondaryColor;
    private final L_ShapeGoalType type;
    boolean[][] done= new boolean[40][40];

    public L_ShapeGoal(int goalID,int points,Kingdom mainColor,Kingdom secondaryColor, L_ShapeGoalType type) {
        super(goalID,points);
        this.mainColor=mainColor;
        this.secondaryColor=secondaryColor;
        this.type=type;
    }

    public L_ShapeGoalType getType() {
        return type;
    } //not used
    public Kingdom getMainColor() {
        return mainColor;
    } //not used
    public Kingdom getSecondaryColor() {
        return secondaryColor;
    }  //not used


    @Override
    public int evaluate(Player player) {
        return 0;
    }
}
