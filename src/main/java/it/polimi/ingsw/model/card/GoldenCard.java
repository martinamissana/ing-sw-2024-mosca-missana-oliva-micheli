package it.polimi.ingsw.model.card;

import it.polimi.ingsw.model.commonItem.Kingdom;
import it.polimi.ingsw.model.commonItem.Resource;

import java.util.HashMap;

public class GoldenCard extends ResourceCard{
    private HashMap<Kingdom,Integer> requirements;
    private GoldenCardType type;
    private Resource pointResource; //is null if type!=RESOURCE

    public GoldenCard(int cardID, CardSide side, HashMap<CornerType, Corner> frontCorner, HashMap<CornerType, Corner> backCorner) {
        super(cardID, side, frontCorner, backCorner);
    }


    public HashMap<Kingdom, Integer> getRequirements() {
        return this.requirements;
    }

    public GoldenCardType getType() {
        return this.type;
    }

    public Resource getPointResource() {
        return this.pointResource;
    }

}
