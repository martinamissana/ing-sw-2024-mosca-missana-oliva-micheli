package it.polimi.ingsw.network.RMI;

import it.polimi.ingsw.controller.Controller;
import it.polimi.ingsw.model.deck.DeckBufferType;
import it.polimi.ingsw.model.exceptions.FullLobbyException;
import it.polimi.ingsw.model.exceptions.HandIsFullException;
import it.polimi.ingsw.model.exceptions.IllegalMoveException;
import it.polimi.ingsw.model.exceptions.NicknameAlreadyTakenException;
import it.polimi.ingsw.model.observer.Observer;
import it.polimi.ingsw.model.observer.events.*;
import it.polimi.ingsw.network.netMessage.HeartBeatMessage;
import it.polimi.ingsw.network.netMessage.c2s.LobbyJoinedMessage;
import it.polimi.ingsw.network.netMessage.s2c.*;
import java.io.IOException;
import java.rmi.RemoteException;

/**
 * RMIVirtualView class
 * receives the events from the controller and sends messages calling the remote method of the view elaborate
 */
public class RMIVirtualView implements Observer {
    private Integer ID;
    private String nickname;
    private final ClientRemoteInterface view;

    /**
     * Class constructor
     * @param c controller
     * @param view
     * @throws RemoteException
     */
    public RMIVirtualView(Controller c, ClientRemoteInterface view) throws RemoteException {
        c.getGh().addObserver(this);
        this.view=view;
    }


