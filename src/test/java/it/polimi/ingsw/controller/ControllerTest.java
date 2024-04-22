package it.polimi.ingsw.controller;

import it.polimi.ingsw.controller.exceptions.GameAlreadyStartedException;
import it.polimi.ingsw.model.card.CardSide;
import it.polimi.ingsw.model.card.Corner;
import it.polimi.ingsw.model.card.CornerType;
import it.polimi.ingsw.model.card.ResourceCard;
import it.polimi.ingsw.model.commonItem.CornerStatus;
import it.polimi.ingsw.model.commonItem.Kingdom;
import it.polimi.ingsw.model.commonItem.Resource;
import it.polimi.ingsw.model.deck.DeckBufferType;
import it.polimi.ingsw.model.exceptions.*;
import it.polimi.ingsw.model.game.GameHandler;
import it.polimi.ingsw.model.goal.Goal;
import it.polimi.ingsw.model.player.Coords;
import it.polimi.ingsw.model.player.Pawn;
import it.polimi.ingsw.model.player.Player;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.ArrayList;
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
    @Test
    public void SetUpTest() throws IOException, HandIsFullException, FullLobbyException, LobbyDoesNotExistsException, NicknameAlreadyTakenException, GameDoesNotExistException, EmptyDeckException, PawnAlreadyTakenException {
        GameHandler gh = new GameHandler();
        Controller c = new Controller(gh);

        // Game creation + SetUp:
        ArrayList<Player> players = new ArrayList<>();
        for (int i = 0; i < 4; i++) {
            Player p = new Player("Player" + (i+1));
            players.add(p);
        }

        c.createLobby(4, players.getFirst());
        for (int i = 1; i < 4; i++) c.joinLobby(players.get(i), 0);

        c.setGameArea(0);
        c.giveStarterCards(0);

        // Starter card choosing:
        for(Player p : players) c.chooseCardSide(p, CardSide.FRONT);

        // Pawn choosing:
        int j = 0;

        for(Player p : players) {
            c.choosePawn(0, p, Pawn.values()[j]);
            j++;
        }

        // Hand filling:
        c.fillHands(0);

        // Setting goals:
        c.setCommonGoals(0);

        for (Player p : players) {
            ArrayList<Goal> goals = c.giveGoals();
            c.choosePersonalGoal(p, goals.getFirst());
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