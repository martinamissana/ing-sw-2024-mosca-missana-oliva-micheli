package it.polimi.ingsw.model.card;

import it.polimi.ingsw.model.commonItem.Kingdom;

import java.util.HashMap;

public class StarterCard extends Card{
    private HashMap<Kingdom, Integer> permanentRes;

    public StarterCard(int cardID, CardSide side, HashMap<CornerType, Corner> frontCorner, HashMap<CornerType, Corner> backCorner, HashMap<Kingdom, Integer> permanentRes) {
        super(cardID, side, frontCorner, backCorner);
        this.permanentRes = permanentRes;
    }

    public HashMap<Kingdom, Integer> getPermanentRes() {
        return this.permanentRes;
    }
}
