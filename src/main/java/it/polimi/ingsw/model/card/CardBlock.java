package it.polimi.ingsw.model.card;

import java.io.Serializable;

/**
 * Class BlockedCard
 * instantiated in the field where it is not possible to place other cards due to the absence of one or more corner in the adjacent spots
 */
public class CardBlock extends Card implements Serializable {

    /**
     * Class constructor
     * sets all the attributes to default values as they are not relevant
     */
    public CardBlock() {
        super(-1, CardSide.FRONT, null, null);
    }
}
