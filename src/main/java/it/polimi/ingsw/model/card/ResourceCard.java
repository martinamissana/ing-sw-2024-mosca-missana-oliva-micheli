package it.polimi.ingsw.model.card;

import it.polimi.ingsw.model.commonItem.Kingdom;

import java.util.HashMap;

public class ResourceCard extends Card  {
    private int points;
    private Kingdom kingdom;

    public ResourceCard(int cardID, CardSide side, HashMap<CornerType, Corner> frontCorner, HashMap<CornerType, Corner> backCorner) {
        super(cardID, side, frontCorner, backCorner);
    }


    public int getPoints() {
        return this.points;
    }

    public Kingdom getKingdom() {
        return this.kingdom;
    }
}
