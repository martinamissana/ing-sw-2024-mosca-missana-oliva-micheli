package it.polimi.ingsw.model.game;

import it.polimi.ingsw.controller.Controller;
import it.polimi.ingsw.controller.exceptions.CannotJoinMultipleLobbiesException;
import it.polimi.ingsw.controller.exceptions.GameAlreadyStartedException;
import it.polimi.ingsw.controller.exceptions.UnexistentUserException;
import it.polimi.ingsw.model.card.CardSide;
import it.polimi.ingsw.model.card.Corner;
import it.polimi.ingsw.model.card.CornerType;
import it.polimi.ingsw.model.card.ResourceCard;
import it.polimi.ingsw.model.commonItem.CornerStatus;
import it.polimi.ingsw.model.commonItem.Kingdom;
import it.polimi.ingsw.model.commonItem.Resource;
import it.polimi.ingsw.model.exceptions.*;
import it.polimi.ingsw.model.player.Pawn;
import it.polimi.ingsw.model.player.Player;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.*;

public class GameHandlerTest {
    GameHandler gameHandler=new GameHandler();
    HashMap< CornerType, Corner > frontCorners=new HashMap<>();
    HashMap< CornerType, Corner > backCorners=new HashMap<>();
    Corner corner1=new Corner(CornerStatus.EMPTY);
    Corner corner2=new Corner(Resource.INKWELL);
    Player anna,eric,giorgio,anna1;
    Controller c=new Controller(gameHandler);
    ResourceCard card;

  @BeforeEach
    public void setUp() {
        frontCorners.put(CornerType.NORTH,corner1);
        backCorners.put(CornerType.NORTH,corner2);
        card = new ResourceCard(0, CardSide.FRONT, frontCorners,backCorners,0, Kingdom.FUNGI);
        anna=new Player("anna");
        eric=new Player("eric");
        giorgio=new Player("giorgio");
        anna1=new Player("anna");

    }
    @Test
    public void testAddUser() throws NicknameAlreadyTakenException, IOException {
        gameHandler.addUser(anna);
        gameHandler.addUser(giorgio);
        gameHandler.addUser(eric);
        assertThrows(NicknameAlreadyTakenException.class, () -> {
            gameHandler.addUser(anna1);
        });
        assertTrue(gameHandler.getUsers().contains(anna));
        assertTrue(gameHandler.getUsers().contains(eric));
        assertTrue(gameHandler.getUsers().contains(giorgio));
    }
    @Test
    public void testSaveAndLoad() throws IOException, ClassNotFoundException, GameDoesNotExistException, FullLobbyException, NicknameAlreadyTakenException, LobbyDoesNotExistsException, HandIsFullException, CannotJoinMultipleLobbiesException, GameAlreadyStartedException, PawnAlreadyTakenException, UnexistentUserException {
        c.getGh().getUsers().add(anna);
        c.getGh().getUsers().add(eric);
        c.getGh().getUsers().add(giorgio);
        c.createLobby(3,anna.getNickname());
        c.joinLobby(eric.getNickname(),0);
        c.joinLobby(giorgio.getNickname(),0);
        c.choosePawn(0,anna.getNickname(), Pawn.BLUE);
        c.choosePawn(0,eric.getNickname(),Pawn.RED);
        c.choosePawn( 0,giorgio.getNickname(),Pawn.YELLOW);
        anna.getHand().addCard(card);
        eric.getHand().addCard(card);
        giorgio.getHand().addCard(card);
        gameHandler.save();
        GameHandler gameHandler1=new GameHandler();
        gameHandler1.load();
        assertEquals(gameHandler.getGame(0).getPlayers().get(0).getNickname(), gameHandler1.getActiveGames().get(0).getPlayers().get(0).getNickname());
        assertEquals(gameHandler.getGame(0).getPlayers().get(0).getHand().getCard(0).getCardID(), gameHandler1.getActiveGames().get(0).getPlayers().get(0).getHand().getCard(0).getCardID());
    }
}