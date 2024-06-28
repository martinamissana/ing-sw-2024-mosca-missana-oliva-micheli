package it.polimi.ingsw.model.card;

/**
 * Enumeration of golden card types, which indicate how points should be calculated when placing a golden card:
 * <strong>Direct</strong>: the number of points to assign is indicated directly on the card,
 *                              and is stored in the card's {@code directPoints} parameter
 * <strong>Resource</strong>: these cards indicate a specific type of resource,
 *                                which stored in the card's {@code resourceToCount} parameter.<br>
 *                                the number of points to assign is equal to that resource's count
 *                                on the player's field, which is stored on the field's {@code totalResources} map
 * <strong>Corner</strong>: the player gets two points for each corner covered by the golden card they're placing
 */
public enum GoldenCardType {
    DIRECT,
    RESOURCE,
    CORNER
}
