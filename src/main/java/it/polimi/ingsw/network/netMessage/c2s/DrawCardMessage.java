package it.polimi.ingsw.network.netMessage.c2s;

import it.polimi.ingsw.model.deck.DeckTypeBox;
import it.polimi.ingsw.network.netMessage.NetMessage;

/**
 * Class DrawCardMessage
 * used to request the server to draw a card from a deck/deckbuffer
 */
public class DrawCardMessage extends NetMessage {
    private final Integer gameID;
    private final String nickname;
    private final DeckTypeBox deckTypeBox;

    /**
     * Class constructor
     * @param gameID the game ID
     * @param nickname the name of the player
     * @param deckTypeBox the deck/deckBuffer to draw from
     */
    public DrawCardMessage(Integer gameID, String nickname, DeckTypeBox deckTypeBox) {
        this.gameID = gameID;
        this.nickname = nickname;
        this.deckTypeBox = deckTypeBox;
    }

    /**
     * @return Integer
     */
    public Integer getGameID() {
        return gameID;
    }

    /**
     * @return String
     */
    public String getNickname() {
        return nickname;
    }

    /**
     * @return DeckTypeBox
     */
    public DeckTypeBox getDeckTypeBox() {
        return deckTypeBox;
    }
}
