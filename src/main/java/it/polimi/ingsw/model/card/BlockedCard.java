package it.polimi.ingsw.model.card;

import java.util.HashMap;

/**
 * Class BlockedCard
 * instantiated in the field where it is not possible to place other cards due to the absence of one or more corner in the adjacent spots
 */
public class BlockedCard extends Card {

    /**
     * Class constructor
     * sets all the attributes to default values as they are not relevant
     */
    public BlockedCard() {
        super(-1, CardSide.FRONT, null, null);
    }
}
