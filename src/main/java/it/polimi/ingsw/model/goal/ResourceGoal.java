package it.polimi.ingsw.model.goal;

import it.polimi.ingsw.model.commonItem.ItemBox;

import java.util.ArrayList;

/**
 * class ResourceGoal
 * extends Goal, it contains the list of the resources needed to complete the goal
 */
public class ResourceGoal extends Goal {
    private final ArrayList<ItemBox> resourceList;

    /**
     * constructor
     *
     * @param goalID       identifies univocally the goal
     * @param resourceList list that contains the resources needed to complete the goal
     * @param points       points given from the goal
     */
    public ResourceGoal(int goalID, ArrayList<ItemBox> resourceList, int points) {
        super(goalID, points);
        this.resourceList = resourceList;
    }

    /**
     * getter for the resource list of the goal
     *
     * @return list<ItemBox>
     */
    public ArrayList<ItemBox> getResourceList() {
        return resourceList;
    }
}

