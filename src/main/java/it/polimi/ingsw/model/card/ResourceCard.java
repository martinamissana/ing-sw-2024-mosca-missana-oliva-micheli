package it.polimi.ingsw.model.card;

import it.polimi.ingsw.model.commonItem.Kingdom;

import java.util.HashMap;

public class ResourceCard extends Card  {
    private int points;
    private Kingdom kingdom;

    public ResourceCard(int cardID, CardSide side, HashMap<CornerType, Corner> frontCorner, HashMap<CornerType, Corner> backCorner, int points, Kingdom kingdom) {
        super(cardID, side, frontCorner, backCorner);
        this.points = points;
        this.kingdom = kingdom;
    }

    public int getPoints() {
        return this.points;
    }

    public Kingdom getKingdom() {
        return this.kingdom;
    }
}
