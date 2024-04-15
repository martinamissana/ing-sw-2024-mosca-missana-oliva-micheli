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
 * Field Class
 * each player has an associated field to play their cards onto
 */
public class Field implements Serializable {
    private final HashMap<Coords, Card> matrix;
    private final HashMap<ItemBox, Integer> totalResources;
    private final CardBlock cardBlock;

    /**
     * Class constructor
     */
    public Field() {
        this.matrix = new HashMap<Coords, Card>();

        HashMap<ItemBox, Integer> temp = new HashMap<ItemBox, Integer>();

        for(Kingdom kingdom : Kingdom.values())
            temp.put(kingdom, 0);
        for(Resource resource : Resource.values())
            temp.put(resource, 0);

        this.totalResources = temp;
        this.cardBlock = new CardBlock();
    }

    /**
     * gets the set of currently placed cards
     * @return matrix
     */
    public HashMap<Coords, Card> getMatrix() { return matrix; }

    /**
     * gets each resource's total count
     * @return totalResources
     */
    public HashMap<ItemBox, Integer> getTotalResources() { return totalResources; }

    /**
     * returns the field's CardBlock. should only be used for testing purposes
     * @return CardBlock
     */
    public CardBlock getCardBlock() { return cardBlock; }

    /**
     * adds a starter card at the origin of the field (0,0) with no checks whatsoever
     * @param card
     */
    public void addCard(StarterCard card) {
        Coords coords = new Coords(0, 0);
        this.matrix.put(coords, card);
        blockCardSpaces(card, coords);
        updateTotalRes(card);
    }

    /**
     * adds a card (Resource or Golden) to the field at the specified position.
     * returns the number of points gained by the player after placing said card.
     * @param card
     * @param coords
     * @return int
     * @throws IllegalMoveException
     */
    public int addCard(ResourceCard card, Coords coords) throws IllegalMoveException {
        if (!checkIfPlaceable(coords)) throw new IllegalCoordsException();
        if (!checkRequirements(card)) throw new RequirementsNotSatisfiedException();
        this.matrix.put(coords, card);
        blockCardSpaces(card, coords);
        updateTotalRes(card, coords);
        return evaluatePoints(card, coords);
    }

    /**
     * checks if a position is already occupied by a card, blocked by a corner or unreachable
     * @param coords
     * @return boolean
     * @throws IllegalCoordsException
     */
    public boolean checkIfPlaceable(Coords coords) throws IllegalCoordsException {

        // if there's already a card at that position (including a CardBlock,
        // meaning the position would be blocked by a corner), throw an exception
        if (this.matrix.get(coords) != null) throw new OccupiedCoordsException();

        // if any of the adjacent positions has a card with a non-blocking corner
        // pointed towards the specified position, return true
        for (Map.Entry<CornerType, Coords> entry : getNeighbours(coords).entrySet())            // iterate through neighboring cards
            if (this.matrix.get(entry.getValue()).getCorner(opposite(entry.getKey())) != null)  // if any such card... (as before)
                return true;                                                                    // a card can be placed at 'coords'

        // unreachable position, throw exception
        throw new UnreachablePositionException();
    }

    /**
     * checks if a card's requirements are met (any non-Golden card will always have its requirements met)
     * @param card
     * @return boolean
     * @throws RequirementsNotSatisfiedException
     */
    public boolean checkRequirements(ResourceCard card) throws RequirementsNotSatisfiedException {

        // if it's not a Golden card it doesn't have requirements, so they're automatically met
        if (card.getClass() != GoldenCard.class)
            return true;

        // if any resource required is insufficient, throw exception
        for (Map.Entry<Kingdom, Integer> entry : ((GoldenCard) card).getRequirements().entrySet())  // for each required resource's quantity
            if (entry.getValue() > this.totalResources.get(entry.getKey()))                         // if it's greater than the corresponding total
                throw new RequirementsNotSatisfiedException();                                      // the card's requirements are not met

        // else, requirements are met. return true
        return true;
    }

    /**
     * looks at a card's blocking corners and sets cardBlock entries if there already isn't any Card
     * @param card
     * @param coords
     */
    private void blockCardSpaces(Card card, Coords coords) {

        // if a free adjacent position is covered by a blocking corner of
        // the specified card, set a CardBlock at that position
        for (Map.Entry<CornerType, Coords> entry : getFreeNeighboursCoords(coords).entrySet())  // iterate through free neighboring positions.
            if (card.getCorner(entry.getKey()) == null)                                         // if no corner is instantiated for that direction,
                this.matrix.put(entry.getValue(), this.cardBlock);                              // then set a CardBlock
    }

    /**
     * updates each resource's total count, given the newly placed Starter card
     * @param card
     */
    private void updateTotalRes(StarterCard card) {

            // starter card is face up
        if (card.getSide() == CardSide.FRONT)           // starter cards always have
            for (Kingdom resource : Kingdom.values())   // one resource per kingdom
                this.totalResources.put(resource, 1);   // on their front side
        else {
            // starter card is flipped
            this.totalResources.putAll(card.getPermanentRes());             // get center resources first

            // two of the starter cards have resources on their back corners
            for (CornerType corner : CornerType.values()) {                 // then check all corners
                ItemBox resource = card.getCorner(corner);
                Integer oldQty = 0;
                if (resource != null && resource != CornerStatus.EMPTY) {   // if there's a resource,
                    oldQty = this.totalResources.get(resource);             // increment its count
                    this.totalResources.put(resource, oldQty+1);            // by 1
                }
            }
        }
    }

