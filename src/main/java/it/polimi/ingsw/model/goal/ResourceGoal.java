package it.polimi.ingsw.model.goal;

import it.polimi.ingsw.model.commonItem.ItemBox;

import java.util.List;

public class ResourceGoal extends Goal{
    private final List<ItemBox> resourceList;

    public ResourceGoal(int goalID,List<ItemBox> resourceList, int points) {
        super(goalID,points);
        this.resourceList=resourceList;
    }

    public List<ItemBox> getResourceList() {
        return resourceList;
    }
}

