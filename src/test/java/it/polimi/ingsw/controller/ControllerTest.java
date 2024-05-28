package it.polimi.ingsw.controller;

import it.polimi.ingsw.controller.exceptions.*;
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
import it.polimi.ingsw.model.game.GamePhase;
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
    Player anna, eric, giorgio,sara,paola ,anna1,eric1,giorgio1;
    Controller c=new Controller(gameHandler);

    @Before
    public void setUp() throws IOException, FullLobbyException, NicknameAlreadyTakenException, LobbyDoesNotExistsException, HandIsFullException, CannotJoinMultipleLobbiesException, GameAlreadyStartedException, PawnAlreadyTakenException, GameDoesNotExistException, UnexistentUserException {
        anna=new Player("anna");
        eric=new Player("eric");
        giorgio=new Player("giorgio");
        sara=new Player("sara");
        paola=new Player("paola");
        c.getGh().getUsers().add(anna);
        c.getGh().getUsers().add(eric);
        c.getGh().getUsers().add(giorgio);
        c.getGh().getUsers().add(sara);
        c.getGh().getUsers().add(paola);
        c.createLobby(3,anna.getNickname());
        c.joinLobby(eric.getNickname(),0);
        c.joinLobby(giorgio.getNickname(),0);
        c.choosePawn(0,anna.getNickname(),Pawn.BLUE);
        c.choosePawn(0,eric.getNickname(),Pawn.RED);
        c.choosePawn( 0,giorgio.getNickname(),Pawn.YELLOW);
        anna.getHand().addCard(card);
    }

    @Test (expected = PawnAlreadyTakenException.class)
    public void testGamePhase() throws LobbyDoesNotExistsException, GameDoesNotExistException, FullLobbyException, NicknameAlreadyTakenException, IOException, EmptyDeckException, HandIsFullException, IllegalGoalChosenException, WrongGamePhaseException, GameAlreadyStartedException, PawnAlreadyTakenException, CannotJoinMultipleLobbiesException, UnexistentUserException {
        anna1=new Player("anna1");
        eric1=new Player("eric1");
        giorgio1=new Player("giorgio1");
        c.getGh().getUsers().add(anna1);
        c.getGh().getUsers().add(eric1);
        c.getGh().getUsers().add(giorgio1);

        c.createLobby(3,anna1.getNickname());
        c.joinLobby(eric1.getNickname(),1);


        assertFalse(c.getGh().getActiveGames().containsKey(1));
        c.choosePawn(1,anna1.getNickname(),Pawn.BLUE);
        c.choosePawn(1,eric1.getNickname(),Pawn.BLUE);
        c.choosePawn(1,eric1.getNickname(),Pawn.RED);
        c.joinLobby(giorgio1.getNickname(),1);
        c.choosePawn( 1,giorgio1.getNickname(),Pawn.YELLOW);
        assertTrue(c.getGh().getActiveGames().containsKey(1));

        assertEquals(gameHandler.getGame(1).getGamePhase(), GamePhase.PLACING_STARTER_CARD);
        c.chooseCardSide(1,anna1.getNickname(),CardSide.FRONT);
        c.chooseCardSide(1,eric1.getNickname(),CardSide.BACK);
        c.chooseCardSide(1,giorgio1.getNickname(),CardSide.FRONT);

        assertEquals(gameHandler.getGame(1).getGamePhase(), GamePhase.CHOOSING_SECRET_GOAL);
        c.chooseSecretGoal(1,anna1.getNickname(),anna1.getChoosableGoals().get(1).getGoalID());
        c.chooseSecretGoal(1,eric1.getNickname(),eric1.getChoosableGoals().get(0).getGoalID());
        c.chooseSecretGoal(1,giorgio1.getNickname(),giorgio1.getChoosableGoals().get(1).getGoalID());

        assertEquals(gameHandler.getGame(1).getGamePhase(), GamePhase.PLAYING_GAME);



    }

    @Test
    public void testCreateGameAndTerminateGame() throws GameDoesNotExistException, LobbyDoesNotExistsException, IOException, UnexistentUserException, GameAlreadyStartedException, PawnAlreadyTakenException {
        assertTrue(gameHandler.getGame(0).getPlayers().contains(anna));
        assertTrue(gameHandler.getGame(0).getPlayers().contains(eric));
        assertTrue(gameHandler.getGame(0).getPlayers().contains(giorgio));
        c.terminateGame(0);
        assertTrue(gameHandler.getActiveGames().isEmpty());
        assertTrue(gameHandler.getLobbies().isEmpty());
    }
    @Test (expected = FullLobbyException.class)
    public void testJoinLobbyButLobbyIsFull() throws FullLobbyException, NicknameAlreadyTakenException, LobbyDoesNotExistsException, IOException, GameDoesNotExistException, CannotJoinMultipleLobbiesException, UnexistentUserException {
        c.joinLobby(sara.getNickname(),0);
        assertFalse(gameHandler.getLobby(0).getPlayers().contains(sara));
        assertFalse(gameHandler.getGame(0).getPlayers().contains(sara));
    }
    @Test (expected = LobbyDoesNotExistsException.class)
    public void testJoinLobbyButLobbyDoesNotExist() throws FullLobbyException, NicknameAlreadyTakenException, LobbyDoesNotExistsException, IOException, GameDoesNotExistException, CannotJoinMultipleLobbiesException, UnexistentUserException {
        c.joinLobby(sara.getNickname(),1);
        assertFalse(gameHandler.getLobby(0).getPlayers().contains(sara));
        assertFalse(gameHandler.getGame(0).getPlayers().contains(sara));
    }
    @Test
    public void leaveLobby() throws FullLobbyException, LobbyDoesNotExistsException, IOException, GameAlreadyStartedException, CannotJoinMultipleLobbiesException, GameDoesNotExistException, UnexistentUserException {
        c.createLobby(3, sara.getNickname());
        c.joinLobby(paola.getNickname(),1);
        c.leaveLobby(sara.getNickname(), 1);
        assertTrue(gameHandler.getLobbies().containsKey(1));
        c.leaveLobby(paola.getNickname(), 1);
        assertFalse(gameHandler.getLobbies().containsKey(1));
    }
    @Test
    public void SetUpTest() throws IOException, HandIsFullException, FullLobbyException, LobbyDoesNotExistsException, NicknameAlreadyTakenException, GameDoesNotExistException, EmptyDeckException, PawnAlreadyTakenException, IllegalGoalChosenException, WrongGamePhaseException, GameAlreadyStartedException, CannotJoinMultipleLobbiesException, UnexistentUserException {
        GameHandler gh = new GameHandler();
        Controller c = new Controller(gh);

        // Game creation + SetUp:
        ArrayList<Player> players = new ArrayList<>();
        for (int i = 0; i < 4; i++) {
            Player p = new Player("Player" + (i+1));
            players.add(p);
            gh.addUser(p);
        }

        c.createLobby(4, players.getFirst().getNickname());
        for (int i = 1; i < 4; i++) c.joinLobby(players.get(i).getNickname(), 0);

        // Pawn choosing:
        int j = 0;

        for(Player p : players) {
            c.choosePawn(0, p.getNickname(), Pawn.values()[j]);
            j++;
        }

        // Starter card choosing:
        for(Player p : players) c.chooseCardSide(0,p.getNickname(), CardSide.FRONT);

        for (Player p : players) {
            c.chooseSecretGoal(0,p.getNickname(), p.getChoosableGoals().getFirst().getGoalID());
        }

        // Player + Pawn + Hand printing:
        for (Player p : players) {
            System.out.print(p.getNickname() + " (" + p.getPawn().name() + " - " + gh.getGame(0).getScoreboard().get(p) + "): ");
            for(int i = 0; i < 3; i++) {
                System.out.print("[" + p.getHand().getCard(i).getCardID() + "]");
            }
            System.out.println("   goal: (" + p.getSecretGoal().getGoalID() + ")");
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
    public void FlipCardTest() throws IOException, FullLobbyException, LobbyDoesNotExistsException, NicknameAlreadyTakenException, GameDoesNotExistException, HandIsFullException, CannotJoinMultipleLobbiesException, UnexistentUserException, GameAlreadyStartedException, PawnAlreadyTakenException, WrongGamePhaseException, EmptyDeckException, IllegalGoalChosenException {
        GameHandler gh = new GameHandler();
        Controller con = new Controller(gh);

        // players
        ArrayList<Player> players = new ArrayList<>();
        for (int i = 0; i < 2; i++) {
            Player p = new Player("Player" + (i+1));
            players.add(p);
            con.getGh().addUser(p);
        }

        // lobby + game
        con.createLobby(2, players.getFirst().getNickname());
        con.joinLobby(players.get(1).getNickname(), 0);
        con.choosePawn(0,players.get(0).getNickname(),Pawn.RED);
        con.choosePawn(0,players.get(1).getNickname(),Pawn.BLUE);
        Game game = con.getGh().getGame(0);

        // hand setup
        ArrayList<ResourceCard> resourceCards = CardsPreset.getResourceCards();
        ArrayList<GoldenCard> goldenCards = CardsPreset.getGoldenCards();

        con.chooseCardSide(game.getGameID(),players.get(0).getNickname(),CardSide.FRONT);
        con.chooseCardSide(game.getGameID(),players.get(1).getNickname(),CardSide.FRONT);

        con.chooseSecretGoal(0,players.get(0).getNickname(),players.get(0).getChoosableGoals().getFirst().getGoalID());
        con.chooseSecretGoal(0,players.get(1).getNickname(),players.get(1).getChoosableGoals().getLast().getGoalID());

        // card flipping
        con.flipCard(0, game.getPlayers().get(0).getNickname(), 0);
        assert(CardSide.BACK == game.getPlayers().get(0).getHand().getCard(0).getSide());
        con.flipCard(0, game.getPlayers().get(0).getNickname(), 1);
        assert(CardSide.BACK == game.getPlayers().get(0).getHand().getCard(1).getSide());
        con.flipCard(0, game.getPlayers().get(0).getNickname(), 2);
        assert(CardSide.BACK == game.getPlayers().get(0).getHand().getCard(2).getSide());
        con.flipCard(0, game.getPlayers().get(1).getNickname(), 0);
        assert(CardSide.BACK == game.getPlayers().get(1).getHand().getCard(0).getSide());
        con.flipCard(0, game.getPlayers().get(1).getNickname(), 1);
        assert(CardSide.BACK == game.getPlayers().get(1).getHand().getCard(1).getSide());
        con.flipCard(0, game.getPlayers().get(1).getNickname(), 2);
        assert(CardSide.BACK == game.getPlayers().get(1).getHand().getCard(2).getSide());
    }

    @Test
    public void PlayDrawTurnTest() throws FullLobbyException, LobbyDoesNotExistsException, NicknameAlreadyTakenException, IOException, GameDoesNotExistException, PawnAlreadyTakenException, EmptyDeckException, HandIsFullException, IllegalActionException, NotYourTurnException, IllegalMoveException, EmptyBufferException, CannotJoinMultipleLobbiesException, GameAlreadyStartedException, UnexistentUserException, WrongGamePhaseException {
        GameHandler gh = new GameHandler();
        Controller con = new Controller(gh);

        // players
        ArrayList<Player> players = new ArrayList<>();
        for (int i = 0; i < 2; i++){
            Player p = new Player("Player" + (i+1));
            players.add(p);
            gh.addUser(p);
        }

        // lobby + game
        con.createLobby(2, players.getFirst().getNickname());
        con.joinLobby(players.get(1).getNickname(), 0);
        con.choosePawn(0,players.get(0).getNickname(),Pawn.BLUE);
        con.choosePawn(0,players.get(1).getNickname(),Pawn.RED);

        Game game = con.getGh().getGame(0);

        // starter cards
        for(Player p : players) con.chooseCardSide(0,p.getNickname(), CardSide.FRONT);

        // hand setup
        ArrayList<ResourceCard> resourceCards = CardsPreset.getResourceCards();
        ArrayList<GoldenCard> goldenCards = CardsPreset.getGoldenCards();

        players.get(0).getHand().removeAllCards();
        players.get(1).getHand().removeAllCards();
        players.get(0).getHand().addCard(resourceCards.get(0));
        players.get(0).getHand().addCard(resourceCards.get(1));
        players.get(0).getHand().addCard(goldenCards.get(0));
        players.get(1).getHand().addCard(resourceCards.get(2));
        players.get(1).getHand().addCard(resourceCards.get(3));
        players.get(1).getHand().addCard(goldenCards.get(1));

        // card placement, one resource and one golden for each player
        con.playCard(0, game.getPlayers().get(0).getNickname(), 0, new Coords(1,0));
        con.drawCard(0, game.getPlayers().get(0).getNickname(), DeckType.RESOURCE);
        con.playCard(0, game.getPlayers().get(1).getNickname(), 0, new Coords(0,1));
        con.drawCard(0, game.getPlayers().get(1).getNickname(), DeckType.RESOURCE);
        con.playCard(0, game.getPlayers().get(0).getNickname(), 1, new Coords(-1,0));
        con.drawCard(0, game.getPlayers().get(0).getNickname(), DeckType.RESOURCE);
        con.playCard(0, game.getPlayers().get(1).getNickname(), 1, new Coords(0,-1));
    }

    @Test
    public void winnerTest() throws FullLobbyException, LobbyDoesNotExistsException, NicknameAlreadyTakenException, GameDoesNotExistException, IOException, IllegalMoveException, EmptyDeckException, HandIsFullException, CannotJoinMultipleLobbiesException, UnexistentUserException, WrongGamePhaseException, GameAlreadyStartedException, PawnAlreadyTakenException {
        GameHandler gh = new GameHandler();
        Controller c = new Controller(gh);

        // Game creation + SetUp:
        ArrayList<Player> players = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            Player p = new Player("Player" + (i));
            players.add(p);
            gh.getUsers().add(p);
        }

        c.createLobby(3, players.getFirst().getNickname());
        for (int i = 1; i < 3; i++) {
            c.joinLobby(players.get(i).getNickname(), 0);
        }
        int i=0;
        for (Pawn pawn: Pawn.values()){
             c.choosePawn(0, players.get(i).getNickname(),pawn);
             i++;
             if(i==3)break;
        }

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
        player0.setSecretGoal(goal0);
        player1.setSecretGoal(goal1);
        player2.setSecretGoal(goal2);
        //setting common goals
        gh.getGame(0).setCommonGoal1(goal2);
        gh.getGame(0).setCommonGoal2(goal2);
        // Starter card choosing:
        for (Player p : players) c.chooseCardSide(0,p.getNickname(), CardSide.FRONT);
        // Adding cards to the field to satisfy the secret goal
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
        c.winner(0);
        assertNotNull(gh.getGame(0).getWinners());
        assertSame(gh.getGame(0).getWinners().getFirst(), player2);
        // player0's field contains their secret goal so their score should be equal to the points of their secret goal
        assertEquals(goal0.getPoints(), gh.getGame(0).getPlayerScore(player0));
        // player1's field contains their secret goal so their score should be equal to the points of their secret goal
        assertEquals(goal1.getPoints(), gh.getGame(0).getPlayerScore(player1));
        // since for the test the common goals are the same of player2 secret goal (not possible in the real game), player2 score should be three times the points of his secret goal (one time for the secret one, two times for the common ones)
        assertEquals(3 * goal2.getPoints(), gh.getGame(0).getPlayerScore(player2));
    }
}