    /**
     * updates each resource's total count, given the newly placed card and its position
     * @param card
     * @param coords
     */
    private void updateTotalRes(ResourceCard card, Coords coords) {
        int oldQty;
        ItemBox item;

            // card is face up
        if (card.getSide() == CardSide.FRONT) {
            for (CornerType corner : CornerType.values()) {         // check each corners'
                item = card.getCorner(corner);                      // resources.
                if (item != null && item != CornerStatus.EMPTY) {   // if there's any,
                    oldQty = this.totalResources.get(item);         // increment the corresponding
                    this.totalResources.put(item, oldQty+1);        // total count
                }
            }
        } else {
            // card is face down
            item = card.getKingdom();                   // get the card's Kingdom resource
            oldQty = this.totalResources.get(item);     // add 1 to its
            this.totalResources.put(item, oldQty+1);    // total count
        }

        // subtract 1 to a resource's count every time an adjacent card's corner that contains it gets covered
        for (Map.Entry<CornerType, Coords> entry : getNeighbours(coords).entrySet()) {      // iterate through neighboring cards
            item = this.matrix.get(entry.getValue()).getCorner(opposite(entry.getKey()));   // get covered corner's resource
            if (item != null) {                                                             // if there is
                oldQty = this.totalResources.get(item);                                     // decrement its
                this.totalResources.put(item, oldQty-1);                                    // total count
            }
        }
    }

    /**
     * returns the number of points from Resource cards
     * @param card
     * @return int
     */
    private int evaluatePoints(ResourceCard card) { return card.getPoints(); }

    /**
     * evaluates and returns the number of points (given the card's position and GoldenCardType if it's a GoldenCard)
     * @param card
     * @param coords
     * @return int
     */
    private int evaluatePoints(ResourceCard card, Coords coords) {

        // read direct points from ResourceCard
        if (card.getClass() == ResourceCard.class)
            return card.getPoints();

        GoldenCard goldenCard = (GoldenCard) card;

        // read direct points from GoldenCard
        if (goldenCard.getType() == GoldenCardType.DIRECT)
            return goldenCard.getPoints();

        // one point for each unit of specified resource
        if (goldenCard.getType() == GoldenCardType.RESOURCE)
            return totalResources.get(goldenCard.getPointResource()); // read total count of the corresponding resource

        // two points for each neighboring card
        if (goldenCard.getType() == GoldenCardType.CORNER)
            return getNeighbours(coords).size() * 2; // get the number of adjacent cards and multiply by 2
        return 0;
    }

    /**
     * returns all cards adjacent to the specified position.
     * more formally, it returns a CornerType (meaning direction) to Coords map,
     * containing all coordinates with a Card in them (CardBlock cards are NOT included)
     * @param coords
     * @return HashMap<CornerType, Coords>
     */
    private HashMap<CornerType, Coords> getNeighbours(Coords coords) {
        HashMap<CornerType, Coords> map = new HashMap<>();
        if (coords == null) return map;
        
        // create neighboring coordinates
        int Xin = coords.getX();
        int Yin = coords.getY();
        Coords northCoords = new Coords(Xin,Yin+1);
        Coords eastCoords = new Coords(Xin+1,Yin);
        Coords southCoords = new Coords(Xin,Yin-1);
        Coords westCoords = new Coords(Xin-1,Yin);

        // if there's a non-blocking Card, add that position to the map (is checking != null needed?)
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
     * returns all non-blocked and empty locations near a specified position.
     * more formally, it returns a CornerType (meaning direction) to Coords map,
     * containing all coordinates without ANY Card in them (CardBlock cards are excluded too)
     * @param coords
     * @return HashMap<CornerType, Coords>
     */
    private HashMap<CornerType, Coords> getFreeNeighboursCoords(Coords coords) {
        HashMap<CornerType, Coords> map = new HashMap<>();
        if (coords == null) return map;

        // create neighboring coordinates
        int Xin = coords.getX();
        int Yin = coords.getY();
        Coords northCoords = new Coords(Xin,Yin+1);
        Coords eastCoords = new Coords(Xin+1,Yin);
        Coords southCoords = new Coords(Xin,Yin-1);
        Coords westCoords = new Coords(Xin-1,Yin);

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
     * returns the CornerType opposite to the one specified
     * @param corner
     * @return CornerType
     */
    private CornerType opposite(CornerType corner) {
        if (corner != null) {
            if (corner == CornerType.NORTH)
                return CornerType.SOUTH;
            if (corner == CornerType.EAST)
                return CornerType.WEST;
            if (corner == CornerType.SOUTH)
                return CornerType.NORTH;
            if (corner == CornerType.WEST)
                return CornerType.EAST;
        }
        return null;
    }
}