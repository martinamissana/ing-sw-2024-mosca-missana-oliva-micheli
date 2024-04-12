package it.polimi.ingsw.model.game;

import it.polimi.ingsw.controller.Controller;
import it.polimi.ingsw.model.exceptions.FullLobbyException;
import it.polimi.ingsw.model.exceptions.GameDoesNotExistException;
import it.polimi.ingsw.model.exceptions.NicknameAlreadyTakenException;
import it.polimi.ingsw.model.player.Player;

import java.io.IOException;
import java.util.HashSet;
import junit.framework.TestCase;
import org.junit.jupiter.api.BeforeEach;

public class GameHandlerTest extends TestCase {
    GameHandler gameHandler;
    Game game;
    Player anna;
    Player eric;
    Player giorgio;
    Player sara;
    Player paola;
    @BeforeEach
    public void setUp() throws IOException, FullLobbyException, NicknameAlreadyTakenException {
        gameHandler= new GameHandler();
        gameHandler.createLobby(3);
        gameHandler.getLobbies().get(0).addPlayer("anna");
        gameHandler.getLobbies().get(0).addPlayer("eric");
        gameHandler.getLobbies().get(0).addPlayer("giorgio");
        gameHandler.setNumOfGames(0);
        gameHandler.createGame(gameHandler.getLobbies().get(0));
        anna=gameHandler.getActiveGames().get(0).getPlayers().get("anna");
        eric=gameHandler.getActiveGames().get(0).getPlayers().get("eric");
        giorgio=gameHandler.getActiveGames().get(0).getPlayers().get("giorgio");
    }

    public void testLoad() throws IOException, ClassNotFoundException {
        gameHandler.save();
        GameHandler gameHandler1=new GameHandler();
        gameHandler1.load();
        assertEquals(gameHandler.getActiveGames().get(0).getPlayers().get(anna),gameHandler1.getActiveGames().get(0).getPlayers().get(anna));
    }

    public void testCreateGame() throws IOException, GameDoesNotExistException, FullLobbyException, NicknameAlreadyTakenException {
        assertTrue(gameHandler.getGame(0).getPlayers().containsValue(anna));
        assertTrue(gameHandler.getGame(0).getPlayers().containsValue(eric));
        assertTrue(gameHandler.getGame(0).getPlayers().containsValue(giorgio));
        assertEquals(anna.getNickname(),"anna");
        assertEquals(eric.getNickname(),"eric");
        assertEquals(giorgio.getNickname(),"giorgio");
    }

}