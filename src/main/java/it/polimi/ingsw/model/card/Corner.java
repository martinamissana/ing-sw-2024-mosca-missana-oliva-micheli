package it.polimi.ingsw.model.card;

import it.polimi.ingsw.model.commonItem.ItemBox;

import java.io.Serializable;

/**
 * Class Corner<br>
 * instantiated only when the corner is actually present on the card, meaning it's not a blocking corner
 */
public class Corner implements Serializable {
    private final ItemBox item;
    private boolean covered = false;

    /**
     * Class constructor<br>
     * @param item the item to be stored in the corner
     */
    public Corner(ItemBox item) {
        this.item = item;
    }

    /**
     * returns the item contained by the corner, {@code null} if it's empty
     * @return ItemBox
     */
    public ItemBox getItem() {
        return this.item;
    }

    /**
     * returns {@code true} if the corner is covered by another card's corner, {@code false} otherwise
     * @return boolean
     */
    public boolean isCovered() {
        return covered;
    }

    /**
     * marks the corner as covered
     */
    public void cover() {
        this.covered = true;
    }
}