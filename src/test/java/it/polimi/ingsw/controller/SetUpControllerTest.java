package it.polimi.ingsw.controller;

import it.polimi.ingsw.model.card.CardSide;
import it.polimi.ingsw.model.deck.DeckBufferType;
import it.polimi.ingsw.model.exceptions.*;
import it.polimi.ingsw.model.goal.Goal;
import it.polimi.ingsw.model.goal.GoalBuffer;
import it.polimi.ingsw.model.player.Coords;
import it.polimi.ingsw.model.game.GameHandler;
import it.polimi.ingsw.model.player.Pawn;
import it.polimi.ingsw.model.player.Player;

import org.junit.Test;

import java.io.IOException;
import java.util.ArrayList;


public class SetUpControllerTest {
    @Test
    public void SetUpTest() throws IOException, HandIsFullException, FullLobbyException, LobbyDoesNotExistsException, NicknameAlreadyTakenException, GameDoesNotExistException, EmptyDeckException, PawnAlreadyTakenException {
        GameHandler gh = new GameHandler();
        Controller controller = new Controller(gh);
        SetUpController c = new SetUpController(gh);

        // Game creation + SetUp:
        ArrayList<Player> players = new ArrayList<>();
        for (int i = 0; i < 4; i++) {
            Player p = new Player("Player" + (i+1));
            players.add(p);
        }

        controller.createLobby(4, players.getFirst());
        for (int i = 1; i < 4; i++) controller.joinLobby(players.get(i), 0);

        c.setGameArea(0);
        c.giveStarterCards(0);

        // Starter card choosing:
        for(Player p : players) c.chooseCard(p, CardSide.FRONT);

        // Pawn choosing:
        int j = 0;

        for(Player p : players) {
            c.choosePawn(0, p, Pawn.values()[j]);
            j++;
        }

        // Hand filling:
        c.fillHands(0);

        // Setting goals:
        GoalBuffer goals = new GoalBuffer();
        c.chooseGoals(0, goals);

        for (Player p : players) {
            Goal goal1 = goals.getgoal();
            Goal goal2 = goals.getgoal();
            c.choosePersonalGoal(p, goal1, goal2, goal1);
        }

        // Player + Pawn + Hand printing:
        for (Player p : players) {
            System.out.print(p.getNickname() + " (" + p.getPawn().name() + " - " + gh.getGame(0).getScoreboard().get(p) + "): ");
            for(int i = 0; i < 3; i++) {
                System.out.print("[" + p.getHand().getCard(i).getCardID() + "]");
            }
            System.out.println("   goal: (" + p.getPrivateGoal().getGoalID() + ")");
        }

        // Deck + Deck Buffers printing
        System.out.println("\nResource Deck (" + gh.getGame(0).getResourceDeck().getCards().size() + " cards):");
        for (int i = 0; i < gh.getGame(0).getResourceDeck().getCards().size(); i++) {
            System.out.print("[" + gh.getGame(0).getResourceDeck().getCards().get(i).getCardID() + "]");
        }
        System.out.println("\nDeck Buffers:  [" + gh.getGame(0).getDeckBuffer(DeckBufferType.RES1).getCard().getCardID() + "] " +
                "[" + gh.getGame(0).getDeckBuffer(DeckBufferType.RES2).getCard().getCardID() + "]");

        System.out.println("\nGolden Deck (" + gh.getGame(0).getGoldenDeck().getCards().size() + " cards):");
        for (int i = 0; i < gh.getGame(0).getGoldenDeck().getCards().size(); i++) {
            System.out.print("[" + gh.getGame(0).getGoldenDeck().getCards().get(i).getCardID() + "]");
        }
        System.out.println("\nDeck Buffers:  [" + gh.getGame(0).getDeckBuffer(DeckBufferType.GOLD1).getCard().getCardID() + "] [" + gh.getGame(0).getDeckBuffer(DeckBufferType.GOLD2).getCard().getCardID() + "]\n");

        // Goals printing:
        System.out.println("Common goals: (" + gh.getGame(0).getCommonGoal1().getGoalID() + ") (" + gh.getGame(0).getCommonGoal2().getGoalID() + ")\n\n");

        // Field printing:
        for (Player p: players) {
            System.out.println(p.getNickname() + ": (starterID = " + p.getField().getMatrix().get(new Coords(0, 0)).getCardID() + ")");
            System.out.println(p.getField());
        }
    }
}