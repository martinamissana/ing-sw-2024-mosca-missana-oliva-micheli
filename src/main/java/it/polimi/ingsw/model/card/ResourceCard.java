package it.polimi.ingsw.model.card;

import it.polimi.ingsw.model.commonItem.Kingdom;

import java.util.HashMap;

/**
 * Class ResourceCard
 * extends Card, adding the attributes for points and kingdom
 */
public class ResourceCard extends Card {
    private final int points;
    private final Kingdom kingdom;

    /**
     * Class constructor
     *
     * @param cardID      the card ID
     * @param side        the side of the card that is visible
     * @param frontCorner the list of corners in the front
     * @param backCorner  the list of corners in the back
     * @param points      the points associated with the card
     * @param kingdom     the kingdom associated with the card
     */
    public ResourceCard(int cardID, CardSide side, HashMap<CornerType, Corner> frontCorner, HashMap<CornerType, Corner> backCorner, int points, Kingdom kingdom) {
        super(cardID, side, frontCorner, backCorner);
        this.points = points;
        this.kingdom = kingdom;
    }

    /**
     * gets the points
     *
     * @return card points
     */
    public int getPoints() {
        return this.points;
    }

    /**
     * gets the kingdom
     *
     * @return card kingdom
     */
    public Kingdom getKingdom() {
        return this.kingdom;
    }
}
