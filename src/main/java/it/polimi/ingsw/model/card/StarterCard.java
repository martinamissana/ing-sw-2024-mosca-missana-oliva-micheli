package it.polimi.ingsw.model.card;

import it.polimi.ingsw.model.commonItem.Kingdom;

import java.util.HashMap;

/**
 * Class StarterCard<br>
 * extends Card, adding an attribute for the permanent resources on the back of starter cards
 */
public class StarterCard extends Card {
    private final HashMap<Kingdom, Integer> permanentRes;

    /**
     * Class constructor
     * @param ID           the card's ID
     * @param side         indicates which side the card is currently on
     * @param frontCorners list of corners on the front side
     * @param backCorners  list of corners on the back side
     * @param permanentRes list of resources at the center of the starter card's back
     *                     (they can't be covered by other corners, so they're permanent)
     */
    public StarterCard(int ID, CardSide side, HashMap<CornerType, Corner> frontCorners, HashMap<CornerType, Corner> backCorners, HashMap<Kingdom, Integer> permanentRes) {
        super(ID, side, frontCorners, backCorners);
        this.permanentRes = permanentRes;
    }

    /**
     * @return the card's permanent resources
     */
    public HashMap<Kingdom, Integer> getPermanentRes() {
        return this.permanentRes;
    }
}
