package it.polimi.ingsw.model.card;

import it.polimi.ingsw.model.commonItem.Kingdom;

import java.util.HashMap;

/**
 * Class ResourceCard
 * extends Card, adding the attributes for points and kingdom
 */
public class ResourceCard extends Card  {
    private int points;
    private Kingdom kingdom;

    /**
     * Class constructor
     * @param cardID
     * @param side
     * @param frontCorner
     * @param backCorner
     * @param points
     * @param kingdom
     */
    public ResourceCard(int cardID, CardSide side, HashMap<CornerType, Corner> frontCorner, HashMap<CornerType, Corner> backCorner, int points, Kingdom kingdom) {
        super(cardID, side, frontCorner, backCorner);
        this.points = points;
        this.kingdom = kingdom;
    }

    /**
     * gets the points
     * @return card points
     */
    public int getPoints() {
        return this.points;
    }

    /**
     * gets the kingdom
     * @return card kingdom
     */
    public Kingdom getKingdom() {
        return this.kingdom;
    }
}
