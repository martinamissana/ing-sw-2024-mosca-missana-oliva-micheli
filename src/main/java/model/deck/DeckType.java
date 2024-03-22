package model.deck;

public enum DeckType {
    RESOURCE, GOLDEN;

    public String getType() {
        return this.name();
    }
}
