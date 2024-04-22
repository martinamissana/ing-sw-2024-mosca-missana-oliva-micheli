package it.polimi.ingsw.controller;

import it.polimi.ingsw.controller.exceptions.GameAlreadyStartedException;
import it.polimi.ingsw.model.card.CardSide;
import it.polimi.ingsw.model.card.Corner;
import it.polimi.ingsw.model.card.CornerType;
import it.polimi.ingsw.model.card.ResourceCard;
import it.polimi.ingsw.model.commonItem.CornerStatus;
import it.polimi.ingsw.model.commonItem.Kingdom;
import it.polimi.ingsw.model.commonItem.Resource;
import it.polimi.ingsw.model.exceptions.*;
import it.polimi.ingsw.model.game.GameHandler;
import it.polimi.ingsw.model.player.Player;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.HashMap;


public class ControllerTest {
    GameHandler gameHandler=new GameHandler();
    HashMap<CornerType, Corner> frontCorners;
    HashMap< CornerType, Corner > backCorners;
    Corner corner1=new Corner(CornerStatus.EMPTY);
    Corner corner2=new Corner(Resource.INKWELL);
    ResourceCard card= new ResourceCard(0, CardSide.FRONT, frontCorners,backCorners,0, Kingdom.FUNGI);
    Player anna, eric, giorgio,sara,paola;
    Controller c=new Controller(gameHandler);


    @Before
    public void setUp() throws IOException, FullLobbyException, NicknameAlreadyTakenException, LobbyDoesNotExistsException, HandIsFullException {
        anna=new Player("anna");
        eric=new Player("eric");
        giorgio=new Player("giorgio");
        sara=new Player("sara");
        paola=new Player("paola");
        c.createLobby(3,anna);
        c.joinLobby(eric,0);
        c.joinLobby(giorgio,0);
        anna.getHand().addCard(card);
    }

    @Test
    public void testCreateGameAndTerminateGame() throws IOException, GameDoesNotExistException, FullLobbyException, NicknameAlreadyTakenException, LobbyDoesNotExistsException {
        Assert.assertTrue(gameHandler.getGame(0).getPlayers().contains(anna));
        Assert.assertTrue(gameHandler.getGame(0).getPlayers().contains(eric));
        Assert.assertTrue(gameHandler.getGame(0).getPlayers().contains(giorgio));
        Assert.assertEquals(anna.getHand().getCard(0),card);
        c.terminateGame(0);
        Assert.assertTrue(gameHandler.getActiveGames().isEmpty());
        Assert.assertTrue(gameHandler.getLobbies().isEmpty());
    }
    @Test (expected = FullLobbyException.class)
    public void testJoinLobbyButLobbyIsFull() throws FullLobbyException, NicknameAlreadyTakenException, LobbyDoesNotExistsException, IOException, GameDoesNotExistException {
        c.joinLobby(sara,0);
        Assert.assertFalse(gameHandler.getLobby(0).getPlayers().contains(sara));
        Assert.assertFalse(gameHandler.getGame(0).getPlayers().contains(sara));
    }
    @Test (expected = LobbyDoesNotExistsException.class)
    public void testJoinLobbyButLobbyDoesNotExist() throws FullLobbyException, NicknameAlreadyTakenException, LobbyDoesNotExistsException, IOException, GameDoesNotExistException {
        c.joinLobby(sara,1);
        Assert.assertFalse(gameHandler.getLobby(0).getPlayers().contains(sara));
        Assert.assertFalse(gameHandler.getGame(0).getPlayers().contains(sara));
    }
    @Test (expected = GameAlreadyStartedException.class)
    public void leaveLobby() throws FullLobbyException, LobbyDoesNotExistsException, NicknameAlreadyTakenException, IOException, GameAlreadyStartedException {
        c.createLobby(3, sara);
        c.joinLobby(paola,1);
        c.leaveLobby(sara, 1);
        Assert.assertTrue(gameHandler.getLobbies().containsKey(1));
        c.leaveLobby(paola, 1);
        Assert.assertFalse(gameHandler.getLobbies().containsKey(1));
        c.leaveLobby(anna, 0);
        Assert.assertTrue(gameHandler.getLobbies().containsKey(0));
        Assert.assertTrue(gameHandler.getActiveGames().containsKey(0));
    }
}