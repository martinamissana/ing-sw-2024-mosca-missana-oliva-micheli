package it.polimi.ingsw.model.goal;

import it.polimi.ingsw.model.commonItem.ItemBox;

import java.util.ArrayList;

/**
 * Class ResourceGoal<br>
 * Extends Goal, it contains the list of the resources needed to complete the goal
 */
public class ResourceGoal extends Goal {
    private final ArrayList<ItemBox> resourceList;

    /**
     * Class constructor
     * @param goalID       the goal's ID
     * @param resourceList list that contains the resources needed to complete the goal
     * @param points       points to be added to the player's score when they complete the goal
     */
    public ResourceGoal(int goalID, ArrayList<ItemBox> resourceList, int points) {
        super(goalID, points);
        this.resourceList = resourceList;
    }

    /**
     * Gets the resource list of the goal
     * @return list of ItemBox
     */
    public ArrayList<ItemBox> getResourceList() { return resourceList; }
}

