package model.card;

import java.util.Map;

public abstract class Card  {
    private int CardID;
    private CardSide side;
    private Map<CornerType,Corner> corner;

    public int getCardID() {
        return CardID;
    }

    public CardSide getSide() {
        return side;
    }

    public void flip (Card card){
        if (side == CardSide.FRONT) {
            side= CardSide.BACK;
        }else{
            side = CardSide.FRONT;
        }
        return;
    }
}
