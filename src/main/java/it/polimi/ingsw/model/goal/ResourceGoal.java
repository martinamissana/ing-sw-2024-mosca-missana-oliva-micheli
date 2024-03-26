package it.polimi.ingsw.model.goal;

import it.polimi.ingsw.model.commonItem.ItemBox;
import it.polimi.ingsw.model.player.Player;

import java.util.List;
import java.util.Map;

public class ResourceGoal extends Goal{
    private final List<ItemBox> resourceList;

    public ResourceGoal(int goalID,List<ItemBox> resourceList, int points) {
        super(goalID,points);
        this.resourceList=resourceList;
    }

    @Override
    public int evaluate(Player player){
        Map<ItemBox, Integer> totalResources = player.getPlayerField().getTotalResources();
        int goalpoints=0;
        int i;
        while(true) {
            for (i = 0; i < resourceList.size(); i++) {
                if (totalResources.get(resourceList.get(i)) != 0) {
                    //decreases by 1 the number of that resource
                    totalResources.replace(resourceList.get(i), totalResources.get(resourceList.get(i)), totalResources.get(resourceList.get(i)) - 1);
                } else {
                    //add goalpoints to scoreboard
                    return goalpoints;
                }
            }
            goalpoints = goalpoints + super.getPoints();
        }
    }

}

