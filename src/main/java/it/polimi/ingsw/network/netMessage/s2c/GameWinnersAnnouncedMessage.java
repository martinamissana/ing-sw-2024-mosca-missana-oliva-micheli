package it.polimi.ingsw.network.netMessage.s2c;

import it.polimi.ingsw.model.player.Player;
import it.polimi.ingsw.network.netMessage.NetMessage;

import java.util.ArrayList;

public class GameWinnersAnnouncedMessage extends NetMessage {
    private Integer ID;
    private ArrayList<Player> winners;

    public GameWinnersAnnouncedMessage(Integer ID, ArrayList<Player> winners) {
        this.ID = ID;
        this.winners=new ArrayList<>(winners);
    }

    public Integer getID() {
        return ID;
    }

    public ArrayList<Player> getWinners() {
        return winners;
    }
}
