package it.polimi.ingsw.model.observer.events;

import it.polimi.ingsw.model.card.Card;
import it.polimi.ingsw.model.card.CardSide;
import it.polimi.ingsw.model.player.Coords;

/**
 *  CardPlacedOnFieldEvent class<br>
 *  Extends abstract class Event<br>
 *  Used to notify the virtual view of the client when a card is placed in field
 */
public class CardPlacedOnFieldEvent  extends Event{
    private final Coords coords;
    private final Integer ID;
    private final Card card;
    private final CardSide side;
    private final String nickname;

    /**
     * Class constructor
     * @param coords coordinates where the card is put
     * @param ID ID of the game
     * @param card card placed in field
     * @param side the side the card is on
     * @param nickname nickname of the player
     */
    public CardPlacedOnFieldEvent(Coords coords, Integer ID, Card card, CardSide side, String nickname) {
        this.coords = coords;
        this.ID = ID;
        this.card = card;
        this.side = side;
        this.nickname = nickname;
    }

    /**
     * @return coordinates where the card is put
     */
    public Coords getCoords() { return coords; }

    /**
     * @return Integer ID of the game
     */
    public Integer getID() { return ID; }

    /**
     * @return String nickname of the player
     */
    public String getNickname() { return nickname; }

    /**
     * @return card placed of field
     */
    public Card getCard() { return card; }

    /**
     * @return card side
     */
    public CardSide getSide() { return side; }
}
