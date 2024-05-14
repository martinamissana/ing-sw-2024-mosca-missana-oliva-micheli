package it.polimi.ingsw.view;

import it.polimi.ingsw.controller.exceptions.*;
import it.polimi.ingsw.model.card.CardSide;
import it.polimi.ingsw.model.card.ResourceCard;
import it.polimi.ingsw.model.chat.Message;
import it.polimi.ingsw.model.deck.DeckBufferType;
import it.polimi.ingsw.model.deck.DeckType;
import it.polimi.ingsw.model.deck.DeckTypeBox;
import it.polimi.ingsw.model.exceptions.*;
import it.polimi.ingsw.model.player.Coords;
import it.polimi.ingsw.model.player.Pawn;
import it.polimi.ingsw.model.player.Player;
import it.polimi.ingsw.network.RMI.RemoteInterface;
import it.polimi.ingsw.network.netMessage.NetMessage;
import it.polimi.ingsw.network.netMessage.c2s.CurrentStatusMessage;
import it.polimi.ingsw.network.netMessage.c2s.DisconnectMessage;
import it.polimi.ingsw.network.netMessage.c2s.LobbyJoinedMessage;
import it.polimi.ingsw.network.netMessage.s2c.*;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.io.IOException;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;


public class RMIView extends View {

    private final Registry registry;
    private final RemoteInterface RMIServer;

    public RMIView() throws RemoteException, NotBoundException {
        registry = LocateRegistry.getRegistry();
        System.out.println("RMI registry bindings: ");
        String[] e = registry.list();
        for (String string : e) {
            System.out.println(string);
        }
        String remoteObjectName = "RMIServer";
        RMIServer = (RemoteInterface) registry.lookup(remoteObjectName);
        RMIServer.connect(super.getID(),this);
    }

    public void login(String nickname) throws NicknameAlreadyTakenException, IOException {
        RMIServer.login(nickname);
        super.setPlayer(new Player(nickname));
        super.setNickname(nickname);
        System.out.println("you tried to login");
    }

    @Override
    public void createLobby(int numOfPlayers) throws LobbyDoesNotExistsException, IOException, CannotJoinMultipleLobbiesException, UnexistentUserException, FullLobbyException, NicknameAlreadyTakenException, ClassNotFoundException {
        RMIServer.createLobby(numOfPlayers, super.getPlayer().getNickname());
    }

    @Override
    public void joinLobby(int lobbyID) throws FullLobbyException, NicknameAlreadyTakenException, LobbyDoesNotExistsException, IOException, CannotJoinMultipleLobbiesException, UnexistentUserException {
        RMIServer.joinLobby(lobbyID, super.getPlayer().getNickname());
    }

    @Override
    public void leaveLobby() throws GameAlreadyStartedException, LobbyDoesNotExistsException, IOException, GameDoesNotExistException, UnexistentUserException {
        RMIServer.leaveLobby(super.getID(), super.getPlayer().getNickname());
    }

    @Override
    public void sendMessage(Message message) throws LobbyDoesNotExistsException, GameDoesNotExistException, UnexistentUserException, RemoteException, PlayerChatMismatchException {
        RMIServer.sendMessage(message,super.getID());
    }

    @Override
    public void choosePawn(Pawn color) throws LobbyDoesNotExistsException, PawnAlreadyTakenException, IOException, GameAlreadyStartedException, GameDoesNotExistException, UnexistentUserException {
        RMIServer.choosePawn(super.getID(), super.getPlayer().getNickname(), color);
    }

    @Override
    public void chooseSecretGoal(int goalID) throws IOException, IllegalGoalChosenException, WrongGamePhaseException, GameDoesNotExistException, UnexistentUserException {
        RMIServer.chooseSecretGoal(super.getID(), super.getPlayer().getNickname(), goalID);
    }

    @Override
    public void chooseCardSide(CardSide side) throws EmptyDeckException, GameDoesNotExistException, HandIsFullException, UnexistentUserException, IOException, WrongGamePhaseException {
        RMIServer.chooseCardSide(super.getID(), super.getPlayer().getNickname(), side);
    }

    @Override
    public void playCard(int handPos, Coords coords) throws IllegalActionException, NotYourTurnException, IllegalMoveException, GameDoesNotExistException, LobbyDoesNotExistsException, UnexistentUserException, IOException {
        RMIServer.playCard(super.getID(), super.getPlayer().getNickname(), handPos, coords);
    }

    @Override
    public void drawCard(DeckTypeBox deckTypeBox) throws IllegalActionException, EmptyBufferException, NotYourTurnException, EmptyDeckException, HandIsFullException, IOException, UnexistentUserException, LobbyDoesNotExistsException, GameDoesNotExistException {
        RMIServer.drawCard(super.getID(), super.getPlayer().getNickname(), deckTypeBox);
    }

    @Override
    public void flipCard(int handPos) throws UnexistentUserException, RemoteException, GameDoesNotExistException {
        RMIServer.flipCard(super.getID(),super.getPlayer().getNickname(), handPos);
    }

    @Override
    public void getCurrentStatus() {

    }


