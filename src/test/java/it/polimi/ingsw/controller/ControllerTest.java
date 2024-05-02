package it.polimi.ingsw.controller;

import it.polimi.ingsw.controller.exceptions.GameAlreadyStartedException;
import it.polimi.ingsw.controller.exceptions.IllegalActionException;
import it.polimi.ingsw.controller.exceptions.IllegalGoalChosenException;
import it.polimi.ingsw.controller.exceptions.NotYourTurnException;
import it.polimi.ingsw.model.card.*;
import it.polimi.ingsw.model.commonItem.CornerStatus;
import it.polimi.ingsw.model.commonItem.ItemBox;
import it.polimi.ingsw.model.commonItem.Kingdom;
import it.polimi.ingsw.model.commonItem.Resource;
import it.polimi.ingsw.model.deck.DeckBufferType;
import it.polimi.ingsw.model.deck.DeckType;
import it.polimi.ingsw.model.exceptions.*;
import it.polimi.ingsw.model.game.CardsPreset;
import it.polimi.ingsw.model.game.Game;
import it.polimi.ingsw.model.game.GameHandler;
import it.polimi.ingsw.model.goal.*;
import it.polimi.ingsw.model.player.Coords;
import it.polimi.ingsw.model.player.Pawn;
import it.polimi.ingsw.model.player.Player;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import static org.junit.Assert.*;

