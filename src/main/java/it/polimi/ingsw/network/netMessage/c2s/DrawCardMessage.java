package it.polimi.ingsw.network.netMessage.c2s;

import it.polimi.ingsw.model.deck.DeckTypeBox;
import it.polimi.ingsw.network.netMessage.NetMessage;

public class DrawCardMessage extends NetMessage {
    private final Integer gameID;
    private final String nickname;
    private final DeckTypeBox deckTypeBox;

    public DrawCardMessage(Integer gameID, String nickname, DeckTypeBox deckTypeBox) {
        this.gameID = gameID;
        this.nickname = nickname;
        this.deckTypeBox = deckTypeBox;
    }

    public Integer getGameID() {
        return gameID;
    }

    public String getNickname() {
        return nickname;
    }

    public DeckTypeBox getDeckTypeBox() {
        return deckTypeBox;
    }
}
