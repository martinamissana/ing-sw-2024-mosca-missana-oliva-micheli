package it.polimi.ingsw.model.player;

import it.polimi.ingsw.model.card.*;
import it.polimi.ingsw.model.commonItem.CornerStatus;
import it.polimi.ingsw.model.commonItem.ItemBox;
import it.polimi.ingsw.model.commonItem.Kingdom;
import it.polimi.ingsw.model.commonItem.Resource;
import it.polimi.ingsw.model.exceptions.*;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * Field Class<br>
 * Each player has an associated {@code Field} to play their cards onto
 */
public class Field implements Serializable {
    private final HashMap<Coords, Card> matrix;
    private final HashMap<ItemBox, Integer> totalResources;
    private final CardBlock cardBlock;

    /**
     * Class constructor
     */
    public Field() {
        this.matrix = new HashMap<>();

        HashMap<ItemBox, Integer> temp = new HashMap<>();

        for (Kingdom kingdom : Kingdom.values())
            temp.put(kingdom, 0);
        for (Resource resource : Resource.values())
            temp.put(resource, 0);

        this.totalResources = temp;
        this.cardBlock = new CardBlock();
    }

    /**
     * Gets the set of currently placed cards
     * @return matrix
     */
    public HashMap<Coords, Card> getMatrix() { return matrix; }

    /**
     * Gets each resource's total count
     * @return totalResources
     */
    public HashMap<ItemBox, Integer> getTotalResources() { return totalResources; }

    /**
     * <strong>For testing purposes only.</strong><br>
     * Returns the field's CardBlock
     * @return CardBlock
     */
    public CardBlock getCardBlock() { return cardBlock; }

    /**
     * Adds a starter card at the origin of the {@code Field} (0,0)
     * @param card Starter card that is to be added
     */
    public void addCard(StarterCard card) {
        Coords coords = new Coords(0, 0);
        this.matrix.put(coords, card);
        blockCardSpaces(card, coords);
        updateTotalRes(card);
    }

    /**
     * Adds a card (either {@code RESOURCE} or {@code GOLDEN}) to the {@code Field} at the specified position.
     * @param card   {@code RESOURCE} or {@code GOLDEN} card that is to be added
     * @param coords position at which to place the card
     * @return the number of points gained by the player after placing said card.
     * @throws IllegalMoveException thrown if the card couldn't be added to the field
     */
    public int addCard(ResourceCard card, Coords coords) throws IllegalMoveException {
        if (!checkIfPlaceable(coords)) throw new IllegalCoordsException();
        if (!checkRequirements(card)) throw new RequirementsNotSatisfiedException();
        this.matrix.put(coords, card);
        updateCoverage(coords);
        blockCardSpaces(card, coords);
        updateTotalRes(card, coords);
        return evaluatePoints(card, coords);
    }

    /**
     * Checks if a position is already occupied by a card, blocked by a corner or unreachable
     * @param coords position to check
     * @return boolean
     * @throws IllegalCoordsException thrown if the position currently cannot contain a card
     */
    public boolean checkIfPlaceable(Coords coords) throws IllegalCoordsException {

        // if there's already a card at that position (including a CardBlock,
        // meaning the position would be blocked by a corner), throw an exception
        if (this.matrix.get(coords) != null) {
            throw new OccupiedCoordsException();
        }

        // if any of the adjacent positions has a card with a non-blocking corner
        // pointed towards the specified position, return true
        for (Map.Entry<CornerType, Coords> entry : getAdjacentCards(coords).entrySet())                 // iterate through neighboring cards
            if (this.matrix.get(entry.getValue()).getItemFromCorner(opposite(entry.getKey())) != null)  // if any such card... (as before)
                return true;                                                                            // a card can be placed at 'coords'

        // there's no corners adjacent to the specified position, making it unreachable
        throw new UnreachablePositionException();
    }

    /**
     * Checks if a card's requirements are met (any non-Golden card will always have its requirements met)
     * @param card card to check the requirements of
     * @return boolean
     * @throws RequirementsNotSatisfiedException thrown if the card doesn't have its requirements met
     */
    public boolean checkRequirements(ResourceCard card) throws RequirementsNotSatisfiedException {

        // if it's not a Golden card or if it's flipped, it doesn't have requirements, so they're automatically met
        if (card.getClass() != GoldenCard.class || card.getSide() == CardSide.BACK)
            return true;

        // if any resource required is insufficient, throw exception
        for (Map.Entry<Kingdom, Integer> entry : ((GoldenCard) card).getRequirements().entrySet())  // for each required resource's quantity
            if (entry.getValue() > this.totalResources.get(entry.getKey()))                         // if it's greater than the corresponding total
                throw new RequirementsNotSatisfiedException();                                      // requirements are not met

        // else, requirements are met. return true
        return true;
    }

