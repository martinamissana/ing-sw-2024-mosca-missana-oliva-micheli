package it.polimi.ingsw.model.player;

import it.polimi.ingsw.model.card.GoldenCard;
import it.polimi.ingsw.model.card.ResourceCard;
import it.polimi.ingsw.model.card.StarterCard;
import it.polimi.ingsw.model.commonItem.ItemBox;
import it.polimi.ingsw.model.commonItem.Kingdom;
import it.polimi.ingsw.model.commonItem.Resource;
import it.polimi.ingsw.model.exceptions.IllegalMoveException;
import it.polimi.ingsw.model.exceptions.OccupiedCoordsException;
import it.polimi.ingsw.model.exceptions.UnreachablePositionException;
import it.polimi.ingsw.model.game.CardsPreset;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.*;

public class TestField {

    @Test
    public void testAddCard() throws IOException, IllegalMoveException {
        ArrayList<StarterCard> starterCards = CardsPreset.getStarterCards();
        ArrayList<ResourceCard> resourceCards = CardsPreset.getResourceCards();
        ArrayList<GoldenCard> goldenCards = CardsPreset.getGoldenCards();
        Field field = new Field();

        field.addCard(starterCards.getFirst());
        assertEquals(starterCards.getFirst(), field.getMatrix().get(new Coords(0, 0)));

        field.addCard(resourceCards.getFirst(), new Coords(1,0));
        assertEquals(resourceCards.getFirst(), field.getMatrix().get(new Coords(1, 0)));
        assertEquals(starterCards.getFirst(), field.getMatrix().get(new Coords(0, 0)));
        assertEquals(field.getCardBlock(), field.getMatrix().get(new Coords(1, -1)));

        assertThrows(OccupiedCoordsException.class, () ->
                field.addCard(resourceCards.getFirst(), new Coords(1, -1)));

        assertThrows(UnreachablePositionException.class, () ->
                field.addCard(resourceCards.get(3), new Coords(3, -1)));

        assertThrows(UnreachablePositionException.class, () ->
                field.addCard(resourceCards.get(3), new Coords(-2, -1)));

        field.addCard(resourceCards.get(1), new Coords(0,-1));
        assertEquals(field.getCardBlock(), field.getMatrix().get(new Coords(1,-1)));

        field.addCard(resourceCards.get(21), new Coords(2,0));
        field.addCard(goldenCards.getFirst(), new Coords(-1,0));
        assertEquals(goldenCards.getFirst(), field.getMatrix().get(new Coords(-1,0)));
    }

    @Test
    public void testAddCard2() throws IOException, IllegalMoveException {
        ArrayList<StarterCard> starterCards = CardsPreset.getStarterCards();
        ArrayList<ResourceCard> resourceCards = CardsPreset.getResourceCards();
        ArrayList<GoldenCard> goldenCards = CardsPreset.getGoldenCards();
        Field field = new Field();

        assertTrue(field.getMatrix().isEmpty());
        for (HashMap.Entry<ItemBox, Integer> entry : field.getTotalResources().entrySet())
            assertEquals(0, (int) entry.getValue());

        field.addCard(starterCards.getFirst());
        assertEquals(field.getMatrix().get(new Coords(0,0)).getCardID(),starterCards.getFirst().getCardID());
        assertEquals(field.getTotalResources().get(Kingdom.FUNGI), 1);
        assertEquals(field.getTotalResources().get(Kingdom.PLANT), 1);
        assertEquals(field.getTotalResources().get(Kingdom.ANIMAL), 1);
        assertEquals(field.getTotalResources().get(Kingdom.INSECT), 1);

        field.addCard(resourceCards.getFirst(), new Coords(0,1));
        assertEquals(field.getMatrix().get(new Coords(0,1)).getCardID(),resourceCards.getFirst().getCardID());
        assertEquals(field.getTotalResources().get(Kingdom.FUNGI), 2);
        assertEquals(field.getTotalResources().get(Kingdom.PLANT), 1);
        assertEquals(field.getTotalResources().get(Kingdom.ANIMAL), 1);
        assertEquals(field.getTotalResources().get(Kingdom.INSECT), 1);

        field.addCard(goldenCards.getFirst(), new Coords(0,-1));
        assertEquals(field.getMatrix().get(new Coords(0,-1)).getCardID(),goldenCards.getFirst().getCardID());
        assertEquals(field.getTotalResources().get(Kingdom.FUNGI), 2);
        assertEquals(field.getTotalResources().get(Kingdom.PLANT), 1);
        assertEquals(field.getTotalResources().get(Kingdom.ANIMAL), 0);
        assertEquals(field.getTotalResources().get(Kingdom.INSECT), 1);
        assertEquals(field.getTotalResources().get(Resource.QUILL), 1);
    }
}