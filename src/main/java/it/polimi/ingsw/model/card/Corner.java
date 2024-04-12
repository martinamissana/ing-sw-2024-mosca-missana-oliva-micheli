package it.polimi.ingsw.model.card;

import it.polimi.ingsw.model.commonItem.ItemBox;

import java.io.Serializable;

/**
 * Class Corner
 * instantiated when the corner is actually present on the card
 */
public class Corner implements Serializable {
    private final ItemBox item;

    /**
     * Class constructor
     * @param item
     */
    public Corner(ItemBox item){
        this.item=item;
    }

    /**
     * gets the item
     * @return ItemBox associated with the corner
     */
    public ItemBox getItem() {
        return this.item;
    }

}
