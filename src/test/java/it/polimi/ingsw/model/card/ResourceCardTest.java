package it.polimi.ingsw.model.card;

import it.polimi.ingsw.model.commonItem.CornerStatus;
import it.polimi.ingsw.model.commonItem.Kingdom;
import it.polimi.ingsw.model.commonItem.Resource;


import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ResourceCardTest {

    public void testFlip (){
        HashMap<CornerType,Corner> frontCorners= new HashMap<>();
        HashMap<CornerType,Corner> backCorners=new HashMap<>();
        Corner corner1 = new Corner(CornerStatus.EMPTY);
        Corner corner2 = new Corner(Resource.INKWELL);
        frontCorners.put(CornerType.NORTH, corner1);
        backCorners.put(CornerType.NORTH, corner2);
        ResourceCard card = new ResourceCard(1, CardSide.FRONT, frontCorners, backCorners, 0, Kingdom.ANIMAL);
        card.flip();
        assertEquals(card.getSide(), CardSide.BACK);
        card.flip();
        assertEquals(card.getSide(), CardSide.FRONT);
    }

    public void testGetCornerTest(){
        HashMap<CornerType,Corner> frontCorners= new HashMap<>();
        HashMap<CornerType,Corner> backCorners=new HashMap<>();
        Corner corner1 = new Corner(CornerStatus.EMPTY);
        Corner corner2 = new Corner(Resource.INKWELL);
        frontCorners.put(CornerType.NORTH, corner1);
        backCorners.put(CornerType.NORTH, corner2);
        ResourceCard card = new ResourceCard(1, CardSide.FRONT, frontCorners, backCorners, 0, Kingdom.ANIMAL);
        assertEquals(card.getCorner(CornerType.NORTH), CornerStatus.EMPTY);
        card.flip();
        assertEquals(card.getCorner(CornerType.NORTH), Resource.INKWELL);
        assertEquals(card.getCorner(CornerType.SOUTH), null);
    }

}