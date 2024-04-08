package it.polimi.ingsw.controller;

import it.polimi.ingsw.model.card.Card;
import it.polimi.ingsw.model.commonItem.ItemBox;
import it.polimi.ingsw.model.game.Game;
import it.polimi.ingsw.model.goal.*;
import it.polimi.ingsw.model.player.Coords;
import it.polimi.ingsw.model.player.Player;

import java.util.*;

public class FinalGameController {

    /**
     * checks in the field the private goal of the players and adds the points to the scoreboard
     * @param game
     */
    public void evaluatePrivateGoal(Game game){
        for(Player p:game.getPlayers().values()){
            if(p.getPrivateGoal().getClass()==ResourceGoal.class){
                resourceEvaluator(game, (ResourceGoal) p.getPrivateGoal(),p);
            } else if (p.getPrivateGoal().getClass()==L_ShapeGoal.class) {
                L_ShapeEvaluator(game, (L_ShapeGoal) p.getPrivateGoal(),p);
            }
            else{
                diagonalEvaluator(game, (DiagonalGoal) p.getPrivateGoal(),p);
            }

        }
    }

    /**
     * checks in the field of every player the common goals and adds the points to the scoreboard
     * @param game
     */
    public void evaluateCommonGoal(Game game){
        Goal commonGoal1= game.getCommonGoal1();
        Goal commonGoal2= game.getCommonGoal2();
        for(Player p: game.getPlayers().values()){
            if(commonGoal1.getClass()==ResourceGoal.class){
                resourceEvaluator(game, (ResourceGoal) commonGoal1,p);
            } else if (commonGoal1.getClass()==L_ShapeGoal.class) {
                L_ShapeEvaluator(game, (L_ShapeGoal) commonGoal1,p);
            }
            else{
                diagonalEvaluator(game, (DiagonalGoal) commonGoal1,p);
            }
            if(commonGoal2.getClass()==ResourceGoal.class){
                resourceEvaluator(game, (ResourceGoal) commonGoal2,p);
            } else if (commonGoal2.getClass()==L_ShapeGoal.class) {
                L_ShapeEvaluator(game, (L_ShapeGoal) commonGoal2,p);
            }
            else{
                diagonalEvaluator(game, (DiagonalGoal) commonGoal2,p);
            }
        }
    }

    /**
     * returns the winner of the game finding the maximum points in the scoreboard
     * @param game
     * @return Player
     */
    public Player winner(Game game){
        HashMap<Player,Integer> scoreboard=game.getScoreboard();
        Optional<Map.Entry<Player, Integer>> maxEntry = scoreboard.entrySet().stream()
                .max(Comparator.comparing(Map.Entry::getValue));
        return maxEntry.get().getKey();
    }

    /**
     * checks the presence of the Diagonal goal in the field
     * @param game
     * @param goal
     * @param player
     */
    private void diagonalEvaluator(Game game,DiagonalGoal goal,Player player) {
        HashMap<Coords, Card> field = player.getField().getMatrix();
        HashSet<Coords> done = new HashSet<>(); //indicates the cards already used for the goal
        if (goal.getType() == DiagonalGoalType.UPWARD) {
            for (Coords coords : field.keySet()) {
                Coords secondCard = new Coords(coords.getX() + 1, coords.getY());
                Coords thirdCard = new Coords(coords.getX() + 2, coords.getY());
                if (!done.contains(coords) && field.get(coords).getKingdom() == goal.getColor()&&field.containsKey(secondCard) && field.containsKey(thirdCard) && field.get(secondCard).getKingdom() == goal.getColor() && field.get(thirdCard).getKingdom() == goal.getColor()) {
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
                if (!done.contains(coords) && field.get(coords).getKingdom() == goal.getColor()&&field.containsKey(secondCard) && field.containsKey(thirdCard) && field.get(secondCard).getKingdom() == goal.getColor() && field.get(thirdCard).getKingdom() == goal.getColor()) {
                    done.add(coords);
                    done.add(secondCard);
                    done.add(thirdCard);
                    game.addToScore(player, goal.getPoints());
                }
            }
        }
    }
    private void resourceEvaluator(Game game,ResourceGoal goal,Player player){
            HashMap<ItemBox, Integer> totalResources = player.getField().getTotalResources();
            while(totalResources.values().stream().noneMatch(count -> count == 0)){
                for (ItemBox item : goal.getResourceList()) {
                    totalResources.put(item, totalResources.get(item)-1);
                }
                game.addToScore(player, goal.getPoints());
            }
    }

    private void L_ShapeEvaluator(Game game,L_ShapeGoal goal,Player player){
        HashSet<Coords> done=new HashSet<>();
        HashMap<Coords, Card> field=player.getField().getMatrix();
        switch (goal.getType()){
            case UP_RIGHT -> {
                for(Coords coords: field.keySet()){
                       Coords secondCard=new Coords(coords.getX(),coords.getY()-1);
                       Coords thirdCard=new Coords(coords.getX()+1,coords.getY()-2);
                       genericL_ShapeEvaluator(game,goal,player,coords,secondCard,thirdCard,done);
                }
            }
            case UP_LEFT -> {
                for(Coords coords: field.keySet()){
                        Coords secondCard=new Coords(coords.getX()+1,coords.getY());
                        Coords thirdCard=new Coords(coords.getX()+2,coords.getY()-1);
                        genericL_ShapeEvaluator(game,goal,player,coords,secondCard,thirdCard,done);
                }
            }
            case DOWN_LEFT -> {
                for(Coords coords: field.keySet()){
                    Coords secondCard=new Coords(coords.getX(),coords.getY()+1);
                    Coords thirdCard=new Coords(coords.getX()-1,coords.getY()+2);
                    genericL_ShapeEvaluator(game,goal,player,coords,secondCard,thirdCard,done);
                }
            }
            case DOWN_RIGHT -> {
                for(Coords coords: field.keySet()){
                    Coords secondCard=new Coords(coords.getX()-1,coords.getY());
                    Coords thirdCard=new Coords(coords.getX()-2,coords.getY()+1);
                    genericL_ShapeEvaluator(game,goal,player,coords,secondCard,thirdCard,done);
                }
            }
        }
    }

    private void genericL_ShapeEvaluator(Game game,L_ShapeGoal goal,Player player,Coords firstCard,Coords secondCard,Coords thirdCard,HashSet<Coords> done){
        HashMap<Coords, Card> field=player.getField().getMatrix();
        if(!done.contains(firstCard)&&field.get(firstCard).getKingdom()==goal.getSecondaryColor()&&field.containsKey(secondCard)&&field.containsKey(thirdCard)&&field.get(secondCard).getKingdom()==goal.getMainColor()&&field.get(thirdCard).getKingdom()==goal.getMainColor()){
            done.add(firstCard);
            done.add(secondCard);
            done.add(thirdCard);
            game.addToScore(player, goal.getPoints());
        }
    }
}
