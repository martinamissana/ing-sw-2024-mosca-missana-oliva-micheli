package it.polimi.ingsw.controller;

import it.polimi.ingsw.model.card.Card;
import it.polimi.ingsw.model.commonItem.ItemBox;
import it.polimi.ingsw.model.game.Game;
import it.polimi.ingsw.model.game.GameHandler;
import it.polimi.ingsw.model.goal.*;
import it.polimi.ingsw.model.player.Coords;
import it.polimi.ingsw.model.player.Player;

import java.util.*;

public class FinalGameController {
    private GameHandler gh;

    /** Class constructor
     * @param gh game handler that contains all the matches
     */
    public FinalGameController(GameHandler gh) {
        this.gh = gh;
    }

    /**
     * checks in the field the private goal of the players and adds the points to the scoreboard
     * @param gameID index of the game where the evaluation is done
     */
   private  void evaluatePrivateGoal(Integer gameID){
       Game game=gh.getActiveGames().get(gameID);
        for(Player p:game.getPlayers()){
            if(p.getPrivateGoal().getClass()==ResourceGoal.class){
                resourceEvaluator(gameID, (ResourceGoal) p.getPrivateGoal(),p);
            } else if (p.getPrivateGoal().getClass()==L_ShapeGoal.class) {
                L_ShapeEvaluator(gameID, (L_ShapeGoal) p.getPrivateGoal(),p);
            }
            else{
                diagonalEvaluator(gameID, (DiagonalGoal) p.getPrivateGoal(),p);
            }

        }
    }

    /**
     * checks in the field of every player the common goals and adds the points to the scoreboard
     * @param gameID index of the game where the evaluation is done
     */
    private void evaluateCommonGoal(Integer gameID){
        Game game=gh.getActiveGames().get(gameID);
        Goal commonGoal1= game.getCommonGoal1();
        Goal commonGoal2= game.getCommonGoal2();
        for(Player p: game.getPlayers()){
            if(commonGoal1.getClass()==ResourceGoal.class){
                resourceEvaluator(gameID, (ResourceGoal) commonGoal1,p);
            } else if (commonGoal1.getClass()==L_ShapeGoal.class) {
                L_ShapeEvaluator(gameID, (L_ShapeGoal) commonGoal1,p);
            }
            else{
                diagonalEvaluator(gameID, (DiagonalGoal) commonGoal1,p);
            }
            if(commonGoal2.getClass()==ResourceGoal.class){
                resourceEvaluator(gameID, (ResourceGoal) commonGoal2,p);
            } else if (commonGoal2.getClass()==L_ShapeGoal.class) {
                L_ShapeEvaluator(gameID, (L_ShapeGoal) commonGoal2,p);
            }
            else{
                diagonalEvaluator(gameID, (DiagonalGoal) commonGoal2,p);
            }
        }
    }

    /**
     * returns the list of winners (players with the highest score) of the game after the evaluation of the private and the common goals
     * @param gameID index of the game where the evaluation is done
     * @return ArrayList<Player>
     */
    public ArrayList<Player> winner(Integer gameID){
        Game game=gh.getActiveGames().get(gameID);
        evaluatePrivateGoal(gameID);
        evaluateCommonGoal(gameID);
        HashMap<Player,Integer> scoreboard=game.getScoreboard();
        ArrayList<Player> winners=new ArrayList<>();
        int maxValue = 0;
        for (Map.Entry<Player, Integer> entry : scoreboard.entrySet()) {
            if (entry.getValue() > maxValue) {
                maxValue = entry.getValue();
            }
        }
        for(Player p:scoreboard.keySet()){
            if(scoreboard.get(p)==maxValue) winners.add(p);
        }
        return winners;
    }

    /**
     * checks the presence of the Diagonal goal in the field and adds the points to the scoreboard
     * @param gameID index of the game where the evaluation is done
     * @param goal goal that needs to be found in the field
     * @param player player who has to complete the goal
     */
    private void diagonalEvaluator(Integer gameID,DiagonalGoal goal,Player player) {
        Game game=gh.getActiveGames().get(gameID);
        HashMap<Coords, Card> field = player.getField().getMatrix();
        HashSet<Coords> done = new HashSet<>(); //indicates the cards already used for the goal
        if (goal.getType() == DiagonalGoalType.UPWARD) {
            for (Coords coords : field.keySet()) {
                Coords secondCard = new Coords(coords.getX() + 1, coords.getY());
                Coords thirdCard = new Coords(coords.getX() + 2, coords.getY());
                if (!done.contains(coords)&& !done.contains(secondCard)&&!done.contains(thirdCard) && field.get(coords).getKingdom() == goal.getColor()&&field.containsKey(secondCard) && field.containsKey(thirdCard) && field.get(secondCard).getKingdom() == goal.getColor() && field.get(thirdCard).getKingdom() == goal.getColor()) {
                    done.add(coords);
                    done.add(secondCard);
                    done.add(thirdCard);
                    game.addToScore(player, goal.getPoints());
                }
            }
        } else {
            for (Coords coords : field.keySet()) {
                Coords secondCard = new Coords(coords.getX(), coords.getY()+1);
                Coords thirdCard = new Coords(coords.getX(),coords.getY()+2);
                if (!done.contains(coords)&&!done.contains(secondCard)&&!done.contains(thirdCard) && field.get(coords).getKingdom() == goal.getColor()&&field.containsKey(secondCard) && field.containsKey(thirdCard) && field.get(secondCard).getKingdom() == goal.getColor() && field.get(thirdCard).getKingdom() == goal.getColor()) {
                    done.add(coords);
                    done.add(secondCard);
                    done.add(thirdCard);
                    game.addToScore(player, goal.getPoints());
                }
            }
        }
    }

