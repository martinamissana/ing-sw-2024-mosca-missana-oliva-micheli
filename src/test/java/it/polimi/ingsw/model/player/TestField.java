package it.polimi.ingsw.model.player;

import it.polimi.ingsw.model.card.GoldenCard;
import it.polimi.ingsw.model.card.ResourceCard;
import it.polimi.ingsw.model.card.StarterCard;
import it.polimi.ingsw.model.exceptions.IllegalMoveException;
import it.polimi.ingsw.model.exceptions.OccupiedCoordsException;
import it.polimi.ingsw.model.exceptions.UnreachablePositionException;
import it.polimi.ingsw.model.game.CardsPreset;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TestField {

    @Test
    public void testAddCard() throws IOException, IllegalMoveException {
        ArrayList<StarterCard> starterCards = CardsPreset.getStarterCards();
        ArrayList<ResourceCard> resourceCards = CardsPreset.getResourceCards();
        ArrayList<GoldenCard> goldenCards = CardsPreset.getGoldenCards();
        Field field = new Field();

        field.addCard(starterCards.get(0));
        assertEquals(starterCards.get(0), field.getMatrix().get(new Coords(0, 0)));

        field.addCard(resourceCards.get(0), new Coords(1,0));
        assertEquals(resourceCards.get(0), field.getMatrix().get(new Coords(1, 0)));
        assertEquals(starterCards.get(0), field.getMatrix().get(new Coords(0, 0)));
        assertEquals(field.getCardBlock(), field.getMatrix().get(new Coords(1, -1)));

        try {
            field.addCard(resourceCards.get(0), new Coords(1, -1));
        } catch (OccupiedCoordsException e) {
            System.out.println("correct throw");
        }

        try {
            field.addCard(resourceCards.get(3), new Coords(3, -1));
            field.addCard(resourceCards.get(3), new Coords(-2, -1));
        } catch (UnreachablePositionException e) {
            System.out.println("correct throw");
        }

        field.addCard(resourceCards.get(1), new Coords(0,-1));
        assertEquals(field.getCardBlock(), field.getMatrix().get(new Coords(1,-1)));

        field.addCard(resourceCards.get(21), new Coords(2,0));
        field.addCard(goldenCards.get(0), new Coords(-1,0));
        assertEquals(goldenCards.get(0), field.getMatrix().get(new Coords(-1,0)));
    }

    @Test
    public void testAddCard2() throws IOException, IllegalMoveException {
        ArrayList<StarterCard> starterCards = CardsPreset.getStarterCards();
        ArrayList<ResourceCard> resourceCards = CardsPreset.getResourceCards();
        ArrayList<GoldenCard> goldenCards = CardsPreset.getGoldenCards();
        Field field = new Field();
        System.out.printf(field.toString());
        field.addCard(starterCards.get(0));
        System.out.printf(field.toString());
        field.addCard(resourceCards.get(0), new Coords(0,1));
        System.out.printf(field.toString());
        field.addCard(goldenCards.get(0), new Coords(0,-1));
        System.out.printf(field.toString());
    }
}