    /**
     * Looks at a card's blocking corners and sets cardBlock entries if there already isn't any Card
     * @param card   newly placed card
     * @param coords position of the card
     */
    private void blockCardSpaces(Card card, Coords coords) {

        // if a free adjacent position is covered by a blocking corner of
        // the specified card, set a CardBlock at that position
        for (Map.Entry<CornerType, Coords> entry : getFreeAdjacentCoords(coords).entrySet()) // iterate through free neighboring positions.
            if (card.getItemFromCorner(entry.getKey()) == null)                              // if no corner is instantiated for that direction,
                this.matrix.put(entry.getValue(), this.cardBlock);                           // then set a CardBlock
    }

    /**
     * Covers all corners adjacent to a position
     */
    private void updateCoverage(Coords coords) {
        HashMap<CornerType, Coords> adjacentCoords = getAdjacentCards(coords);
        for (CornerType corner : adjacentCoords.keySet())
            this.matrix.get(adjacentCoords.get(corner)).getCorner(opposite(corner)).cover();
    }

    /**
     * Updates each resource's total count, given the newly placed {@code StarterCard}
     * @param card newly placed {@code StarterCard}
     */
    private void updateTotalRes(StarterCard card) {

        // starter card is face up
        if (card.getSide() == CardSide.FRONT)           // starter cards always have
            for (Kingdom resource : Kingdom.values())   // one resource per kingdom
                this.totalResources.put(resource, 1);   // on their front side
        else {
            // starter card is flipped
            this.totalResources.putAll(card.getPermanentRes());             // get center resources first

            // this is for the two Starter cards
            // that have resources on their back corners
            for (CornerType corner : CornerType.values()) {                 // then check all corners
                ItemBox resource = card.getItemFromCorner(corner);
                if (resource != null && resource != CornerStatus.EMPTY) {   // if there's a resource,
                    Integer oldQty = this.totalResources.get(resource);
                    this.totalResources.replace(resource, oldQty + 1);        // increment its count by 1
                }
            }
        }
    }

    /**
     * Updates each resource's total count, given the newly placed
     * {@code ResourceCard} or {@code GoldenCard} and its position
     * @param card   newly placed {@code ResourceCard} or {@code GoldenCard}
     * @param coords position of the card
     */
    private void updateTotalRes(ResourceCard card, Coords coords) {
        int oldQty;
        ItemBox item;

        // card is face up
        if (card.getSide() == CardSide.FRONT) {
            for (CornerType corner : CornerType.values()) {         // check each corners'
                item = card.getItemFromCorner(corner);                      // resources.
                if (item != null && item != CornerStatus.EMPTY) {   // if there's any,
                    oldQty = this.totalResources.get(item);         // increment the corresponding
                    this.totalResources.replace(item, oldQty + 1);    // total count
                }
            }
        } else {
            // card is face down
            item = card.getKingdom();                       // get the card's Kingdom resource
            oldQty = this.totalResources.get(item);         // add 1 to its
            this.totalResources.replace(item, oldQty + 1);  // total count
        }

        // subtract 1 to a resource's count every time an adjacent card's corner that contains it gets covered
        for (Map.Entry<CornerType, Coords> entry : getAdjacentCards(coords).entrySet()) {           // iterate through neighboring cards
            item = this.matrix.get(entry.getValue()).getItemFromCorner(opposite(entry.getKey()));   // get covered corner's resource
            if (item != null && item != CornerStatus.EMPTY) {                                       // if there is
                oldQty = this.totalResources.get(item);
                this.totalResources.replace(item, oldQty - 1);                                      // decrement its total count
            }
        }
    }

