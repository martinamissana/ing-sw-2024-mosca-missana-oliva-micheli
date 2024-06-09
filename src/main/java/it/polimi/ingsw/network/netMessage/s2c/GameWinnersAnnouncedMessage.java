package it.polimi.ingsw.network.netMessage.s2c;

import it.polimi.ingsw.model.player.Player;
import it.polimi.ingsw.network.netMessage.NetMessage;

import java.util.ArrayList;
import java.util.HashMap;

public class GameWinnersAnnouncedMessage extends NetMessage {
    private Integer ID;
    private ArrayList<Player> winners;
    private HashMap <Player,Integer> goalsDone;

    public GameWinnersAnnouncedMessage(Integer ID, ArrayList<Player> winners, HashMap<Player,Integer> goalsDone) {
        this.ID = ID;
        this.winners=new ArrayList<>(winners);
        this.goalsDone=goalsDone;
    }

    public HashMap<Player, Integer> getGoalsDone() {
        return goalsDone;
    }

    public Integer getID() {
        return ID;
    }

    public ArrayList<Player> getWinners() {
        return winners;
    }
}
