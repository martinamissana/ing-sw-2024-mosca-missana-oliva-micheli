package it.polimi.ingsw.model.commonItem;

public enum CornerStatus implements ItemBox {
    EMPTY;

    @Override
    public String getType() {
        return this.name();
    }
}
