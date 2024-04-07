package it.polimi.ingsw.model.chat;

import it.polimi.ingsw.model.game.Game;
import it.polimi.ingsw.model.player.Player;
import junit.framework.TestCase;

import java.util.ArrayList;
import java.util.HashMap;

public class ChatTest extends TestCase {

    public void setUp() throws Exception {
        super.setUp();

    }

    public void tearDown() throws Exception {
    }

    /*public void testGetReceivedMessagesFromPlayer() {
        Game game=new Game(3,new HashMap<>());
        Player anna=new Player("anna");
        game.getPlayers().put(1,anna);
        Player eric=new Player("eric");
        game.getPlayers().put(1,eric);
        Player giorgio=new Player("giorgio");
        game.getPlayers().put(1,giorgio);
        Message mess1=new Message("ciao",anna,eric,false);
        Message mess2=new Message("ciao",eric,anna,false);
        Message mess3=new Message("ciao",anna,null,true);
        //send()--when controller is ready
        assertTrue(anna.getChat().getReceivedMessages().contains(mess3));
    }*/

    public void testGetSentMessagesToPlayer() {
    }

    public void testGetGlobalReceivedMessages() {
    }

    public void testGetGlobalSentMessages() {
    }
}