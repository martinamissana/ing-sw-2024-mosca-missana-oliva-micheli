package model.player;

import model.card.*;
import model.commonItem.ItemBox;

import java.util.HashMap;

public class Field {
    private final Player player;
    private final HashMap<Coords, Card> matrix;
    private final HashMap<ItemBox, Integer> totalResources;
    private final BlockedCard blockedCard;
    public Player getPlayer() { return player; }
    public HashMap<Coords, Card> getMatrix() { return matrix; }
    public HashMap<ItemBox, Integer> getTotalResources() { return totalResources; }
    public Field(Player p, BlockedCard blockedCard) {
        this.player = p;
        this.matrix = new HashMap<Coords, Card>();
        this.totalResources = new HashMap<ItemBox, Integer>();
        this.blockedCard = blockedCard;
    }

    // adds a starter card at the origin, with no checks whatsoever.
    // should only be called once per player, per game, at the start (might need to take precautions though)
    public void addCard(StarterCard card) {
        Coords coords = new Coords(0, 0);
        matrix.put(coords, card);
        blockCardSpaces(card, coords);
        updateTotalRes(card, coords);
    }

    // adds a card at a given position
    public boolean addCard(ResourceCard card, Coords coords) {
        if (!checkIfPlaceable(coords))
            return false;
        matrix.put(coords, card);
        blockCardSpaces(card, coords);
        updateTotalRes(card, coords);
        int addPts = evaluatePoints(card);
        if (addPts > 0) {
            //int oldScore = player.get
            // add points to scoreboard (there might currently be no way to access a player's score but maybe i'm wrong)
        }
        return true;
    }

    // could maybe get absorbed by the ResourceCard override method
    public boolean addCard(GoldenCard card, Coords coords) {
        if (!checkIfPlaceable(coords) ||
                !checkRequirements(card)) return false;
        matrix.put(coords, card);
        blockCardSpaces(card, coords);
        updateTotalRes(card, coords);
        // a golden card only adds at most one special resource, but never any of the kingdom resources
        // might be an useful observation to optimize this, maybe using card.getClass()
        int addPts = evaluatePoints(card, coords);
        if (addPts > 0) {
            //int oldScore = player.get
            // add points to scoreboard
        }
        return true;
    }

    // should check if a position is already occupied by a card or blocked by a corner
    // currently always returns false, but remember to change the last return to true when implementation is finished
    private boolean checkIfPlaceable(Coords coords) {
        if (matrix.get(coords) != null) // there's already a card at that position (including a BlockedCard)
            return false;
        //if (matrix[row][col]) // check if position is reachable by the graph with any corner (use neighbours map)
        return false;
    }

    // should check if a golden card's requirements are met before placing it
    private boolean checkRequirements(GoldenCard card) {
        //HashMap<Kingdom, Integer> reqs = card.getRequirements();
        // reqs.forEach((kingdom, integer) -> integer < totalResources.get(kingdom));
        // compare and evaluate answer
        return false;
    }

    // should look at a card's blocking corners and adds blockedCard entries wherever needed (using addBlockedCard)
    private void blockCardSpaces(Card card, Coords coords) {
        // calls addBlockedCard
        return;
    }

    // used to prevent cards from being placed on a blocking corner
    private boolean addBlockedCard(Coords coords) {
        if (matrix.containsKey(coords)) // don't block if a card is already set there
            return false;
        matrix.put(coords, this.blockedCard); // there's a BlockedCard for each field, functioning like a custom null pointer
        return true;
    }

    // should update totalResources when placing a new card
    private void updateTotalRes(Card card, Coords coords) {
        //Integer qty;
        // add card's resources to total (remember to check if card is on FRONT or BACK)
        // subtract covered corners' resources from total (always)
        return;
    }

    // should return a card to neighboring cards map
    private HashMap<CornerType, Card> getFreeNeighbours(Coords coords) {
        HashMap<CornerType, Card> map = new HashMap<>();
        // stuff
        return map;
    }
    private int evaluatePoints(ResourceCard card) { return card.getPoints(); } //returns direct points from res card

    // should golden card points given its position and GoldenCardType
    private int evaluatePoints(GoldenCard card, Coords coords) {
        // much stuff
        return 0;
    }
}