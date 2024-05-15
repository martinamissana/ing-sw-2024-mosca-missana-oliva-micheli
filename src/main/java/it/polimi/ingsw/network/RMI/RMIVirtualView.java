package it.polimi.ingsw.network.RMI;

import it.polimi.ingsw.controller.Controller;
import it.polimi.ingsw.model.deck.DeckBufferType;
import it.polimi.ingsw.model.exceptions.FullLobbyException;
import it.polimi.ingsw.model.exceptions.HandIsFullException;
import it.polimi.ingsw.model.exceptions.IllegalMoveException;
import it.polimi.ingsw.model.exceptions.NicknameAlreadyTakenException;
import it.polimi.ingsw.model.observer.Observer;
import it.polimi.ingsw.model.observer.events.*;
import it.polimi.ingsw.network.netMessage.c2s.LobbyJoinedMessage;
import it.polimi.ingsw.network.netMessage.s2c.*;
import java.io.IOException;
import java.rmi.RemoteException;

public class RMIVirtualView implements Observer, Runnable {
    private final Controller c;
    private final Integer ID;
    private final String nickname;
    private final ClientRemoteInterface view;

    public RMIVirtualView(Controller c, ClientRemoteInterface view) throws RemoteException {
        this.c = c;
        c.getGh().addObserver(this);
        this.view=view;
        this.ID=view.getID();
        this.nickname=view.getNickname();
    }


    @Override
    public void update(Event event) throws IOException {
        switch (event) {
            case LoginEvent e -> {
                try {
                    view.elaborate(new LoginMessage(e.getNickname()));
                } catch (FullLobbyException | NicknameAlreadyTakenException | HandIsFullException |
                         IllegalMoveException ex) {
                    throw new RuntimeException(ex);
                }
            }
            case LobbyCreatedEvent e -> {
                try {
                    view.elaborate(new LobbyCreatedMessage(e.getCreator(), e.getLobby(), e.getID()));
                } catch (FullLobbyException | NicknameAlreadyTakenException | HandIsFullException |
                         IllegalMoveException ex) {
                    throw new RuntimeException(ex);
                }
            }
            case LobbyJoinedEvent e -> {
                try {
                    view.elaborate(new LobbyJoinedMessage(e.getPlayer(), e.getID()));
                } catch (FullLobbyException | NicknameAlreadyTakenException | HandIsFullException |
                         IllegalMoveException ex) {
                    throw new RuntimeException(ex);
                }
            }
            case LobbyLeftEvent e -> {
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
                        view.elaborate(new CardPlacedOnFieldMessage(e.getCoords(), e.getID(), e.getCard(), e.getNickname()));
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
                        view.elaborate(new TurnChangedMessage(e.getID(), e.getNickname()));
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
            default -> throw new IllegalStateException("Unexpected value: " + event);
        }
    }

    @Override
    public void run() {

    }
}
