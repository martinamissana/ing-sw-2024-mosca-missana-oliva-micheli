package it.polimi.ingsw.network.netMessage.s2c;

import it.polimi.ingsw.model.game.GamePhase;
import it.polimi.ingsw.network.netMessage.NetMessage;

public class GamePhaseChangedMessage extends NetMessage {
    private final Integer ID;
    private final GamePhase gamePhase;

    public GamePhaseChangedMessage(Integer ID, GamePhase gamePhase) {
        this.ID = ID;
        this.gamePhase = gamePhase;
    }

    public Integer getID() {
        return ID;
    }

    public GamePhase getGamePhase() {
        return gamePhase;
    }
}
