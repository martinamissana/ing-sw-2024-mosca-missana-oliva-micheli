package it.polimi.ingsw.model.player;

import it.polimi.ingsw.model.card.*;
import it.polimi.ingsw.model.commonItem.ItemBox;
import it.polimi.ingsw.model.commonItem.Kingdom;
import java.util.HashMap;
import java.util.Map;

public class Field {
    private final HashMap<Coords, Card> matrix;
    private final HashMap<ItemBox, Integer> totalResources;
    private final BlockedCard blockedCard;
    public HashMap<Coords, Card> getMatrix() { return matrix; }
    public HashMap<ItemBox, Integer> getTotalResources() { return totalResources; }
    public Field() {
        this.matrix = new HashMap<Coords, Card>();
        this.totalResources = new HashMap<ItemBox, Integer>();
        this.blockedCard = new BlockedCard();
    }

    // adds a starter card at the origin, with no checks whatsoever.
    // should only be called once per player, per game, at the start (might need to take precautions though)
    public void addCard(StarterCard card) {
        Coords coords = new Coords(0, 0);
        this.matrix.put(coords, card);
        blockCardSpaces(card, coords);
        updateTotalRes(card);
    }

    // adds a card at a given position
    public boolean addCard(ResourceCard card, Coords coords) {
        if (!checkIfPlaceable(coords))
            return false;
        this.matrix.put(coords, card);
        blockCardSpaces(card, coords);
        updateTotalRes(card, coords);
        int addPts = evaluatePoints(card);

        return true;
    }

    // could maybe get absorbed by the ResourceCard override method
    public boolean addCard(GoldenCard card, Coords coords) {
        if (!checkIfPlaceable(coords) ||
                !checkRequirements(card)) return false;
        this.matrix.put(coords, card);
        blockCardSpaces(card, coords);
        updateTotalRes(card, coords);
        // a golden card only adds at most one special resource, but never any of the kingdom resources
        // might be a useful observation to optimize this, maybe using card.getClass()
        int addPts = evaluatePoints(card, coords);
        return true;
    }

    // checks if a position is already occupied by a card or blocked by a corner
    public boolean checkIfPlaceable(Coords coords) {

        // if there's already a card at that position (including a BlockedCard), return false
        if (this.matrix.get(coords) != null)
            return false;
        // if it's a reachable position (has to overlap at least one of its corners with another card's), return true
        HashMap<CornerType, Coords> neighbours = getNeighbours(coords);
        for (Map.Entry<CornerType, Coords> entry : neighbours.entrySet())
            if (this.matrix.get(entry.getValue()).getCorner(opposite(entry.getKey())) != null)
                return true;
        return false;
    }

    // checks if a golden card's requirements are currently met
    public boolean checkRequirements(GoldenCard card) {

        // if any resource required is insufficient, return false
        for (Map.Entry<Kingdom, Integer> entry : card.getRequirements().entrySet())
            if (entry.getValue() > this.totalResources.get(entry.getKey()))
                return false;
        // else, return true
        return true;
    }

    // looks at a card's blocking corners and adds blockedCard entries if there already isn't ANY Card
    private void blockCardSpaces(Card card, Coords coords) {

        // if a free cell is covered by a blocking corner, put a BlockedCard in that cell
        for (Map.Entry<CornerType, Coords> entry : getFreeNeighboursCoords(coords).entrySet())
            if (card.getCorner(entry.getKey()) == null)
                this.matrix.put(entry.getValue(), this.blockedCard);
    }

    // updates totalResources when placing the starter card
    private void updateTotalRes(StarterCard card) {
        if (card.getSide() == CardSide.FRONT) // one resource per kingdom
            for (Kingdom resource : Kingdom.values())
                this.totalResources.put(resource, 1);
        else {
            // add center resource(s)
            this.totalResources.putAll(card.getPermanentRes());

            // two of the starter cards have resources on their corners
            for (CornerType corner : CornerType.values()) {
                ItemBox resource = card.getCorner(corner);
                Integer oldQty = 0;
                if (resource != null) {
                    oldQty = this.totalResources.get(resource);
                    this.totalResources.put(resource, oldQty + 1);
                }
            }
        }
    }

