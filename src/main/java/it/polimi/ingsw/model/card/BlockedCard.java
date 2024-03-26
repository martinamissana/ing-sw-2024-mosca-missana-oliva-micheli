package it.polimi.ingsw.model.card;

import java.util.HashMap;

public class BlockedCard extends Card {
    public BlockedCard(int cardID, CardSide side, HashMap<CornerType, Corner> frontCorner, HashMap<CornerType, Corner> backCorner) {
        super(cardID, side, frontCorner, backCorner);
    }
}
