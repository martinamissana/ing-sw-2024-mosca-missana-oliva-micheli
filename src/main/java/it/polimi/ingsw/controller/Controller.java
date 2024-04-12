package it.polimi.ingsw.controller;

import it.polimi.ingsw.model.chat.Message;
import it.polimi.ingsw.model.exceptions.GameDoesNotExistException;
import it.polimi.ingsw.model.game.Game;
import it.polimi.ingsw.model.game.GameHandler;
import it.polimi.ingsw.model.player.Player;

import java.io.Serializable;
import java.util.Map;

public class Controller implements Serializable {
    GameHandler gh;

    public Controller(GameHandler gh) {
        this.gh = gh;
    }

    public GameHandler getGh() {
        return gh;
    }



    //chat related methods
    public void send(Message message, int game) throws GameDoesNotExistException {
        try {
            if (!message.isGlobal()) {
                message.getSender().getChat().getSentMessages().add(message);
                message.getReceiver().getChat().getReceivedMessages().add(message);
            } else {
                message.getSender().getChat().getSentMessages().add(message);
                for (Player player : gh.getGame(game).getPlayers().values()) {
                    if (player != message.getSender()) player.getChat().getReceivedMessages().add(message);
                }
            }
        }
            catch (GameDoesNotExistException e) {
                    throw new GameDoesNotExistException("Game with ID " + game + " does not exist");
                }
    }

}
