package it.polimi.ingsw.model.card;

import it.polimi.ingsw.model.commonItem.Kingdom;

import java.util.HashMap;

/**
 * Class StarterCard
 * extends Card, adding the attribute for permanent resources
 */
public class StarterCard extends Card{
    private HashMap<Kingdom, Integer> permanentRes;

    /**
     * Class constructor
     * @param cardID
     * @param side
     * @param frontCorner
     * @param backCorner
     * @param permanentRes
     */
    public StarterCard(int cardID, CardSide side, HashMap<CornerType, Corner> frontCorner, HashMap<CornerType, Corner> backCorner, HashMap<Kingdom, Integer> permanentRes) {
        super(cardID, side, frontCorner, backCorner);
        this.permanentRes = permanentRes;
    }

    /**
     * gets the permanent resources
     * @return the card permanentRes
     */
    public HashMap<Kingdom, Integer> getPermanentRes() {
        return this.permanentRes;
    }
}