    @Override
    public void update(Event event) throws IOException {
        switch (event) {
            case LoginEvent e -> {
                if(nickname==null) this.nickname=e.getNickname();
                try {
                    view.elaborate(new LoginMessage(e.getNickname()));
                } catch (FullLobbyException | NicknameAlreadyTakenException | HandIsFullException |
                         IllegalMoveException ex) {
                    throw new RuntimeException(ex);
                }
            }
            case LobbyCreatedEvent e -> {
                if(e.getCreator().getNickname().equals(nickname)) this.ID=e.getID();
                try {
                    view.elaborate(new LobbyCreatedMessage(e.getCreator(), e.getLobby(), e.getID()));
                } catch (FullLobbyException | NicknameAlreadyTakenException | HandIsFullException |
                         IllegalMoveException ex) {
                    throw new RuntimeException(ex);
                }
            }
            case LobbyJoinedEvent e -> {
                if(e.getPlayer().getNickname().equals(this.nickname)){
                    this.ID=e.getID();
                }
                try {
                    view.elaborate(new LobbyJoinedMessage(e.getPlayer(), e.getID()));
                } catch (FullLobbyException | NicknameAlreadyTakenException | HandIsFullException |
                         IllegalMoveException ex) {
                    throw new RuntimeException(ex);
                }
            }
            case LobbyLeftEvent e -> {
                if (e.getPlayer().getNickname().equals(this.nickname)) {
                    this.nickname=null;
                    this.ID=null;
                }
                try {
                    view.elaborate(new LobbyLeftMessage(e.getPlayer(), e.getLobby(), e.getID()));
                } catch (FullLobbyException | NicknameAlreadyTakenException | HandIsFullException |
                         IllegalMoveException ex) {
                    throw new RuntimeException(ex);
                }
            }
            case LobbyDeletedEvent e -> {
                try {
                    view.elaborate(new LobbyDeletedMessage(e.getID()));
                } catch (FullLobbyException | NicknameAlreadyTakenException | HandIsFullException |
                         IllegalMoveException ex) {
                    throw new RuntimeException(ex);
                }
            }
            case PawnAssignedEvent e -> {
                try {
                    view.elaborate(new PawnAssignedMessage(e.getPlayer(), e.getColor(), e.getLobbyID()));
                } catch (FullLobbyException | NicknameAlreadyTakenException | HandIsFullException |
                         IllegalMoveException ex) {
                    throw new RuntimeException(ex);
                }
            }
            case ChatMessageAddedEvent e -> {
                if(e.getLobbyID().equals(ID)) {
                    try {
                        view.elaborate(new ChatMessageAddedMessage(e.getM(), e.getLobbyID()));
                    } catch (FullLobbyException | NicknameAlreadyTakenException | HandIsFullException |
                             IllegalMoveException ex) {
                        throw new RuntimeException(ex);
                    }
                }
            }
            case GameCreatedEvent e -> {
                if(e.getID().equals(ID)){
                    try {
                        view.elaborate(new GameCreatedMessage(e.getID(), e.getFirstPlayer(), e.getScoreboard(), e.getTopResourceCard(), e.getTopGoldenCard(), e.getCommonGoal1(), e.getCommonGoal2(), e.getGamePhase(), e.getDeckBuffers().get(DeckBufferType.RES1), e.getDeckBuffers().get(DeckBufferType.RES2), e.getDeckBuffers().get(DeckBufferType.GOLD1), e.getDeckBuffers().get(DeckBufferType.GOLD2)));
                    } catch (FullLobbyException | NicknameAlreadyTakenException | HandIsFullException |
                             IllegalMoveException ex) {
                        throw new RuntimeException(ex);
                    }
                }

            }
            case CardAddedToHandEvent e -> {
                if(e.getPlayer().getNickname().equals(nickname)) {
                    try {
                        view.elaborate(new CardAddedToHandMessage(e.getPlayer(), e.getCard()));
                    } catch (FullLobbyException | NicknameAlreadyTakenException | HandIsFullException |
                             IllegalMoveException ex) {
                        throw new RuntimeException(ex);
                    }
                }
            }
            case CardRemovedFromHandEvent e -> {
                if(e.getPlayer().getNickname().equals(nickname)) {
                    try {
                        view.elaborate(new CardRemovedFromHandMessage(e.getPlayer(), e.getCard()));
                    } catch (FullLobbyException | NicknameAlreadyTakenException | HandIsFullException |
                             IllegalMoveException ex) {
                        throw new RuntimeException(ex);
                    }
                }
            }
            case CardPlacedOnFieldEvent e -> {
                if(e.getID().equals(ID)) {
                    try {
                        view.elaborate(new CardPlacedOnFieldMessage(e.getCoords(), e.getID(), e.getCard(),e.getCard().getSide(), e.getNickname()));
                    } catch (FullLobbyException | NicknameAlreadyTakenException | HandIsFullException |
                             IllegalMoveException ex) {
                        throw new RuntimeException(ex);
                    }
                }
            }
            case GamePhaseChangedEvent e -> {
                if(e.getID().equals(ID)) {
                    try {
                        view.elaborate(new GamePhaseChangedMessage(e.getID(), e.getGamePhase()));
                    } catch (FullLobbyException | NicknameAlreadyTakenException | HandIsFullException |
                             IllegalMoveException ex) {
                        throw new RuntimeException(ex);
                    }
                }
            }
            case SecretGoalsListAssignedEvent e -> {
                if(e.getPlayer().getNickname().equals(nickname)) {
                    try {
                        view.elaborate(new SecretGoalsListAssignedMessage(e.getList(), e.getPlayer()));
                    } catch (FullLobbyException | NicknameAlreadyTakenException | HandIsFullException |
                             IllegalMoveException ex) {
                        throw new RuntimeException(ex);
                    }
                }
            }
            case SecretGoalAssignedEvent e -> {
                if(e.getPlayer().getNickname().equals(nickname)) {
                    try {
                        view.elaborate(new SecretGoalAssignedMessage(e.getPlayer(), e.getGoal()));
                    } catch (FullLobbyException | NicknameAlreadyTakenException | HandIsFullException |
                             IllegalMoveException ex) {
                        throw new RuntimeException(ex);
                    }
                }
            }
            case GameActionSwitchedEvent e -> {
                if(e.getID().equals(ID)) {
                    try {
                        view.elaborate(new GameActionSwitchedMessage(e.getID(), e.getAction()));
                    } catch (FullLobbyException | NicknameAlreadyTakenException | HandIsFullException |
                             IllegalMoveException ex) {
                        throw new RuntimeException(ex);
                    }
                }
            }
            case LastRoundStartedEvent e -> {
                if(e.getID().equals(ID)) {
                    try {
                        view.elaborate(new LastRoundStartedMessage(e.getID()));
                    } catch (FullLobbyException | NicknameAlreadyTakenException | HandIsFullException |
                             IllegalMoveException ex) {
                        throw new RuntimeException(ex);
                    }
                }
            }
            case TurnChangedEvent e -> {
                if(e.getID().equals(ID)) {
                    try {
                        view.elaborate(new TurnChangedMessage(e.getID(), e.getNickname(),e.isLastRound()));
                    } catch (FullLobbyException | NicknameAlreadyTakenException | HandIsFullException |
                             IllegalMoveException ex) {
                        throw new RuntimeException(ex);
                    }
                }
            }
            case GameWinnersAnnouncedEvent e -> {
                if(e.getID().equals(ID)) {
                    try {
                        view.elaborate(new GameWinnersAnnouncedMessage(e.getID(), e.getWinners()));
                    } catch (FullLobbyException | NicknameAlreadyTakenException | HandIsFullException |
                             IllegalMoveException ex) {
                        throw new RuntimeException(ex);
                    }
                }
            }
            case GameTerminatedEvent e -> {
                if(e.getID().equals(ID)) {
                    try {
                        view.elaborate(new GameTerminatedMessage(e.getID()));
                    } catch (FullLobbyException | NicknameAlreadyTakenException | HandIsFullException |
                             IllegalMoveException ex) {
                        throw new RuntimeException(ex);
                    }
                }
            }
            case CardDrawnFromSourceEvent e -> {
                if(e.getID().equals(ID)) {
                    try {
                        view.elaborate(new CardDrawnFromSourceMessage(e.getID(), e.getType(), e.getCard()));
                    } catch (FullLobbyException | NicknameAlreadyTakenException | HandIsFullException |
                             IllegalMoveException ex) {
                        throw new RuntimeException(ex);
                    }
                }
            }
            case CurrentStatusEvent e -> {
                try {
                    view.elaborate(new CurrentStatusMessage(e.getLobbies()));
                } catch (FullLobbyException | NicknameAlreadyTakenException | HandIsFullException |
                         IllegalMoveException ex) {
                    throw new RuntimeException(ex);
                }
            }
            case ScoreIncrementedEvent e -> {
                try {
                    view.elaborate(new ScoreIncrementedMessage(e.getID(),e.getPlayer(),e.getPoints()));
                } catch (FullLobbyException | IllegalMoveException | HandIsFullException |
                         NicknameAlreadyTakenException ex) {
                    throw new RuntimeException(ex);
                }
            }
            case HeartBeatEvent e -> {
                try {
                    view.elaborate(new HeartBeatMessage());
                } catch (FullLobbyException | NicknameAlreadyTakenException | IllegalMoveException |
                         HandIsFullException ex) {
                    throw new RuntimeException(ex);
                }
            }
            default -> throw new IllegalStateException("Unexpected value: " + event);
        }
    }

}
