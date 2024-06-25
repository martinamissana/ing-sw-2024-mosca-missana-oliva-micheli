package it.polimi.ingsw.model.card;

import it.polimi.ingsw.model.commonItem.Kingdom;
import it.polimi.ingsw.model.commonItem.Resource;

import java.util.HashMap;

/**
 * Class GoldenCard<br>
 * extends ResourceCard, adding attributes to support requirements for card placement and point assignment
 */
public class GoldenCard extends ResourceCard {
    private final HashMap<Kingdom, Integer> requirements;
    private final GoldenCardType type;
    private final Resource resourceToCount; // null if type!=RESOURCE

    /**
     * Class constructor
     * @param ID              the card's ID
     * @param side            indicates which side the card is currently on
     * @param frontCorners    list of corners on the front side
     * @param backCorners     list of corners on the back side
     * @param directPoints    directPoints to be added to the player's score when playing the card to the field
     * @param kingdom         the card's kingdom
     * @param requirements    map that contains the minimum number of resources required to play the card
     * @param type            the card's {@code GoldenCardType}
     * @param resourceToCount the resource counted to assign directPoints
     */
    public GoldenCard(int ID, CardSide side, HashMap<CornerType, Corner> frontCorners, HashMap<CornerType, Corner> backCorners, int directPoints, Kingdom kingdom, HashMap<Kingdom, Integer> requirements, GoldenCardType type, Resource resourceToCount) {
        super(ID, side, frontCorners, backCorners, directPoints, kingdom);
        this.requirements = requirements;
        this.type = type;
        this.resourceToCount = resourceToCount;
    }

    /**
     * @return map containing the minimum quantity of each resource required to play the card
     */
    public HashMap<Kingdom, Integer> getRequirements() {
        return this.requirements;
    }

    /**
     * @return the card's {@code GoldenCardType}, which indicates how points should be calculated when placing the card
     */
    public GoldenCardType getType() {
        return this.type;
    }

    /**
     * used if the card's type is {@code RESOURCE}, and is otherwise set to {@code null}
     * @return the type of resource of which to count the quantity for point assignment
     */
    public Resource getResourceToCount() {
        return this.resourceToCount;
    }
}