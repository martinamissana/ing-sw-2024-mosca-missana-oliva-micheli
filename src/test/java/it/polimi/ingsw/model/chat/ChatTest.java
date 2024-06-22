package it.polimi.ingsw.model.chat;

import it.polimi.ingsw.controller.Controller;
import it.polimi.ingsw.controller.exceptions.CannotJoinMultipleLobbiesException;
import it.polimi.ingsw.controller.exceptions.GameAlreadyStartedException;
import it.polimi.ingsw.controller.exceptions.PlayerChatMismatchException;
import it.polimi.ingsw.controller.exceptions.UnexistentUserException;
import it.polimi.ingsw.model.exceptions.*;
import it.polimi.ingsw.model.game.GameHandler;
import it.polimi.ingsw.model.player.Player;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

public class ChatTest {
    GameHandler gameHandler = new GameHandler();
    Player anna, eric, giorgio;
    Message m1, m2, m3, m9, m10, m11, m12;
    Controller c;

    @BeforeEach
    public void setUp() throws GameDoesNotExistException, IOException, FullLobbyException, LobbyDoesNotExistsException, CannotJoinMultipleLobbiesException, PlayerChatMismatchException, UnexistentUserException {
        gameHandler = new GameHandler();
        c = new Controller(gameHandler);
        anna = new Player("anna");
        eric = new Player("eric");
        giorgio = new Player("giorgio");
        c.getGh().getUsers().add(anna);
        c.getGh().getUsers().add(eric);
        c.getGh().getUsers().add(giorgio);
        gameHandler.getLobbies().clear();
        c.createLobby(3, anna.getNickname());
        c.joinLobby(eric.getNickname(), 0);
        c.joinLobby(giorgio.getNickname(), 0);
        m1 = new Message("ciao", anna, eric, false);
        m2 = new Message("come va", eric, anna, false);
        m3 = new Message("ciao a tutti", giorgio, null, true);
        m9 = new Message("ciaooo", anna, eric, false);
        m10 = new Message("quando inizia la partita?", eric, null, true);
        m11 = new Message("non so", anna, null, true);
        m12 = new Message("a breve", giorgio, null, true);
        c.send(m1, 0);
        c.send(m2, 0);
        c.send(m3, 0);
        c.send(m10, 0);
        c.send(m11, 0);
        c.send(m12, 0);
    }

    @Test
    public void testSend() {
        assertEquals(anna.getChat().getSentMessages().getFirst(), m1);
        assertEquals(anna.getChat().getReceivedMessages().get(0), m2);
        assertEquals(anna.getChat().getReceivedMessages().get(1), m3);
        assertEquals(eric.getChat().getSentMessages().getFirst(), m2);
        assertEquals(eric.getChat().getReceivedMessages().get(0), m1);
        assertEquals(eric.getChat().getReceivedMessages().get(1), m3);
        assertFalse(giorgio.getChat().getReceivedMessages().isEmpty());
        assertEquals(giorgio.getChat().getSentMessages().getFirst(), m3);
    }

    @Test
    public void testGetPrivateChatAndGlobalChat() {
        assertTrue(anna.getChat().getPrivateChat(eric).contains(m1));
        assertTrue(eric.getChat().getPrivateChat(anna).contains(m1));

        assertFalse(giorgio.getChat().getPrivateChat(eric).contains(m1));
        assertFalse(giorgio.getChat().getPrivateChat(anna).contains(m1));

        assertTrue(giorgio.getChat().getGlobalChat().contains(m3));
        assertTrue(giorgio.getChat().getGlobalChat().contains(m10));
        assertTrue(giorgio.getChat().getGlobalChat().contains(m11));
        assertTrue(giorgio.getChat().getGlobalChat().contains(m12));

        assertTrue(eric.getChat().getGlobalChat().contains(m3));
        assertTrue(eric.getChat().getGlobalChat().contains(m10));
        assertTrue(eric.getChat().getGlobalChat().contains(m11));
        assertTrue(eric.getChat().getGlobalChat().contains(m12));

        assertTrue(anna.getChat().getGlobalChat().contains(m3));
        assertTrue(anna.getChat().getGlobalChat().contains(m10));
        assertTrue(anna.getChat().getGlobalChat().contains(m11));
        assertTrue(anna.getChat().getGlobalChat().contains(m12));

    }
}