    // updates totalResources when placing a new card
    private void updateTotalRes(ResourceCard card, Coords coords) {
        int oldQty;
        ItemBox item;

        if (card.getSide() == CardSide.FRONT) {
            // for each corner, get its resource and add 1 to its total count
            for (CornerType corner : CornerType.values()) {
                item = card.getCorner(corner);
                if (item != null) {
                    oldQty = this.totalResources.get(item);
                    this.totalResources.put(item, oldQty+1);
                }
            }
        } else {
            // add 1 to the total count of the card's Kingdom resource
            item = card.getKingdom();
            oldQty = this.totalResources.get(item);
            this.totalResources.put(item, oldQty + 1);
        }

        // subtracts 1 to a resource every time a corner that contains it gets covered
        for (Map.Entry<CornerType, Coords> entry : getNeighbours(coords).entrySet()) {
            item = this.matrix.get(entry.getValue()).getCorner(opposite(entry.getKey()));
            oldQty = this.totalResources.get(item);
            this.totalResources.put(item, oldQty - 1);
        }
    }

    // returns direct points from resource cards
    private int evaluatePoints(ResourceCard card) { return card.getPoints(); }

    // evaluates golden card points given its position and GoldenCardType
    private int evaluatePoints(GoldenCard card, Coords coords) {

        // read direct points from card
        if (card.getType() == GoldenCardType.DIRECT)
            return card.getPoints();

        // read total count of the corresponding resource
        if (card.getType() == GoldenCardType.RESOURCE)
            return totalResources.get(card.getPointResource());

        // get two points for each neighboring card
        if (card.getType() == GoldenCardType.CORNER)
            return getNeighbours(coords).size() * 2;
        return 0;
    }

    // returns a Corner (meaning direction) to Coords map, containing all coordinates with a Card in them (BlockedCard are NOT included)
    private HashMap<CornerType, Coords> getNeighbours(Coords coords) {
        HashMap<CornerType, Coords> map = new HashMap<>();
        if (coords == null) return map;

        int Xin = coords.getX();
        int Yin = coords.getY();
        Coords northCoords = new Coords(Xin,Yin+1);
        Coords eastCoords = new Coords(Xin+1,Yin);
        Coords southCoords = new Coords(Xin,Yin-1);
        Coords westCoords = new Coords(Xin-1,Yin);

        // is checking != null needed?
        if (this.matrix.get(northCoords) != null && this.matrix.get(northCoords).getClass() != BlockedCard.class)
            map.put(CornerType.NORTH, northCoords);
        if (this.matrix.get(eastCoords) != null && this.matrix.get(eastCoords).getClass() != BlockedCard.class)
            map.put(CornerType.EAST, eastCoords);
        if (this.matrix.get(southCoords) != null && this.matrix.get(southCoords).getClass() != BlockedCard.class)
            map.put(CornerType.SOUTH, southCoords);
        if (this.matrix.get(westCoords) != null && this.matrix.get(westCoords).getClass() != BlockedCard.class)
            map.put(CornerType.WEST, westCoords);
        return map;
    }

    // returns a Corner (meaning direction) to Coords map, which only includes coordinates without ANY Card in them (BlockedCard excluded too)
    private HashMap<CornerType, Coords> getFreeNeighboursCoords(Coords coords) {
        HashMap<CornerType, Coords> map = new HashMap<>();
        if (coords == null) return map;

        int Xin = coords.getX();
        int Yin = coords.getY();
        Coords northCoords = new Coords(Xin,Yin+1);
        Coords eastCoords = new Coords(Xin+1,Yin);
        Coords southCoords = new Coords(Xin,Yin-1);
        Coords westCoords = new Coords(Xin-1,Yin);

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

    // returns the opposite corner
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