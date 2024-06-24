package it.polimi.ingsw.model.card;

import it.polimi.ingsw.model.commonItem.ItemBox;
import it.polimi.ingsw.model.commonItem.Kingdom;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Objects;

/**
 * Abstract Class Card
 * defines all the basic attributes and methods common to all the cards
 */
public abstract class Card implements Serializable {
    private final int ID;
    private CardSide side;
    private final HashMap<CornerType, Corner> frontCorner;
    private final HashMap<CornerType, Corner> backCorner;

    /**
     * Class constructor
     *
     * @param ID          the card ID
     * @param side        the side of the card that is visible
     * @param frontCorner the list of corners in the front
     * @param backCorner  the list of corners in the back
     */
    public Card(int ID, CardSide side, HashMap<CornerType, Corner> frontCorner, HashMap<CornerType, Corner> backCorner) {
        this.ID = ID;
        this.side = side;
        this.frontCorner = frontCorner;
        this.backCorner = backCorner;
    }

    /**
     * getter
     *
     * @return int card ID
     */
    public int getCardID() {
        return this.ID;
    }

    /**
     * getter
     *
     * @return CardSide
     */
    public CardSide getSide() {
        return this.side;
    }

    /**
     * getter
     *
     * @return Kingdom
     */
    public Kingdom getKingdom(){return null;}

    /**
     * depends on which side the card is set,
     * returns null if the corner does not exist
     *
     * @param cornerType the corner you want to get from the side the card is on
     * @return ItemBox associated with the given corner
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
     * getter
     *
     * @param type the corner type you want
     * @return Corner
     */
    public Corner getCorner(CornerType type) {
        if (this.side == CardSide.FRONT) return this.frontCorner.get(type);
        else return this.backCorner.get(type);
    }

    /**
     * Changes the value of side of the card from FRONT to BACK and vice-versa
     */
    public void flip() {
        if (side == CardSide.FRONT) {
            this.side = CardSide.BACK;
        } else {
            this.side = CardSide.FRONT;
        }
    }

    /**
     * setter
     * sets the side of the card
     *
     * @param side is the side that the card will be on
     */
    public void setSide(CardSide side) {
        this.side = side;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Card card = (Card) o;
        return ID == card.ID;
    }

}