    /**
     * Evaluates and returns the number of points given the card's position
     * (and {@code GoldenCardType} if it's a {@code GoldenCard})
     * @param card   card to evaluate the points of
     * @param coords position of the card
     * @return int
     */
    private int evaluatePoints(ResourceCard card, Coords coords) {

        // cards on their back don't give any points
        if (card.getSide().equals(CardSide.BACK))
            return 0;

        // read direct points from ResourceCard
        if (card.getClass() == ResourceCard.class)
            return card.getDirectPoints();

        GoldenCard goldenCard = (GoldenCard) card;

        // read direct points from GoldenCard
        if (goldenCard.getType() == GoldenCardType.DIRECT)
            return goldenCard.getDirectPoints();

        // one point for each unit of specified resource
        if (goldenCard.getType() == GoldenCardType.RESOURCE)
            return totalResources.get(goldenCard.getResourceToCount()); // read total count of the corresponding resource

        // two points for each neighboring card
        if (goldenCard.getType() == GoldenCardType.CORNER)
            return getAdjacentCards(coords).size() * 2; // get the number of adjacent cards and multiply by 2
        return 0;
    }

    /**
     * Returns all cards adjacent to the specified position.<br>
     * More formally, it returns a {@code CornerType} (meaning direction) to {@code Coords} map,
     * containing all coordinates with a {@code Card} in them ({@code CardBlock} cards are <strong>not</strong> included)
     * @param coords position of which to look for adjacent cards of
     * @return adjacent coordinates
     */
    private HashMap<CornerType, Coords> getAdjacentCards(Coords coords) {
        if (coords == null) throw new NullPointerException();

        HashMap<CornerType, Coords> map = new HashMap<>();
        // create adjacent coordinates
        int Xin = coords.getX();
        int Yin = coords.getY();
        Coords northCoords = new Coords(Xin, Yin + 1);
        Coords eastCoords = new Coords(Xin + 1, Yin);
        Coords southCoords = new Coords(Xin, Yin - 1);
        Coords westCoords = new Coords(Xin - 1, Yin);

        // if there's a card (not including CardBlock cards),
        // add that position to the map (is checking != null needed?)
        if (this.matrix.get(northCoords) != null && this.matrix.get(northCoords).getClass() != CardBlock.class)
            map.put(CornerType.NORTH, northCoords);
        if (this.matrix.get(eastCoords) != null && this.matrix.get(eastCoords).getClass() != CardBlock.class)
            map.put(CornerType.EAST, eastCoords);
        if (this.matrix.get(southCoords) != null && this.matrix.get(southCoords).getClass() != CardBlock.class)
            map.put(CornerType.SOUTH, southCoords);
        if (this.matrix.get(westCoords) != null && this.matrix.get(westCoords).getClass() != CardBlock.class)
            map.put(CornerType.WEST, westCoords);
        return map;
    }

    /**
     * Returns all non-blocked and empty coordinates adjacent to a specified one.
     * More formally, it returns a {@code CornerType} (meaning direction) to {@code Coords} map,
     * containing all coordinates without <strong>any</strong> {@code Card} in them (CardBlock cards are excluded too)
     *
     * @param coords position of which to look for free adjacent coordinates of
     * @return adjacent free coordinates
     */
    private HashMap<CornerType, Coords> getFreeAdjacentCoords(Coords coords) {
        if (coords == null) throw new NullPointerException();

        HashMap<CornerType, Coords> map = new HashMap<>();
        // create adjacent coordinates
        int Xin = coords.getX();
        int Yin = coords.getY();
        Coords northCoords = new Coords(Xin, Yin + 1);
        Coords eastCoords = new Coords(Xin + 1, Yin);
        Coords southCoords = new Coords(Xin, Yin - 1);
        Coords westCoords = new Coords(Xin - 1, Yin);

        // if there's no entry for that position, it is free. add it to the map
        if (!this.matrix.containsKey(northCoords))
            map.put(CornerType.NORTH, northCoords);
        if (!this.matrix.containsKey(eastCoords))
            map.put(CornerType.EAST, eastCoords);
        if (!this.matrix.containsKey(southCoords))
            map.put(CornerType.SOUTH, southCoords);
        if (!this.matrix.containsKey(westCoords))
            map.put(CornerType.WEST, westCoords);
        return map;
    }

    /**
     * Returns the {@code CornerType} opposite to the specified one
     * @param corner {@code CornerType} to invert
     * @return the opposite {@code CornerType}
     */
    private CornerType opposite(CornerType corner) {
        return switch (corner) {
            case NORTH -> CornerType.SOUTH;
            case EAST -> CornerType.WEST;
            case SOUTH -> CornerType.NORTH;
            case WEST -> CornerType.EAST;
            case null -> throw new NullPointerException();
        };
    }
}