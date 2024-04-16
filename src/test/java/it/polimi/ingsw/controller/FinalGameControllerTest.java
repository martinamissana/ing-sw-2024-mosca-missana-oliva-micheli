package it.polimi.ingsw.controller;


import it.polimi.ingsw.model.card.ResourceCard;
import it.polimi.ingsw.model.card.StarterCard;
import it.polimi.ingsw.model.commonItem.ItemBox;
import it.polimi.ingsw.model.commonItem.Kingdom;
import it.polimi.ingsw.model.commonItem.Resource;
import it.polimi.ingsw.model.exceptions.IllegalMoveException;
import it.polimi.ingsw.model.game.CardsPreset;
import it.polimi.ingsw.model.game.Game;
import it.polimi.ingsw.model.goal.*;
import it.polimi.ingsw.model.player.Coords;
import it.polimi.ingsw.model.player.Pawn;
import it.polimi.ingsw.model.player.Player;
import junit.framework.TestCase;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class FinalGameControllerTest extends TestCase {

    public void testEvaluatePrivateGoal() throws IOException, IllegalMoveException {
        FinalGameController controller=new FinalGameController();
        HashMap<Integer, Player> players=new HashMap<>();
        HashMap<Player,Integer> scoreboard=new HashMap<>();
        ArrayList<ItemBox> resourceList= new ArrayList<>();
        resourceList.add(Resource.QUILL);
        resourceList.add(Resource.QUILL);
        //one goal for each type to test
        Goal goal1=new ResourceGoal(15,resourceList,2);
        Goal goal2=new DiagonalGoal(0,2,Kingdom.FUNGI, DiagonalGoalType.UPWARD);
        Goal goal3=new L_ShapeGoal(4,3,Kingdom.FUNGI,Kingdom.PLANT,L_ShapeGoalType.DOWN_RIGHT);
        //three players
        Player player0=new Player("giorgio",true, Pawn.BLUE);
        Player player1=new Player("anna",false,Pawn.GREEN);
        Player player2=new Player("elisa",false,Pawn.YELLOW);
        //set the private goals to the players
        player0.setPrivateGoal(goal1);
        player1.setPrivateGoal(goal2);
        player2.setPrivateGoal(goal3);
        ArrayList<StarterCard> starterCards=CardsPreset.getStarterCards();
        ArrayList<ResourceCard> cards= CardsPreset.getResourceCards();
        //put specific cards in the field to complete the resource goal
        player0.getField().addCard(starterCards.get(0));
        player0.getField().addCard(cards.get(4),new Coords(1,0));
        player0.getField().addCard(cards.get(14),new Coords(0,1));
        //put specific cards in the field to complete the diagonal goal
        player1.getField().addCard(starterCards.get(0));
        player1.getField().addCard(cards.get(0),new Coords(0,-1));
        player1.getField().addCard(cards.get(1),new Coords(-1,-1));
        player1.getField().addCard(cards.get(2),new Coords(1,-1));
        //put specific cards in the field to complete the L_shape goal
        player2.getField().addCard(starterCards.get(0));
        player2.getField().addCard(cards.get(3),new Coords(0,-1));
        player2.getField().addCard(cards.get(7),new Coords(0,-2));
        player2.getField().addCard(cards.get(6),new Coords(-1,-2));
        player2.getField().addCard(cards.get(15),new Coords(-1,-3));
        //game with the three players
        players.put(0,player0);
        players.put(1,player1);
        players.put(2,player2);
        scoreboard.put(player0,0);
        scoreboard.put(player1,0);
        scoreboard.put(player2,0);
        Game game=new Game(0,3,players,scoreboard);
        //evaluate the players private goal
        controller.evaluatePrivateGoal(game);
        //every player should have the points of their private goal, since their fields contain their private goal
        assertEquals(goal1.getPoints(),game.getPlayerScore(player0));
        assertEquals(goal2.getPoints(),game.getPlayerScore(player1));
        assertEquals(goal3.getPoints(),game.getPlayerScore(player2));
    }

    public void testEvaluateCommonGoal() throws IOException, IllegalMoveException {
        FinalGameController controller=new FinalGameController();
        HashMap<Integer, Player> players=new HashMap<>();
        HashMap<Player,Integer> scoreboard=new HashMap<>();
        ArrayList<ItemBox> resourceList= new ArrayList<>();
        resourceList.add(Resource.QUILL);
        resourceList.add(Resource.QUILL);
        //one goal for each type to test
        Goal goal1=new ResourceGoal(15,resourceList,2);
        Goal goal2=new DiagonalGoal(0,2, Kingdom.FUNGI, DiagonalGoalType.UPWARD);
        Goal goal3=new L_ShapeGoal(4,3,Kingdom.FUNGI,Kingdom.PLANT,L_ShapeGoalType.DOWN_RIGHT);
        //three players
        Player player0=new Player("giorgio",true, Pawn.BLUE);
        Player player1=new Player("anna",false,Pawn.GREEN);
        Player player2=new Player("elisa",false,Pawn.YELLOW);
        ArrayList<StarterCard> starterCards=CardsPreset.getStarterCards();
        ArrayList<ResourceCard> cards= CardsPreset.getResourceCards();
        //cards to complete the resource goal
        player0.getField().addCard(starterCards.get(0));
        player0.getField().addCard(cards.get(4),new Coords(1,0));
        player0.getField().addCard(cards.get(14),new Coords(0,1));
        //cards to complete the diagonal goal
        player1.getField().addCard(starterCards.get(0));
        player1.getField().addCard(cards.get(0),new Coords(0,-1));
        player1.getField().addCard(cards.get(1),new Coords(-1,-1));
        player1.getField().addCard(cards.get(2),new Coords(1,-1));
        //cards to complete the L_Shape goal
        player2.getField().addCard(starterCards.get(0));
        player2.getField().addCard(cards.get(3),new Coords(0,-1));
        player2.getField().addCard(cards.get(7),new Coords(0,-2));
        player2.getField().addCard(cards.get(6),new Coords(-1,-2));
        player2.getField().addCard(cards.get(15),new Coords(-1,-3));
        //initialize game
        players.put(0,player0);
        players.put(1,player1);
        players.put(2,player2);
        scoreboard.put(player0,0);
        scoreboard.put(player1,0);
        scoreboard.put(player2,0);
        Game game=new Game(0,3,players,scoreboard);
        game.setCommonGoal1(goal1);
        game.setCommonGoal2(goal2);
        //evaluate the common goals
        controller.evaluateCommonGoal(game);
        assertEquals(goal1.getPoints(),game.getPlayerScore(player0));
        assertEquals(goal2.getPoints(),game.getPlayerScore(player1));
        assertEquals(0,game.getPlayerScore(player2));
        //third goal
        game.addToScore(player0,-goal1.getPoints());
        game.addToScore(player1,-goal2.getPoints());
        game.setCommonGoal1(goal3);
        controller.evaluateCommonGoal(game);
        assertEquals(0,game.getPlayerScore(player0));
        assertEquals(goal2.getPoints(),game.getPlayerScore(player1));
        assertEquals(goal3.getPoints(),game.getPlayerScore(player2));
    }

    public void testWinner() throws IOException {
        FinalGameController controller=new FinalGameController();
        HashMap<Integer, Player> players=new HashMap<>();
        HashMap<Player,Integer> scoreboard=new HashMap<>();
        Player player0=new Player("giorgio",true, Pawn.BLUE);
        Player player1=new Player("anna",false,Pawn.GREEN);
        Player player2=new Player("elisa",false,Pawn.YELLOW);
        players.put(0,player0);
        players.put(1,player1);
        players.put(2,player2);
        scoreboard.put(player0,10);
        scoreboard.put(player1,3);
        scoreboard.put(player2,0);
        Game game=new Game(0,3,players,scoreboard);
        assertEquals(controller.winner(game),player0 );
    }
}