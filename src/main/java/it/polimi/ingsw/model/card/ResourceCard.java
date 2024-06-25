package it.polimi.ingsw.model.card;

import it.polimi.ingsw.model.commonItem.Kingdom;

import java.util.HashMap;

/**
 * Class ResourceCard
 * extends Card, adding attributes for direct points and kingdom
 */
public class ResourceCard extends Card {
    private final int directPoints;
    private final Kingdom kingdom;

    /**
     * Class constructor
     * @param ID           the card's ID
     * @param side         indicates which side the card is currently on
     * @param frontCorners list of corners on the front side
     * @param backCorners  list of corners on the back side
     * @param directPoints points to be added to the player's score when playing the card to the field
     * @param kingdom      the card's kingdom
     */
    public ResourceCard(int ID, CardSide side, HashMap<CornerType, Corner> frontCorners, HashMap<CornerType, Corner> backCorners, int directPoints, Kingdom kingdom) {
        super(ID, side, frontCorners, backCorners);
        this.directPoints = directPoints;
        this.kingdom = kingdom;
    }

    /**
     * @return int
     */
    public int getDirectPoints() { return this.directPoints; }

    /**
     * @return Kingdom
     */
    public Kingdom getKingdom() { return this.kingdom; }
}
