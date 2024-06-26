package it.polimi.ingsw.model.player;

import it.polimi.ingsw.controller.Controller;
import it.polimi.ingsw.controller.exceptions.CannotJoinMultipleLobbiesException;
import it.polimi.ingsw.controller.exceptions.GameAlreadyStartedException;
import it.polimi.ingsw.controller.exceptions.UnexistentUserException;
import it.polimi.ingsw.controller.exceptions.WrongGamePhaseException;
import it.polimi.ingsw.model.card.*;
import it.polimi.ingsw.model.exceptions.*;
import it.polimi.ingsw.model.game.GameHandler;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.*;

class HandTest {

    GameHandler gameHandler=new GameHandler();
    HashMap<CornerType, Corner> frontCorners;
    HashMap<CornerType, Corner> backCorners;
    Card card;
    Player anna, eric, giorgio;
    Controller c=new Controller(gameHandler);

    @BeforeEach
    void setUp() throws CannotJoinMultipleLobbiesException, LobbyDoesNotExistException, UnexistentUserException, FullLobbyException, IOException, GameAlreadyStartedException, PawnAlreadyTakenException, GameDoesNotExistException, WrongGamePhaseException, EmptyDeckException, HandIsFullException {
        anna=new Player("anna");
        eric=new Player("eric");
        giorgio=new Player("giorgio");
        c.getGh().getUsers().add(anna);
        c.getGh().getUsers().add(eric);
        c.getGh().getUsers().add(giorgio);
        c.createLobby(3,anna.getNickname());
        c.joinLobby(eric.getNickname(),0);
        c.joinLobby(giorgio.getNickname(),0);
        c.choosePawn(0,anna.getNickname(),Pawn.BLUE);
        c.choosePawn(0,eric.getNickname(),Pawn.RED);
        c.choosePawn( 0,giorgio.getNickname(),Pawn.YELLOW);
        c.chooseCardSide(0,"anna",CardSide.FRONT);
        c.chooseCardSide(0,"eric",CardSide.FRONT);
        c.chooseCardSide(0,"giorgio",CardSide.FRONT);
    }

    @Test
    void removeAndAddCard() throws HandIsFullException {
        card=anna.getHand().getCard(2);
        assertEquals(anna.getHand().getSize(),3);

        anna.getHand().removeCard(anna.getHand().getCard(2));
        assertEquals(anna.getHand().getSize(),2);

        anna.getHand().addCard(card);
        assertEquals(anna.getHand().getSize(),3);

        assertThrows(HandIsFullException.class,()->anna.getHand().addCard(card));

        anna.getHand().removeAllCards();
        assertEquals(anna.getHand().getSize(),0);
    }

}