package it.polimi.ingsw.model.chat;

import it.polimi.ingsw.controller.Controller;
import it.polimi.ingsw.model.exceptions.GameDoesNotExistException;
import it.polimi.ingsw.model.game.Game;
import it.polimi.ingsw.model.game.GameHandler;
import it.polimi.ingsw.model.game.Lobby;
import it.polimi.ingsw.model.player.Player;
import junit.framework.TestCase;
import org.junit.Test;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class ChatTest extends TestCase {
    GameHandler gameHandler=new GameHandler();
    Player anna;
    Player eric;
    Player giorgio;
    Message m1;
    Message m2;
    Message m3;
    Controller c;
    public void setUp() throws GameDoesNotExistException, IOException {

        gameHandler= new GameHandler();
        c=new Controller(gameHandler);
        gameHandler.getLobbies().clear();
        gameHandler.createLobby(3);
        gameHandler.getLobbies().get(0).getPlayers().add("anna");
        gameHandler.getLobbies().get(0).getPlayers().add("eric");
        gameHandler.getLobbies().get(0).getPlayers().add("giorgio");
        gameHandler.setNumOfGames(0);
        gameHandler.createGame(gameHandler.getLobbies().get(0));
        anna=gameHandler.getActiveGames().get(0).getPlayers().get(0);
        eric=gameHandler.getActiveGames().get(0).getPlayers().get(1);
        giorgio=gameHandler.getActiveGames().get(0).getPlayers().get(2);
        m1=new Message("ciao1",anna,eric,false);
        m2=new Message("ciao2",eric,anna,false);
        m3=new Message("ciao2",giorgio,null,true);
        System.out.println("game ID"+ gameHandler.getGame(0));
        c.send(m1, 0);
        c.send(m2, 0);
        c.send(m3, 0);
    }

    public void testSend(){
        assertEquals(anna.getChat().getSentMessages().get(0),m1);
        assertEquals(anna.getChat().getReceivedMessages().get(0),m2);
        assertEquals(anna.getChat().getReceivedMessages().get(1),m3);
        assertEquals(eric.getChat().getSentMessages().get(0),m2);
        assertEquals(eric.getChat().getReceivedMessages().get(0),m1);
        assertEquals(eric.getChat().getReceivedMessages().get(1),m3);
        assertTrue(giorgio.getChat().getReceivedMessages().isEmpty());
        assertEquals(giorgio.getChat().getSentMessages().get(0),m3);
    }

}

