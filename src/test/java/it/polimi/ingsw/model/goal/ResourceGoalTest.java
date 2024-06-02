package it.polimi.ingsw.model.goal;

import it.polimi.ingsw.model.commonItem.ItemBox;
import it.polimi.ingsw.model.commonItem.Kingdom;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ResourceGoalTest {

    public void testGetResourceList() {
        ArrayList<ItemBox> resourceList =new ArrayList<>();
        ItemBox e= Kingdom.ANIMAL;
        resourceList.add(e);
        ResourceGoal resourceGoal= new ResourceGoal(0,resourceList,0);
        assertEquals(resourceList,resourceGoal.getResourceList());
    }
}