package it.polimi.ingsw.model.commonItem;

/**
 * Enumeration of the kingdoms in the game
 */
public enum Kingdom implements ItemBox {
    ANIMAL, FUNGI, INSECT, PLANT;


    @Override
    public String getType() {
        return this.name();
    }
}
