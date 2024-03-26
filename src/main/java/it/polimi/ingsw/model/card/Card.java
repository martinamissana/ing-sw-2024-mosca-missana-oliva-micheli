package it.polimi.ingsw.model.card;

import it.polimi.ingsw.model.commonItem.Kingdom;

import java.util.HashMap;

public abstract class Card  {
    private int cardID;
    private CardSide side;
    private HashMap<CornerType,Corner> frontCorner;
    private HashMap<CornerType,Corner> backCorner;

    public Card(int cardID, CardSide side, HashMap<CornerType, Corner> frontCorner, HashMap<CornerType, Corner> backCorner) {
        this.cardID = cardID;
        this.side = side;
        this.frontCorner = frontCorner;
        this.backCorner = backCorner;
    }

    public int getCardID() {
        return this.cardID;
    }
    public CardSide getSide() {
        return this.side;
    }

    public void flip (Card card){
        if (side == CardSide.FRONT) {
            this.side= CardSide.BACK;
        }else{
            this.side = CardSide.FRONT;
        }
    }

    public Corner getCorner(CornerType cornerType){
        if(side==CardSide.FRONT){
            return frontCorner.get(cornerType);
        } else {
            return backCorner.get(cornerType);
        }
    }

    public Kingdom getKingdom(){
      return null;
    }


}
