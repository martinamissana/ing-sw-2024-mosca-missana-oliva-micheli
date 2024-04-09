package it.polimi.ingsw.model.game;

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
    @BeforeEach
    public void setUp() throws IOException {
        anna=new Player("anna");
        eric=new Player("eric");
        giorgio=new Player("giorgio");
        gameHandler= new GameHandler();
        gameHandler.createLobby(3);
        gameHandler.getLobbies().get(0).getPlayers().add("anna");
        gameHandler.getLobbies().get(0).getPlayers().add("eric");
        gameHandler.getLobbies().get(0).getPlayers().add("giorgio");
        //gameHandler.getActiveGames().get(0).getPlayers().put(anna,0);
        //gameHandler.getActiveGames().get(0).getPlayers().put(eric,1);
        //gameHandler.getActiveGames().get(0).getPlayers().put(giorgio,2);
        gameHandler.createGame(gameHandler.getLobbies().get(0));
    }

    public void testLoad() throws IOException, ClassNotFoundException {
        gameHandler.save();
        GameHandler gameHandler1=new GameHandler();
        gameHandler1.load();
        assertEquals(gameHandler.getActiveGames().get(0).getPlayers().get(anna),gameHandler1.getActiveGames().get(0).getPlayers().get(anna));
    }
}