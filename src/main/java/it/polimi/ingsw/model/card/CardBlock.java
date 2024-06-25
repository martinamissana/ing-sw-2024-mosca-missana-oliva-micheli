package it.polimi.ingsw.model.card;

import java.io.Serializable;

/**
 * Class CardBlock<br>
 * instantiated in the field map whenever a position gets blocked by a blocking corner (its key will be that position)
 */
public class CardBlock extends Card implements Serializable {

    /**
     * Class constructor<br>
     * sets all the attributes to default values as they are not relevant
     */
    public CardBlock() {
        super(-1, CardSide.FRONT, null, null);
    }
}
