package it.polimi.ingsw.model.card;

import it.polimi.ingsw.model.commonItem.Kingdom;
import it.polimi.ingsw.model.commonItem.Resource;

import java.util.HashMap;

/**
 * Class GoldenCard
 * extends ResourceCard, adding the attributes for requirements, type and pointResource
 */
public class GoldenCard extends ResourceCard {
    private final HashMap<Kingdom, Integer> requirements;
    private final GoldenCardType type;
    private final Resource pointResource; //which resource is to count to accumulate points
                                          //is null if type!=RESOURCE

    /**
     * Class constructor
     *
     * @param cardID        the card ID
     * @param side          the side of the card that is visible
     * @param frontCorner   the list of corners in the front
     * @param backCorner    the list of corners in the back
     * @param points        the points associated with the card
     * @param kingdom       the kingdom associated with the card
     * @param requirements  the requirements to play the card
     * @param type          the type of golden card
     * @param pointResource the resource counted to assign points
     */
    public GoldenCard(int cardID, CardSide side, HashMap<CornerType, Corner> frontCorner, HashMap<CornerType, Corner> backCorner, int points, Kingdom kingdom, HashMap<Kingdom, Integer> requirements, GoldenCardType type, Resource pointResource) {
        super(cardID, side, frontCorner, backCorner, points, kingdom);
        this.requirements = requirements;
        this.type = type;
        this.pointResource = pointResource;
    }

    /**
     * gets the requirements to place the card
     *
     * @return card requirements
     */
    public HashMap<Kingdom, Integer> getRequirements() {
        return this.requirements;
    }

    /**
     * gets the type of pattern to calculate the points
     *
     * @return card type
     */
    public GoldenCardType getType() {
        return this.type;
    }

    /**
     * gets which resource is to count to accumulate points if the type is RESOURCE,
     * otherwise is set to null
     *
     * @return card pointResource
     */
    public Resource getPointResource() {
        return this.pointResource;
    }

}
