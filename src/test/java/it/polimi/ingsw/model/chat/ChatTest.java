package it.polimi.ingsw.model.chat;

import it.polimi.ingsw.controller.Controller;
import it.polimi.ingsw.controller.exceptions.CannotJoinMultipleLobbiesException;
import it.polimi.ingsw.controller.exceptions.GameAlreadyStartedException;
import it.polimi.ingsw.controller.exceptions.PlayerChatMismatchException;
import it.polimi.ingsw.model.exceptions.*;
import it.polimi.ingsw.model.game.GameHandler;
import it.polimi.ingsw.model.player.Player;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;

public class ChatTest {
    GameHandler gameHandler=new GameHandler();
    Player anna,eric,giorgio;
    Message m1,m2,m3,m4,m5,m6,m7,m8,m9,m10,m11,m12;
    Controller c;

    @Before
    public void setUp() throws GameDoesNotExistException, IOException, FullLobbyException, LobbyDoesNotExistsException, NicknameAlreadyTakenException, CannotJoinMultipleLobbiesException, PlayerChatMismatchException, GameAlreadyStartedException, PawnAlreadyTakenException {

        gameHandler= new GameHandler();
        c=new Controller(gameHandler);
        gameHandler.getLobbies().clear();
        anna=new Player("anna");
        eric=new Player("eric");
        giorgio=new Player("giorgio");
        c.createLobby(3,anna);
        c.joinLobby(eric,0);
        c.joinLobby(giorgio,0);
        //gameHandler.setNumOfGames(0);
        //c.createGame(gameHandler.getLobbies().get(0));
        m1=new Message("ciao",anna,eric,false);
        m2=new Message("come va",eric,anna,false);
        m3=new Message("ciao a tutti",giorgio,null,true);
        m4=new Message("e che hai fatto oggi",eric,anna,false);
        m5=new Message("tutto bene",anna,eric,false);
        m6=new Message("meccanica",anna,eric,false);
        m7=new Message("tu?",anna,eric,false);
        m8=new Message("niente",eric,anna,false);
        m9=new Message("ciaooo",anna,eric,false);
        m10=new Message("quando inizia la partita?",eric,null,true);
        m11=new Message("non so",anna,null,true);
        m12=new Message("a breve",giorgio,null,true);
        c.send(m1, 0);
        c.send(m2, 0);
        c.send(m3, 0);
        c.send(m4, 0);
        c.send(m5, 0);
        c.send(m6, 0);
        c.send(m7, 0);
        c.send(m8, 0);
        c.send(m9, 0);
        c.send(m10, 0);
        c.send(m11, 0);
        c.send(m12, 0);
    }

    @Test
    public void testSend(){
        Assert.assertEquals(anna.getChat().getSentMessages().get(0), m1);
        Assert.assertEquals(anna.getChat().getReceivedMessages().get(0), m2);
        Assert.assertEquals(anna.getChat().getReceivedMessages().get(1), m3);
        Assert.assertEquals(eric.getChat().getSentMessages().get(0), m2);
        Assert.assertEquals(eric.getChat().getReceivedMessages().get(0), m1);
        Assert.assertEquals(eric.getChat().getReceivedMessages().get(1), m3);
        Assert.assertFalse(giorgio.getChat().getReceivedMessages().isEmpty());
        Assert.assertEquals(giorgio.getChat().getSentMessages().get(0), m3);
    }

    @Test
    public void testGetPrivateChatAndGlobalChat(){
        int i;
        System.out.println("\nprivate chat:");
        for(i=0;i<anna.getChat().getPrivateChat(eric).size();i++) {
            if (anna.getChat().getPrivateChat(eric).get(i).getSender() == anna)
                System.out.println("you:" + anna.getChat().getPrivateChat(eric).get(i).getText());
            else System.out.println(anna.getChat().getPrivateChat(eric).get(i).getSender().getNickname() +":"+ anna.getChat().getPrivateChat(eric).get(i).getText());
        }
        System.out.println("\nprivate chat:");
        for(i=0;i<eric.getChat().getPrivateChat(anna).size();i++) {
            if (eric.getChat().getPrivateChat(anna).get(i).getSender() == eric)
                System.out.println("you:" + eric.getChat().getPrivateChat(anna).get(i).getText());
            else System.out.println(eric.getChat().getPrivateChat(anna).get(i).getSender().getNickname() +":"+ eric.getChat().getPrivateChat(anna).get(i).getText());
        }
        System.out.println("\nglobal chat:");
        for(i=0;i<eric.getChat().getGlobalChat().size();i++) {
            if (eric.getChat().getGlobalChat().get(i).getSender() == eric)
                System.out.println("you:" + eric.getChat().getGlobalChat().get(i).getText());
            else System.out.println(eric.getChat().getGlobalChat().get(i).getSender().getNickname() +":"+ eric.getChat().getGlobalChat().get(i).getText());
        }
        System.out.println("\nglobal chat:");
        for(i=0;i<giorgio.getChat().getGlobalChat().size();i++) {
            if (giorgio.getChat().getGlobalChat().get(i).getSender() == giorgio)
                System.out.println("you:" + giorgio.getChat().getGlobalChat().get(i).getText());
            else System.out.println(giorgio.getChat().getGlobalChat().get(i).getSender().getNickname() +":"+ giorgio.getChat().getGlobalChat().get(i).getText());
        }
    }
}