    public void elaborate(NetMessage message) throws IOException, FullLobbyException, NicknameAlreadyTakenException, HandIsFullException, IllegalMoveException {
            switch (message) {
                case LoginMessage m -> {
                }
                case LoginFail_NicknameAlreadyTaken m -> {
                    DisconnectMessage disconnectMessage = new DisconnectMessage();
                }
                case LobbyCreatedMessage m -> {
                    if (m.getCreator().equals(super.getPlayer())) {
                        super.setID(m.getID());
                    }
                    super.getLobbies().put(m.getID(), m.getLobby());
                }
                case LobbyJoinedMessage m -> {
                    if (m.getPlayer().getNickname().equals(super.getPlayer().getNickname()))
                        super.setID(m.getID());
                    super.getLobbies().get(m.getID()).addPlayer(m.getPlayer());
                }
                case LobbyLeftMessage m -> {
                    if (m.getPlayer().getNickname().equals(super.getNickname())) {
                        super.setID(null);
                        super.setPawn(null);
                    }
                    super.getLobbies().get(m.getID()).removePlayer(m.getPlayer());
                }
                case LobbyDeletedMessage m -> {
                    super.getLobbies().remove(m.getID());
                }
                case PawnAssignedMessage m -> {
                    if (m.getPlayer().getNickname().equals(super.getNickname()))
                        super.setPawn(m.getColor());
                    else {
                        for (Player p : super.getLobbies().get(m.getLobbyID()).getPlayers()) {
                            if (p.equals(m.getPlayer())) p.setPawn(m.getColor());
                        }
                    }
                }
                case CurrentStatusMessage m -> {
                    super.getLobbies().putAll(m.getLobbies());
                }
                case ChatMessageAddedMessage m -> {
                    if (m.getM().getSender().equals(super.getPlayer())) {
                        super.getChat().getSentMessages().add(m.getM());
                    } else if (m.getM().isGlobal() || m.getM().getReceiver().equals(super.getPlayer())) {
                        super.getChat().getReceivedMessages().add(m.getM());
                    }
                }
                case GameCreatedMessage m -> {
                    if (m.getID().equals(super.getID())) {
                        if (m.getFirstPlayer().equals(super.getPlayer())) {
                            super.setFirstPlayer(true);
                            super.setYourTurn(true);
                        } else {
                            super.setFirstPlayer(false);
                            super.setYourTurn(false);
                        }
                        super.setScoreboard(m.getScoreboard());
                        super.setDeckBuffers(m.getDeckBuffers());
                        super.setTopResourceCard(m.getTopResourceCard());
                        super.setTopGoldenCard(m.getTopGoldenCard());
                        super.setCommonGoal1(m.getCommonGoal1());
                        super.setCommonGoal2(m.getCommonGoal2());
                        super.setGamePhase(m.getGamePhase());
                        super.setAction(m.getAction());
                    }
                }
                case CardAddedToHandMessage m -> {
                    if (m.getPlayer().equals(super.getPlayer())) {
                        super.getHand().addCard(m.getCard());
                    }
                }
                case CardRemovedFromHandMessage m -> {
                    if (m.getPlayer().equals(super.getPlayer())) {
                        super.getHand().removeCard(m.getCard());
                    }
                }
                case CardPlacedOnFieldMessage m -> {
                    if (m.getNickname().equals(super.getNickname())) {
                        super.getMyField().addCard((ResourceCard) m.getCard(), m.getCoords());
                    } else {
                        for (Player p : super.getFields().keySet()) {
                            if (m.getNickname().equals(p.getNickname()))
                                p.getField().addCard((ResourceCard) m.getCard(), m.getCoords());
                        }
                    }
                }
                case GamePhaseChangedMessage m -> {
                    if (m.getID().equals(super.getID()))
                        super.setGamePhase(m.getGamePhase());
                }
                case SecretGoalsListAssignedMessage m -> {
                    if (m.getPlayer().equals(super.getPlayer()))
                        super.setPersonalGoalChoices(m.getList());
                }
                case SecretGoalAssignedMessage m -> {
                    if (m.getPlayer().equals(super.getPlayer()))
                        super.setSecretGoal(m.getGoal());
                }
                case GameActionSwitchedMessage m -> {
                    if (m.getID().equals(super.getID()))
                        super.setAction(m.getAction());
                }
                case LastRoundStartedMessage m -> {
                    if (m.getID().equals(super.getID()))
                        super.setLastRound(true);
                }
                case TurnChangedMessage m -> {
                    if (m.getID().equals(super.getID()) && m.getNickname().equals(super.getNickname()))
                        super.setYourTurn(true);
                    else if (m.getID().equals(super.getID()) && !m.getNickname().equals(super.getNickname()))
                        super.setYourTurn(false);
                }
                case GameWinnersAnnouncedMessage m -> {
                    if (m.getID().equals(super.getID()))
                        super.setWinners(m.getWinners());
                }
                case GameTerminatedMessage m -> {
                    if (m.getID().equals(super.getID())) {
                        super.setFirstPlayer(false);
                        super.setYourTurn(false);
                        super.setScoreboard(null);
                        super.setDeckBuffers(null);
                        super.setTopResourceCard(null);
                        super.setTopGoldenCard(null);
                        super.setCommonGoal1(null);
                        super.setCommonGoal2(null);
                        super.setGamePhase(null);
                        super.setAction(null);
                        super.setID(null);
                        super.setPawn(null);
                    }
                }
                case CardDrawnFromSourceMessage m -> {
                    if (m.getID().equals(super.getID())) {
                        if (m.getType().equals(DeckType.RESOURCE))
                            super.setTopResourceCard(m.getCard());
                        if (m.getType().equals(DeckType.GOLDEN))
                            super.setTopGoldenCard(m.getCard());
                        else super.setCardInDeckBuffer((DeckBufferType) m.getType(), m.getCard());
                    }
                }
                case FailMessage m -> {
                    super.getErrorMessages().add(m.getMessage());
                }
                default -> throw new IllegalStateException("Unexpected value: " + message);
            }
            notify(message);
    }

    public void heartbeat() throws RemoteException {
        ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();
        Runnable task = () -> {
            try {
                RMIServer.heartbeat();
            } catch (RemoteException e) {
                throw new RuntimeException(e);
            }
        };
        executor.scheduleAtFixedRate(task, 0, 5, TimeUnit.SECONDS);
    }

}
