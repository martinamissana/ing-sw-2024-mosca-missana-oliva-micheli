package it.polimi.ingsw.network.RMI;

import it.polimi.ingsw.controller.Controller;
import it.polimi.ingsw.model.deck.DeckBufferType;
import it.polimi.ingsw.model.exceptions.*;
import it.polimi.ingsw.model.observer.Observer;
import it.polimi.ingsw.model.observer.events.*;
import it.polimi.ingsw.network.netMessage.c2s.LobbyJoinedMessage;
import it.polimi.ingsw.network.netMessage.s2c.*;
import java.io.IOException;
import java.rmi.RemoteException;
import java.util.Objects;

/**
 * RMIVirtualView class
 * receives the events from the controller and sends messages calling the remote method of the view elaborate
 */
public class RMIVirtualView implements Observer {
    private final Controller c;
    private Integer ID;
    private String nickname;
    private final ClientRemoteInterface view;


    /**
     * @param c controller
     * @param view client view
     * @param nickname username
     */
    public RMIVirtualView(Controller c, ClientRemoteInterface view, String nickname){
        this.c=c;
        c.getGh().addObserver(this);
        this.view=view;
        this.nickname=nickname;
    }


    @Override
    public void update(Event event) throws IOException {
        // System.out.println(event.getClass());
        switch (event) {
            case LoginEvent e -> {
                if (Objects.equals(nickname, e.getNickname())) {
                    try {
                        view.elaborate(new LoginSuccessMessage(e.getNickname()));
                    } catch (FullLobbyException | NicknameAlreadyTakenException | HandIsFullException |
                             IllegalMoveException ex) {
                        throw new RuntimeException(ex);
                    } catch (RemoteException exc){
                        c.getGh().removeObserver(this);
                    }
                }
            }
            case LobbyCreatedEvent e -> {
                if(e.getCreator().getNickname().equals(nickname)) this.ID=e.getID();
                try {
                    view.elaborate(new LobbyCreatedMessage(e.getCreator(), e.getLobby(), e.getID()));
                } catch (FullLobbyException | NicknameAlreadyTakenException | HandIsFullException |
                         IllegalMoveException ex) {
                    throw new RuntimeException(ex);
                }catch (RemoteException exc){
                    c.getGh().removeObserver(this);
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
                }catch (RemoteException exc){
                    c.getGh().removeObserver(this);
                }
            }
            case LobbyLeftEvent e -> {
                if (e.getPlayer().getNickname().equals(this.nickname)) {
                    this.ID=null;
                }
                try {
                    view.elaborate(new LobbyLeftMessage(e.getPlayer(), e.getLobby(), e.getID()));
                } catch (FullLobbyException | NicknameAlreadyTakenException | HandIsFullException |
                         IllegalMoveException ex) {
                    throw new RuntimeException(ex);
                }catch (RemoteException exc){
                    c.getGh().removeObserver(this);
                }
            }
            case LobbyDeletedEvent e -> {
                try {
                    view.elaborate(new LobbyDeletedMessage(e.getID()));
                } catch (FullLobbyException | NicknameAlreadyTakenException | HandIsFullException |
                         IllegalMoveException ex) {
                    throw new RuntimeException(ex);
                }catch (RemoteException exc){
                    c.getGh().removeObserver(this);
                }
            }
            case PawnAssignedEvent e -> {
                try {
                    view.elaborate(new PawnAssignedMessage(e.getPlayer(), e.getColor(), e.getLobbyID()));
                } catch (FullLobbyException | NicknameAlreadyTakenException | HandIsFullException |
                         IllegalMoveException ex) {
                    throw new RuntimeException(ex);
                }catch (RemoteException exc){
                    c.getGh().removeObserver(this);
                }
            }
            case ChatMessageAddedEvent e -> {
                if(e.getLobbyID().equals(ID)&& (e.getM().isGlobal() || (e.getM().getSender().getNickname().equals(nickname) || e.getM().getReceiver().getNickname().equals(nickname)))) {
                    try {
                        view.elaborate(new ChatMessageAddedMessage(e.getM(), e.getLobbyID()));
                    } catch (FullLobbyException | NicknameAlreadyTakenException | HandIsFullException |
                             IllegalMoveException ex) {
                        throw new RuntimeException(ex);
                    }catch (RemoteException exc){
                        c.getGh().removeObserver(this);
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
                    }catch (RemoteException exc){
                        c.getGh().removeObserver(this);
                    }
                }

            }
            case CardAddedToHandEvent e -> {
                if(e.getID().equals(ID)) {
                    try {
                        view.elaborate(new CardAddedToHandMessage(e.getPlayer(), e.getCard()));
                    } catch (FullLobbyException | NicknameAlreadyTakenException | HandIsFullException |
                             IllegalMoveException ex) {
                        throw new RuntimeException(ex);
                    }catch (RemoteException exc){
                        c.getGh().removeObserver(this);
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
                    }catch (RemoteException exc){
                        c.getGh().removeObserver(this);
                    }
                }
            }
            case CardPlacedOnFieldEvent e -> {
                if(e.getID().equals(ID)) {
                    try {
                        view.elaborate(new CardPlacedOnFieldMessage(e.getCoords(), e.getID(), e.getCard(), e.getSide(), e.getNickname()));
                    } catch (FullLobbyException | NicknameAlreadyTakenException | HandIsFullException |
                             IllegalMoveException ex) {
                        throw new RuntimeException(ex);
                    }catch (RemoteException exc){
                        c.getGh().removeObserver(this);
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
                    }catch (RemoteException exc){
                        c.getGh().removeObserver(this);
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
                    }catch (RemoteException exc){
                        c.getGh().removeObserver(this);
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
                    }catch (RemoteException exc){
                        c.getGh().removeObserver(this);
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
                    }catch (RemoteException exc){
                        c.getGh().removeObserver(this);
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
                    }catch (RemoteException exc){
                        c.getGh().removeObserver(this);
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
                    }catch (RemoteException exc){
                        c.getGh().removeObserver(this);
                    }
                }
            }
            case GameWinnersAnnouncedEvent e -> {
                if(e.getID().equals(ID)) {
                    try {
                        view.elaborate(new GameWinnersAnnouncedMessage(e.getID(), e.getWinners(),e.getGoalsDone()));
                    } catch (FullLobbyException | NicknameAlreadyTakenException | HandIsFullException |
                             IllegalMoveException ex) {
                        throw new RuntimeException(ex);
                    }catch (RemoteException exc){
                        c.getGh().removeObserver(this);
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
                    }catch (RemoteException exc){
                        c.getGh().removeObserver(this);
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
                    }catch (RemoteException exc){
                        c.getGh().removeObserver(this);
                    }
                }
            }
            case CurrentStatusEvent e -> {
                if (nickname != null && Objects.equals(nickname, e.getNickname())) {
                    try {
                        view.elaborate(new CurrentStatusMessage(e.getLobbies(), e.getNickname()));
                    } catch (FullLobbyException | NicknameAlreadyTakenException | HandIsFullException |
                             IllegalMoveException ex) {
                        throw new RuntimeException(ex);
                    }catch (RemoteException exc){
                        c.getGh().removeObserver(this);
                    }
                }
            }
            case ScoreIncrementedEvent e -> {
                try {
                    view.elaborate(new ScoreIncrementedMessage(e.getID(),e.getPlayer(),e.getPoints()));
                } catch (FullLobbyException | IllegalMoveException | HandIsFullException |
                         NicknameAlreadyTakenException ex) {
                    throw new RuntimeException(ex);
                }catch (RemoteException exc){
                    c.getGh().removeObserver(this);
                }
            }
            case DisconnectEvent e -> {
                if(Objects.equals(e.getNickname(),nickname) && Objects.equals(e.getID(),ID)) {
                    e.getC().getGh().removeObserver(this);
                }
            }
            default -> throw new IllegalStateException("Unexpected value: " + event);
        }
    }


}
