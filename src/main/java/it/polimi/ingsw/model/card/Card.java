package it.polimi.ingsw.model.card;

import it.polimi.ingsw.model.commonItem.ItemBox;
import it.polimi.ingsw.model.commonItem.Kingdom;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Objects;

/**
 * Abstract Class Card<br>
 * defines all basic attributes and methods common to all cards
 */
public abstract class Card implements Serializable {
    private final int ID;
    private CardSide side;
    private final HashMap<CornerType, Corner> frontCorner;
    private final HashMap<CornerType, Corner> backCorner;

    /**
     * Class constructor
     * @param ID           the card's ID
     * @param side         indicates which side the card is currently on
     * @param frontCorner list of corners on the front side
     * @param backCorner  list of corners on the back side
     */
    public Card(int ID, CardSide side, HashMap<CornerType, Corner> frontCorner, HashMap<CornerType, Corner> backCorner) {
        this.ID = ID;
        this.side = side;
        this.frontCorner = frontCorner;
        this.backCorner = backCorner;
    }

    /**
     * @return int
     */
    public int getCardID() {
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
    public Kingdom getKingdom(){return null;}

    /**
     * returns the item contained by the specified corner
     * on the side that the card is currently on, if there's one.<br>
     * returns {@code null} otherwise.
     * @param cornerType corner to return the item of
     * @return ItemBox
     */
    public ItemBox getItemFromCorner(CornerType cornerType) {
        if (side == CardSide.FRONT) {
            if (frontCorner.containsKey(cornerType)) {
                return frontCorner.get(cornerType).getItem();
            } else {
                return null;
            }
        } else {
            if (backCorner.containsKey(cornerType)) {
                return backCorner.get(cornerType).getItem();
            } else {
                return null;
            }
        }
    }

    /**
     * returns the corner of the specified type
     * on the side that the card is currently on, if there's one.<br>
     * returns {@code null} otherwise.
     * @param  type corner type you want
     * @return Corner
     */
    public Corner getCorner(CornerType type) {
        if (this.side == CardSide.FRONT) return this.frontCorner.get(type);
        else return this.backCorner.get(type);
    }

    /**
     * switches the side that the card is on
     */
    public void flip() {
        if (side == CardSide.FRONT) {
            this.side = CardSide.BACK;
        } else {
            this.side = CardSide.FRONT;
        }
    }

    /**
     * @param side side to set the card to
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


    /**
     * Hashcode for this object
     * @return int
     */
    @Override
    public int hashCode() {
        return Objects.hash(ID);
    }
}
