package it.polimi.ingsw.view.CLI;

import it.polimi.ingsw.controller.Controller;
import it.polimi.ingsw.controller.exceptions.GameAlreadyStartedException;
import it.polimi.ingsw.controller.exceptions.IllegalActionException;
import it.polimi.ingsw.controller.exceptions.NotYourTurnException;
import it.polimi.ingsw.model.card.CardSide;
import it.polimi.ingsw.model.deck.DeckBufferType;
import it.polimi.ingsw.model.deck.DeckType;
import it.polimi.ingsw.model.exceptions.*;
import it.polimi.ingsw.model.game.GameHandler;
import it.polimi.ingsw.model.player.Coords;
import it.polimi.ingsw.model.player.Pawn;
import it.polimi.ingsw.model.player.Player;
import it.polimi.ingsw.network.exceptions.ConnectionException;

import java.io.IOException;

public class Main {
    public static void main(String[] args) throws FullLobbyException, LobbyDoesNotExistsException, NicknameAlreadyTakenException, IOException, GameDoesNotExistException, EmptyDeckException, HandIsFullException, PawnAlreadyTakenException, GameAlreadyStartedException, IllegalActionException, NotYourTurnException, IllegalMoveException, EmptyBufferException, ConnectionException {
        CLI cli = new CLI();
        CLIGame cliG = new CLIGame();
        GameHandler gh = new GameHandler();
        Controller controller = new Controller(gh);

        cli.setController(controller);
        cli.setGameHandler(gh);
        cliG.setController(controller);
        cliG.setGameHandler(gh);

        Player creator1 = new Player("creator1");
        gh.addUser(creator1);
        controller.createLobby(2, creator1);

        Player creator2 = new Player("creator2");
        gh.addUser(creator2);
        controller.createLobby(3, creator2);

        Player joiner1 = new Player("joiner1");
        gh.addUser(joiner1);
        controller.joinLobby(joiner1, 1);

        Player joiner2 = new Player("joiner2");
        gh.addUser(joiner2);
        controller.joinLobby(joiner2, 1);

        Player creator3 = new Player("creator3");
        gh.addUser(creator3);
        controller.createLobby(4, creator3);

        controller.deleteLobby(2);

        Player creator4 = new Player("creator4");
        gh.addUser(creator4);
        controller.createLobby(4, creator4);

        Player joiner3 = new Player("joiner3");
        gh.addUser(joiner3);
        controller.joinLobby(joiner3, 3);

        Integer gameID = 1;
        controller.setGameArea(gameID);
        controller.giveStarterCards(gameID);
        for (Player p : gh.getGame(gameID).getPlayers()) controller.chooseCardSide(p, CardSide.BACK);
        controller.fillHands(gameID);

        cli.printHello();
        cli.printOpenLobbies();
        cli.printActiveGames();


        // controller.choosePawn(1, gh.getLobby(1).getPlayers().getFirst(), Pawn.BLUE);
        // WARNING! (↑) This changes pawns even if game is started


        controller.choosePawn(3, joiner3, Pawn.BLUE);
        cli.printPlayers(3);
        cli.setPawnColor(3, creator4);
        cli.printPlayers(3);

        cli.quitLobby(0, creator1);
        cli.printOpenLobbies();



        controller.playCard(gameID, gh.getGame(gameID).getPlayers().getFirst(),0, new Coords(0, 1));

        cliG.setGameHandler(gh);
        cliG.printHand(gh.getGame(gameID).getPlayers().getFirst(), -1);
        cliG.printHand(gh.getGame(gameID).getPlayers().getLast(), 2);

        cliG.printGameArea(gameID);

        cliG.printCard(gh.getGame(gameID).getPlayers().getFirst().getField().getMatrix().get(new Coords(0, 0)));
        cliG.printCard(gh.getGame(gameID).getPlayers().getLast().getField().getMatrix().get(new Coords(0, 0)));



        for (int i = gh.getGame(gameID).getDeck(DeckType.RESOURCE).getCards().size(); i > 0; i--) gh.getGame(gameID).getDeck(DeckType.RESOURCE).draw();
        gh.getGame(gameID).getDeckBuffer(DeckBufferType.RES1).draw();
        cliG.printGameArea(gameID);

    }
}