public class ControllerTest {
    GameHandler gameHandler=new GameHandler();
    HashMap<CornerType, Corner> frontCorners;
    HashMap<CornerType, Corner> backCorners;
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
    public void testCreateGameAndTerminateGame() throws GameDoesNotExistException, LobbyDoesNotExistsException {
        assertTrue(gameHandler.getGame(0).getPlayers().contains(anna));
        assertTrue(gameHandler.getGame(0).getPlayers().contains(eric));
        assertTrue(gameHandler.getGame(0).getPlayers().contains(giorgio));
        assertEquals(anna.getHand().getCard(0),card);
        c.terminateGame(0);
        assertTrue(gameHandler.getActiveGames().isEmpty());
        assertTrue(gameHandler.getLobbies().isEmpty());
    }
    @Test (expected = FullLobbyException.class)
    public void testJoinLobbyButLobbyIsFull() throws FullLobbyException, NicknameAlreadyTakenException, LobbyDoesNotExistsException, IOException, GameDoesNotExistException {
        c.joinLobby(sara,0);
        assertFalse(gameHandler.getLobby(0).getPlayers().contains(sara));
        assertFalse(gameHandler.getGame(0).getPlayers().contains(sara));
    }
    @Test (expected = LobbyDoesNotExistsException.class)
    public void testJoinLobbyButLobbyDoesNotExist() throws FullLobbyException, NicknameAlreadyTakenException, LobbyDoesNotExistsException, IOException, GameDoesNotExistException {
        c.joinLobby(sara,1);
        assertFalse(gameHandler.getLobby(0).getPlayers().contains(sara));
        assertFalse(gameHandler.getGame(0).getPlayers().contains(sara));
    }
    @Test (expected = GameAlreadyStartedException.class)
    public void leaveLobby() throws FullLobbyException, LobbyDoesNotExistsException, NicknameAlreadyTakenException, IOException, GameAlreadyStartedException {
        c.createLobby(3, sara);
        c.joinLobby(paola,1);
        c.leaveLobby(sara, 1);
        assertTrue(gameHandler.getLobbies().containsKey(1));
        c.leaveLobby(paola, 1);
        assertFalse(gameHandler.getLobbies().containsKey(1));
        c.leaveLobby(anna, 0);
        assertTrue(gameHandler.getLobbies().containsKey(0));
        assertTrue(gameHandler.getActiveGames().containsKey(0));
    }
    @Test //(expected = PawnAlreadyTakenException.class)
    public void SetUpTest() throws IOException, HandIsFullException, FullLobbyException, LobbyDoesNotExistsException, NicknameAlreadyTakenException, GameDoesNotExistException, EmptyDeckException, PawnAlreadyTakenException, IllegalGoalChosenException {
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

        c.giveGoals(0);
        for (Player p : players) {
            c.choosePersonalGoal(p, p.getChoosableGoals().getFirst());
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

    @Test
    public void FlipCardTest() throws IOException, FullLobbyException, LobbyDoesNotExistsException, NicknameAlreadyTakenException, GameDoesNotExistException, HandIsFullException {
        GameHandler gh = new GameHandler();
        Controller con = new Controller(gh);

        // players
        ArrayList<Player> players = new ArrayList<>();
        for (int i = 0; i < 2; i++) players.add(new Player("Player" + (i+1)));

        // lobby + game
        con.createLobby(2, players.getFirst());
        con.joinLobby(players.get(1), 0);
        Game game = con.getGh().getGame(0);

        // hand setup
        ArrayList<ResourceCard> resourceCards = CardsPreset.getResourceCards();
        ArrayList<GoldenCard> goldenCards = CardsPreset.getGoldenCards();
        players.get(0).getHand().addCard(resourceCards.get(0));
        players.get(0).getHand().addCard(resourceCards.get(1));
        players.get(0).getHand().addCard(resourceCards.get(2));
        players.get(1).getHand().addCard(goldenCards.get(3));
        players.get(1).getHand().addCard(goldenCards.get(4));
        players.get(1).getHand().addCard(goldenCards.get(5));

        // card flipping
        con.flipCard(0, game.getPlayers().get(0), 0);
        assert(CardSide.BACK == game.getPlayers().get(0).getHand().getCard(0).getSide());
        con.flipCard(0, game.getPlayers().get(0), 1);
        assert(CardSide.BACK == game.getPlayers().get(0).getHand().getCard(1).getSide());
        con.flipCard(0, game.getPlayers().get(0), 2);
        assert(CardSide.BACK == game.getPlayers().get(0).getHand().getCard(2).getSide());
        con.flipCard(0, game.getPlayers().get(1), 0);
        assert(CardSide.BACK == game.getPlayers().get(1).getHand().getCard(0).getSide());
        con.flipCard(0, game.getPlayers().get(1), 1);
        assert(CardSide.BACK == game.getPlayers().get(1).getHand().getCard(1).getSide());
        con.flipCard(0, game.getPlayers().get(1), 2);
        assert(CardSide.BACK == game.getPlayers().get(1).getHand().getCard(2).getSide());
    }

    @Test
    public void PlayDrawTurnTest() throws FullLobbyException, LobbyDoesNotExistsException, NicknameAlreadyTakenException, IOException, GameDoesNotExistException, PawnAlreadyTakenException, EmptyDeckException, HandIsFullException, IllegalActionException, NotYourTurnException, IllegalMoveException, EmptyBufferException {
        GameHandler gh = new GameHandler();
        Controller con = new Controller(gh);

        // players
        ArrayList<Player> players = new ArrayList<>();
        for (int i = 0; i < 2; i++) players.add(new Player("Player" + (i+1)));

        // lobby + game
        con.createLobby(2, players.getFirst());
        con.joinLobby(players.get(1), 0);
        Game game = con.getGh().getGame(0);

        // starter cards
        con.giveStarterCards(game.getGameID());
        for(Player p : players) con.chooseCardSide(p, CardSide.FRONT);

        // hand setup
        ArrayList<ResourceCard> resourceCards = CardsPreset.getResourceCards();
        ArrayList<GoldenCard> goldenCards = CardsPreset.getGoldenCards();
        players.get(0).getHand().addCard(resourceCards.get(0));
        players.get(0).getHand().addCard(resourceCards.get(1));
        players.get(0).getHand().addCard(goldenCards.get(0));
        players.get(1).getHand().addCard(resourceCards.get(2));
        players.get(1).getHand().addCard(resourceCards.get(3));
        players.get(1).getHand().addCard(goldenCards.get(1));

        // card placement, one resource and one golden for each player
        con.playCard(0, game.getPlayers().get(0), 0, new Coords(1,0));
        con.drawCard(0, game.getPlayers().get(0), DeckType.RESOURCE);
        con.nextTurn(0);
        con.playCard(0, game.getPlayers().get(1), 0, new Coords(0,1));
        con.drawCard(0, game.getPlayers().get(1), DeckType.RESOURCE);
        con.nextTurn(0);
        con.playCard(0, game.getPlayers().get(0), 1, new Coords(-1,0));
        con.drawCard(0, game.getPlayers().get(0), DeckType.RESOURCE);
        con.nextTurn(0);
        con.playCard(0, game.getPlayers().get(1), 1, new Coords(0,-1));
    }

    @Test
    public void winnerTest() throws FullLobbyException, LobbyDoesNotExistsException, NicknameAlreadyTakenException, GameDoesNotExistException, IOException, IllegalMoveException {
        GameHandler gh = new GameHandler();
        Controller c = new Controller(gh);

        // Game creation + SetUp:
        ArrayList<Player> players = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            Player p = new Player("Player" + (i));
            players.add(p);
        }

        c.createLobby(3, players.getFirst());
        for (int i = 1; i < 3; i++) c.joinLobby(players.get(i), 0);

        c.setGameArea(0);
        c.giveStarterCards(0);
        //Create goals
        ArrayList<ItemBox> resourceList = new ArrayList<>();
        resourceList.add(Resource.QUILL);
        resourceList.add(Resource.QUILL);
        Goal goal0 = new ResourceGoal(15, resourceList, 2);
        Goal goal1 = new DiagonalGoal(0, 2, Kingdom.FUNGI, DiagonalGoalType.UPWARD);
        Goal goal2 = new L_ShapeGoal(4, 3, Kingdom.FUNGI, Kingdom.PLANT, L_ShapeGoalType.DOWN_RIGHT);
        Player player0 = gh.getGame(0).getPlayers().get(0);
        Player player1 = gh.getGame(0).getPlayers().get(1);
        Player player2 = gh.getGame(0).getPlayers().get(2);
        player0.setPrivateGoal(goal0);
        player1.setPrivateGoal(goal1);
        player2.setPrivateGoal(goal2);
        //setting common goals
        gh.getGame(0).setCommonGoal1(goal2);
        gh.getGame(0).setCommonGoal2(goal2);
        // Starter card choosing:
        for (Player p : players) c.chooseCardSide(p, CardSide.FRONT);
        // Adding cards to the field to satisfy the private goal
        ArrayList<ResourceCard> cards = CardsPreset.getResourceCards();
        player0.getField().addCard(cards.get(4), new Coords(1, 0));
        player0.getField().addCard(cards.get(14), new Coords(0, 1));
        player1.getField().addCard(cards.get(0), new Coords(0, -1));
        player1.getField().addCard(cards.get(1), new Coords(-1, -1));
        player1.getField().addCard(cards.get(2), new Coords(1, -1));
        player2.getField().addCard(cards.get(3), new Coords(0, -1));
        player2.getField().addCard(cards.get(7), new Coords(0, -2));
        player2.getField().addCard(cards.get(6), new Coords(-1, -2));
        player2.getField().addCard(cards.get(15), new Coords(-1, -3));
        ArrayList<Player> winners = c.winner(0);
        assertNotNull(winners);
        assertSame(winners.getFirst(), player2);
        // player0's field contains their private goal so their score should be equal to the points of their private goal
        assertEquals(goal0.getPoints(), gh.getGame(0).getPlayerScore(player0));
        // player1's field contains their private goal so their score should be equal to the points of their private goal
        assertEquals(goal1.getPoints(), gh.getGame(0).getPlayerScore(player1));
        // since for the test the common goals are the same of player2 private goal (not possible in the real game), player2 score should be three times the points of his private goal (one time for the private one, two times for the common ones)
        assertEquals(3 * goal2.getPoints(), gh.getGame(0).getPlayerScore(player2));
    }
}