package it.polimi.ingsw.model.card;

/**
 * Enumeration of the types of golden cards
 */
public enum GoldenCardType {
    DIRECT,   // gives points directly
    RESOURCE, //gives points counting a resource
    CORNER    //gives points if the card is covering n corners
}
