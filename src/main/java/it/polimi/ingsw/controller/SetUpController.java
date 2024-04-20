package it.polimi.ingsw.controller;

import it.polimi.ingsw.model.card.*;
import it.polimi.ingsw.model.deck.DeckBufferType;
import it.polimi.ingsw.model.exceptions.*;
import it.polimi.ingsw.model.game.*;
import it.polimi.ingsw.model.goal.*;
import it.polimi.ingsw.model.player.*;

import java.io.IOException;
import java.util.*;

public class SetUpController {
    private final Game g;

    public SetUpController(Game g) {
        this.g = g;
    }

    public Game getGame() {
        return g;
    }

    public void setGameArea() throws IOException, HandIsFullException, EmptyDeckException {
        // setting decks and deck buffers:
        g.getResourceDeck().shuffle();
        g.getGoldenDeck().shuffle();
        for(DeckBufferType type : DeckBufferType.values()) g.getDeckBuffer(type).refill();

        ArrayList<Player> players = g.getPlayers();

        // Creating starter deck:
        ArrayList<StarterCard> starter = CardsPreset.getStarterCards();
        Collections.shuffle(starter);

        // Initializing fields + Drawing and placing starter card for each player:
        for (Player p : players) {
            p.getHand().addCard(starter.removeLast());
            // p has to choose card side before playCard(p);
            playCard(p);
        }

        // Choose pawns: (for now it's something not choose-able)
        Pawn[] pawns = Pawn.values();
        int i = 0;

        for (Player p : players) {
            p.setPawn(pawns[i]);
            i++;
        }

        // Fill hand of players:
        for (Player p : players) {
            p.getHand().addCard(g.getResourceDeck().draw());
            p.getHand().addCard(g.getResourceDeck().draw());
            p.getHand().addCard(g.getGoldenDeck().draw());
        }

        // Global goals:
        GoalBuffer goals = new GoalBuffer();
        g.setCommonGoal1(goals.getgoal());
        g.setCommonGoal2(goals.getgoal());

        // Choosing personal goals:
        for (Player p : players) {
            Goal[] choices = new Goal[2];
            choices[0] = goals.getgoal();
            choices[1] = goals.getgoal();

            int x = 0;  // Let the player choose a personal goal (not implemented yet)
            p.setPrivateGoal(choices[x]);
        }
    }

    public void playCard(Player p) {
        StarterCard card = (StarterCard) p.getHand().getCard(0);
        // card.flip();     // error in test ("oldQty is null in player.Field.updateTotalRes")
        p.getField().addCard(card);
        p.getHand().removeCard(card);
    }
}
