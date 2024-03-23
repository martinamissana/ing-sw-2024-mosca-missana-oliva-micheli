package model.card;

import model.commonItem.ItemBox;

import java.util.Map;

public abstract class Card  {
    private int cardID;
    private CardSide side;
    private Map<CornerType,Corner> frontCorner;
    private Map<CornerType,Corner> backCorner;

  public Card(){
      //robe che dipendono dal json
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
}
