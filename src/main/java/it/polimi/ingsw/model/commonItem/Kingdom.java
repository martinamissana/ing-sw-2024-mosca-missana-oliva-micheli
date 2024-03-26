package it.polimi.ingsw.model.commonItem;

public enum Kingdom implements ItemBox {
    PLANT, ANIMAL, FUNGI, INSECT;


    @Override
    public String getType() {
        return this.name();
    }
}
