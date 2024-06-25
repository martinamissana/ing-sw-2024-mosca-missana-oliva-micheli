package it.polimi.ingsw.model.card;

import it.polimi.ingsw.model.commonItem.ItemBox;
import it.polimi.ingsw.model.commonItem.Kingdom;

import java.io.Serializable;
import java.util.HashMap;

/**
 * Abstract Class Card<br>
 * defines all basic attributes and methods common to all cards
 */
public abstract class Card implements Serializable {
    private final int ID;
    private CardSide side;
    private final HashMap<CornerType, Corner> frontCorners;
    private final HashMap<CornerType, Corner> backCorners;

    /**
     * Class constructor
     * @param ID           the card's ID
     * @param side         indicates which side the card is currently on
     * @param frontCorners list of corners on the front side
     * @param backCorners  list of corners on the back side
     */
    public Card(int ID, CardSide side, HashMap<CornerType, Corner> frontCorners, HashMap<CornerType, Corner> backCorners) {
        this.ID = ID;
        this.side = side;
        this.frontCorners = frontCorners;
        this.backCorners = backCorners;
    }

    /**
     * @return int
     */
    public int getID() {
        return this.ID;
    }

    /**
     * @return CardSide
     */
    public CardSide getSide() {
        return this.side;
    }

    /**
     * @return Kingdom
     */
    public Kingdom getKingdom() { return null; }

    /**
     * returns the corner of the specified type
     * on the side that the card is currently on, if there's one.<br>
     * returns {@code null} otherwise.
     * @param corner the corner type you want
     * @return Corner
     */
    public Corner getCorner(CornerType corner) {
        if (this.side == CardSide.FRONT) return this.frontCorners.get(corner);
        else return this.backCorners.get(corner);
    }

    /**
     * returns the item contained by the specified corner
     * on the side that the card is currently on, if there's one.<br>
     * returns {@code null} otherwise.
     * @param corner corner to return the item of
     * @return ItemBox
     */
    public ItemBox getItemFromCorner(CornerType corner) {
        if (side == CardSide.FRONT)
            return frontCorners.containsKey(corner) ? frontCorners.get(corner).getItem() : null;
        else
            return backCorners.containsKey(corner) ? backCorners.get(corner).getItem() : null;
    }


    /**
     * switches the side that the card is on
     */
    public void flip() {
        this.side = (this.side == CardSide.FRONT) ? CardSide.BACK : CardSide.FRONT;
    }

    /**
     * sets the card's side to the specified one
     * @param side
     */
    public void setSide(CardSide side) {
        this.side = side;
    }


    /**
     * two cards are defined to be equal if their IDs are the same
     * @param o object to compare
     * @return boolean
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Card card = (Card) o;
        return ID == card.ID;
    }
}