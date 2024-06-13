package it.polimi.ingsw.model.card;

import it.polimi.ingsw.model.commonItem.ItemBox;

import java.io.Serializable;

/**
 * Class Corner
 * instantiated when the corner is actually present on the card
 */
public class Corner implements Serializable {
    private final ItemBox item;
    private boolean covered = false;

    /**
     * Class constructor
     * @param item - the item that is stored in the corner
     */
    public Corner(ItemBox item){
        this.item=item;
    }

    /**
     * getter
     * @return ItemBox - item associated with the corner
     */
    public ItemBox getItem() {
        return this.item;
    }

    /**
     * It sees if corner is covered by another card
     * @return covered - if the corner is covered or not
     */
    public boolean isCovered() {
        return covered;
    }

    /**
     * Set corner covered
     */
    public void cover() {
        this.covered = true;
    }
}
