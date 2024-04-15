package it.polimi.ingsw.model.game;

import it.polimi.ingsw.controller.Controller;
import it.polimi.ingsw.model.card.*;
import it.polimi.ingsw.model.commonItem.CornerStatus;
import it.polimi.ingsw.model.commonItem.Kingdom;
import it.polimi.ingsw.model.commonItem.Resource;
import it.polimi.ingsw.model.exceptions.*;
import it.polimi.ingsw.model.player.Hand;
import it.polimi.ingsw.model.player.Player;

import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import junit.framework.TestCase;
import org.junit.jupiter.api.BeforeEach;

public class GameHandlerTest extends TestCase {
    GameHandler gameHandler=new GameHandler();
    HashMap< CornerType, Corner > frontCorners;
    HashMap< CornerType, Corner > backCorners;
    Corner corner1=new Corner(CornerStatus.EMPTY);
    Corner corner2=new Corner(Resource.INKWELL);
    ResourceCard card= new ResourceCard(0, CardSide.FRONT, frontCorners,backCorners,0, Kingdom.FUNGI);

    Game game;
    Player anna;
    Player eric;
    Player giorgio;
    Player sara;
    Player paola;
    Controller c=new Controller(gameHandler);


    public void setUp() throws IOException, FullLobbyException, NicknameAlreadyTakenException, LobbyDoesNotExistsException, HandIsFullException {
        anna=new Player("anna");
        eric=new Player("eric");
        giorgio=new Player("giorgio");
        c.createLobby(3,anna);
        c.joinLobby(eric,0);
        c.joinLobby(giorgio,0);
        c.createGame(gameHandler.getLobbies().get(0));
        anna.getHand().addCard(card);
    }

    public void testSaveAndLoad() throws IOException, ClassNotFoundException, GameDoesNotExistException {
        gameHandler.save();
        GameHandler gameHandler1=new GameHandler();
        gameHandler1.load();
        assertEquals(gameHandler.getGame(0).getPlayers().get(0).getNickname(),gameHandler1.getActiveGames().get(0).getPlayers().get(0).getNickname());
        assertEquals(gameHandler.getGame(0).getPlayers().get(0).getHand().getCard(0).getCardID(),gameHandler1.getActiveGames().get(0).getPlayers().get(0).getHand().getCard(0).getCardID());
    }/**/

    public void testCreateGame() throws IOException, GameDoesNotExistException, FullLobbyException, NicknameAlreadyTakenException {
        assertTrue(gameHandler.getGame(0).getPlayers().containsValue(anna));
        assertTrue(gameHandler.getGame(0).getPlayers().containsValue(eric));
        assertTrue(gameHandler.getGame(0).getPlayers().containsValue(giorgio));
        assertEquals(gameHandler.getGame(0).getPlayers().get(0).getNickname(),"anna");
        assertEquals(anna.getNickname(),"anna");
        assertEquals(eric.getNickname(),"eric");
        assertEquals(giorgio.getNickname(),"giorgio");
    }

}