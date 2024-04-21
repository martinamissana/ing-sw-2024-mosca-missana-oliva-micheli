package it.polimi.ingsw.controller;

import it.polimi.ingsw.model.deck.DeckBufferType;
import it.polimi.ingsw.model.exceptions.*;
import it.polimi.ingsw.model.game.*;
import it.polimi.ingsw.model.player.*;

import org.junit.Test;
import static org.junit.Assert.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;


public class SetUpControllerTest {
    @Test
    public void SetUpTest() throws IOException, HandIsFullException, EmptyDeckException {
        // Game creation + SetUp:
        ArrayList<Player> players = new ArrayList<>();
        HashMap<Player, Integer> scoreboard = new HashMap<>();
        for (int i = 0; i < 4; i++) {
            Player p = new Player("Player" + (i+1));
            players.add(p);
            scoreboard.put(p, 0);
        }
        Game game = new Game(0, 4, players, scoreboard);
        assertNotNull(game);
        assertNotNull(game.getPlayers());
        assert game.getPlayers().get(1).getNickname().equals("Player2");

        SetUpController c = new SetUpController(game);
        assertNotNull(c);
        c.setGameArea();

        // Player + Pawn + Hand printing:
        for (Player p : players) {
            System.out.print(p.getNickname() + " (" + p.getPawn().name() + " - " + game.getScoreboard().get(p) + "): ");
            for(int i = 0; i < 3; i++) {
                System.out.print("[" + /* p.getHand().getCard(i).getCardID() + */ "]");
            }
            System.out.println("   goal: (" + /* p.getPrivateGoal().getGoalID() + */ ")");
        }

        // Deck + Deck Buffers printing
        System.out.println("\nResource Deck (" + game.getResourceDeck().getCards().size() + "):");
        for (int i = 0; i < game.getResourceDeck().getCards().size(); i++) {
            System.out.print("[" /* + game.getResourceDeck().getDeck().get(i).getCardID() + "]" */);
        }
        System.out.println("]\nDeck Buffers:  [" + game.getDeckBuffer(DeckBufferType.RES1).getCard().getCardID() + "] " +
                "[" + game.getDeckBuffer(DeckBufferType.RES2).getCard().getCardID() + "]");

        System.out.println("\nGolden Deck (" + game.getGoldenDeck().getCards().size() + "):");
        for (int i = 0; i < game.getGoldenDeck().getCards().size(); i++) {
            System.out.print("[" /* + game.getGoldenDeck().getDeck().get(i).getCardID() + "]" */);
        }
        System.out.println("]\nDeck Buffers:  [" + game.getDeckBuffer(DeckBufferType.GOLD1).getCard().getCardID() + "] " +
                "[" + game.getDeckBuffer(DeckBufferType.GOLD2).getCard().getCardID() + "]\n");

        // Goals printing:
        System.out.println("Common goals: (" + game.getCommonGoal1().getGoalID() + ") (" + game.getCommonGoal2().getGoalID() + ")\n\n");

        // Field printing:
        for (Player p: players) {

            System.out.print(p.getNickname() + ": " /* + p.getField().getMatrix().get(new Coords(0, 0)) */ + p.getField().toString());
            System.out.println();
        }
    }
}