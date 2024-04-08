package it.polimi.ingsw.model.card;

import it.polimi.ingsw.model.commonItem.ItemBox;
import it.polimi.ingsw.model.commonItem.Kingdom;

import java.io.Serializable;
import java.util.HashMap;

/**
 * Abstract Class Card
 * defines all the basic attributes and methods common to all the cards
 */
public abstract class Card implements Serializable {
    private final int ID;
    private CardSide side;
    private final HashMap<CornerType,Corner> frontCorner;
    private final HashMap<CornerType,Corner> backCorner;

    /**
     * Class constructor
     * @param ID
     * @param side
     * @param frontCorner
     * @param backCorner
     */
    public Card(int ID, CardSide side, HashMap<CornerType, Corner> frontCorner, HashMap<CornerType, Corner> backCorner) {
        this.ID = ID;
        this.side = side;
        this.frontCorner = frontCorner;
        this.backCorner = backCorner;
    }

    /**
     * gets the ID
     * @return card ID
     */
    public int getCardID() {
        return this.ID;
    }

    /**
     * gets the side
     * @return card side
     */
    public CardSide getSide() {
        return this.side;
    }

    /**
     * gets the kingdom
     * @return card kingdom
     */
    public Kingdom getKingdom(){return null;}

    /**
     * depends on which side the card is set
     * returns null if the corner does not exist
     * @param cornerType
     * @return ItemBox associated with the given corner
     */
    public ItemBox getCorner(CornerType cornerType) {
        if(side==CardSide.FRONT){
            if(frontCorner.containsKey(cornerType)) {
                return frontCorner.get(cornerType).getItem();
            } else {
                return null;
            }
        } else {
            if(backCorner.containsKey(cornerType)) {
                return backCorner.get(cornerType).getItem();
            } else {
                return null;
            }
        }
    }

    /**
     * Changes the value of side of the card from FRONT to BACK and vice-versa
     */
    public void flip (){
        if (side == CardSide.FRONT) {
            this.side= CardSide.BACK;
        }else{
            this.side = CardSide.FRONT;
        }
    }
}