    /**
     * checks the presence of the Resource goal in the field and adds the points to the scoreboard
     * @param gameID index of the game where the evaluation is done
     * @param goal goal that needs to be found in the field
     * @param player player who has to complete the goal
     */
    private void resourceEvaluator(Integer gameID,ResourceGoal goal,Player player){
        Game game=gh.getActiveGames().get(gameID);
            HashMap<ItemBox, Integer> totalResources = player.getField().getTotalResources();
            for (ItemBox item : goal.getResourceList()) {
                if(totalResources.get(item)==0) return;
                totalResources.replace(item,totalResources.get(item)-1);
            }
            game.addToScore(player,goal.getPoints());
    }

    /**
     * checks the presence of the L_Shape goal in the field and adds the points to the scoreboard
     * @param gameID index of the game where the evaluation is done
     * @param goal goal that needs to be found in the field
     * @param player player who has to complete the goal
     */
    private void L_ShapeEvaluator(Integer gameID,L_ShapeGoal goal,Player player){
        HashSet<Coords> done=new HashSet<>();
        HashMap<Coords, Card> field=player.getField().getMatrix();
        switch (goal.getType()){
            case UP_RIGHT -> {
                for(Coords coords: field.keySet()){
                       Coords secondCard=new Coords(coords.getX()-1,coords.getY());
                       Coords thirdCard=new Coords(coords.getX()-2,coords.getY()-1);
                       genericL_ShapeEvaluator(gameID,goal,player,coords,secondCard,thirdCard,done);
                }
            }
            case UP_LEFT -> {
                for(Coords coords: field.keySet()){
                        Coords secondCard=new Coords(coords.getX(),coords.getY()-1);
                        Coords thirdCard=new Coords(coords.getX()-1,coords.getY()-2);
                        genericL_ShapeEvaluator(gameID,goal,player,coords,secondCard,thirdCard,done);
                }
            }
            case DOWN_LEFT -> {
                for(Coords coords: field.keySet()){
                    Coords secondCard=new Coords(coords.getX()+1,coords.getY());
                    Coords thirdCard=new Coords(coords.getX()+2,coords.getY()+1);
                    genericL_ShapeEvaluator(gameID,goal,player,coords,secondCard,thirdCard,done);
                }
            }
            case DOWN_RIGHT -> {
                for(Coords coords: field.keySet()){
                    Coords secondCard=new Coords(coords.getX(),coords.getY()+1);
                    Coords thirdCard=new Coords(coords.getX()+1,coords.getY()+2);
                    genericL_ShapeEvaluator(gameID,goal,player,coords,secondCard,thirdCard,done);
                }
            }
        }
    }

    /**
     * useful method to avoid repetition of code based on the direction of L_Shape goal
     * @param gameID index of the game where the evaluation is done
     * @param goal goal that needs to be found in the field
     * @param player player who has to complete the goal
     * @param firstCard coordinates of the card with a different color from the other two in the L_Shape
     * @param secondCard coordinates of one of the cards with the main L_Shape color
     * @param thirdCard coordinates of one of the cards with the main L_Shape color
     * @param done HashMap that maps the cards already used to complete the goal
     */
    private void genericL_ShapeEvaluator(Integer gameID,L_ShapeGoal goal,Player player,Coords firstCard,Coords secondCard,Coords thirdCard,HashSet<Coords> done){
        Game game=gh.getActiveGames().get(gameID);
        HashMap<Coords, Card> field=player.getField().getMatrix();
        if(!done.contains(firstCard)&&!done.contains(secondCard)&&!done.contains(thirdCard)&&field.get(firstCard).getKingdom()==goal.getSecondaryColor()&&field.containsKey(secondCard)&&field.containsKey(thirdCard)&&field.get(secondCard).getKingdom()==goal.getMainColor()&&field.get(thirdCard).getKingdom()==goal.getMainColor()){
            done.add(firstCard);
            done.add(secondCard);
            done.add(thirdCard);
            game.addToScore(player, goal.getPoints());
        }
    }
}
