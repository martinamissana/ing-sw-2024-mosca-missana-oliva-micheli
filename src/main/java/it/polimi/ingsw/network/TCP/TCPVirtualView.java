package it.polimi.ingsw.network.TCP;

import it.polimi.ingsw.controller.Controller;
import it.polimi.ingsw.controller.exceptions.*;
import it.polimi.ingsw.model.deck.DeckBufferType;
import it.polimi.ingsw.model.exceptions.*;
import it.polimi.ingsw.model.observer.Observer;
import it.polimi.ingsw.model.observer.events.*;
import it.polimi.ingsw.network.netMessage.HeartBeatMessage;
import it.polimi.ingsw.network.netMessage.NetMessage;
import it.polimi.ingsw.network.netMessage.c2s.*;
import it.polimi.ingsw.network.netMessage.s2c.*;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class TCPVirtualView implements Runnable, Observer {
    private final Socket socket;
    private final ObjectInputStream in;

    private final ObjectOutputStream out;
    private final Controller c;
    private Integer ID;
    private String nickname;
    private HeartBeatDetector heartBeatDetector = null;


    public TCPVirtualView(Socket socket, Controller c) throws IOException {
        this.socket = socket;
        this.out = new ObjectOutputStream(socket.getOutputStream());
        this.in = new ObjectInputStream(socket.getInputStream());
        this.c = c;
        c.getGh().addObserver(this);
    }

    public ObjectInputStream getIn() {
        return in;
    }

    public ObjectOutputStream getOut() {
        return out;
    }

    public String getNickname() {
        return nickname;
    }

    public Integer getID() {
        return ID;
    }

    public void run() {
        try {
            NetMessage deserialized;
            do {
                deserialized = (NetMessage) in.readObject();
                elaborate(deserialized);
            } while (!socket.isClosed() && deserialized.getClass() != DisconnectMessage.class );
            in.close();
            out.close();
            socket.close();
            if(ID!=null)c.leaveLobby(nickname,ID);
            c.getGh().removeUser(nickname);
            Thread.currentThread().interrupt();
        } catch (IOException | ClassNotFoundException | GameAlreadyStartedException | GameDoesNotExistException |
                 InterruptedException | NicknameAlreadyTakenException | LobbyDoesNotExistsException |
                 CannotJoinMultipleLobbiesException | FullLobbyException | UnexistentUserException |
                 PlayerChatMismatchException | EmptyDeckException | HandIsFullException | WrongGamePhaseException |
                 IllegalGoalChosenException e) {
            throw new RuntimeException(e);
        }
    }


    @Override
    public void update(Event event) throws IOException {
        if (socket.isConnected()) {
            switch (event) {
                case LoginEvent e -> {
                    LoginMessage m = new LoginMessage(((LoginEvent) event).getNickname());
                    out.writeObject(m);

                }
                case LobbyCreatedEvent e -> {
                    LobbyCreatedMessage m = new LobbyCreatedMessage(e.getCreator(), e.getLobby(), e.getID());
                    out.writeObject(m);
                }
                case LobbyJoinedEvent e -> {
                    LobbyJoinedMessage m = new LobbyJoinedMessage(e.getPlayer(), e.getID());
                    out.writeObject(m);
                }
                case LobbyLeftEvent e -> {
                    LobbyLeftMessage m = new LobbyLeftMessage(e.getPlayer(), e.getLobby(), e.getID());
                    out.writeObject(m);
                }
                case LobbyDeletedEvent e -> {
                    LobbyDeletedMessage m = new LobbyDeletedMessage(e.getID());
                    out.writeObject(m);
                }
                case PawnAssignedEvent e -> {
                    PawnAssignedMessage m = new PawnAssignedMessage(e.getPlayer(), e.getColor(), e.getLobbyID());
                    out.writeObject(m);
                }
                case ChatMessageAddedEvent e -> {
                    if(e.getLobbyID().equals(ID)) {
                        ChatMessageAddedMessage m = new ChatMessageAddedMessage(e.getM(), e.getLobbyID());
                        out.writeObject(m);
                    }
                }
                case GameCreatedEvent e -> {
                    if(e.getID().equals(ID)){
                        GameCreatedMessage m = new GameCreatedMessage(e.getID(), e.getFirstPlayer(), e.getScoreboard(), e.getTopResourceCard(), e.getTopGoldenCard(), e.getCommonGoal1(), e.getCommonGoal2(), e.getGamePhase(), e.getDeckBuffers().get(DeckBufferType.RES1), e.getDeckBuffers().get(DeckBufferType.RES2), e.getDeckBuffers().get(DeckBufferType.GOLD1), e.getDeckBuffers().get(DeckBufferType.GOLD2));
                        out.writeObject(m);
                    }

                }
                case CardAddedToHandEvent e -> {
                    if(e.getPlayer().getNickname().equals(nickname)) {
                        CardAddedToHandMessage m = new CardAddedToHandMessage(e.getPlayer(), e.getCard());
                        out.writeObject(m);
                    }
                }
                case CardRemovedFromHandEvent e -> {
                    if(e.getPlayer().getNickname().equals(nickname)) {
                        CardRemovedFromHandMessage m = new CardRemovedFromHandMessage(e.getPlayer(), e.getCard());
                        out.writeObject(m);
                    }
                }
                case CardPlacedOnFieldEvent e -> {
                    if(e.getID().equals(ID)) {
                        CardPlacedOnFieldMessage m = new CardPlacedOnFieldMessage(e.getCoords(), e.getID(), e.getCard(), e.getNickname());
                        out.writeObject(m);
                    }
                }
                case GamePhaseChangedEvent e -> {
                    if(e.getID().equals(ID)) {
                        GamePhaseChangedMessage m = new GamePhaseChangedMessage(e.getID(), e.getGamePhase());
                        out.writeObject(m);
                    }
                }
                case SecretGoalsListAssignedEvent e -> {
                    if(e.getPlayer().getNickname().equals(nickname)) {
                        SecretGoalsListAssignedMessage m = new SecretGoalsListAssignedMessage(e.getList(), e.getPlayer());
                        out.writeObject(m);
                    }
                }
                case SecretGoalAssignedEvent e -> {
                    if(e.getPlayer().getNickname().equals(nickname)) {
                        SecretGoalAssignedMessage m = new SecretGoalAssignedMessage(e.getPlayer(), e.getGoal());
                        out.writeObject(m);
                    }
                }
                case GameActionSwitchedEvent e -> {
                    if(e.getID().equals(ID)) {
                        GameActionSwitchedMessage m = new GameActionSwitchedMessage(e.getID(), e.getAction());
                        out.writeObject(m);
                    }
                }
                case LastRoundStartedEvent e -> {
                    if(e.getID().equals(ID)) {
                        LastRoundStartedMessage m = new LastRoundStartedMessage(e.getID());
                        out.writeObject(m);
                    }
                }
                case TurnChangedEvent e -> {
                    if(e.getID().equals(ID)) {
                        TurnChangedMessage m = new TurnChangedMessage(e.getID(), e.getNickname());
                        out.writeObject(m);
                    }
                }
                case GameWinnersAnnouncedEvent e -> {
                    if(e.getID().equals(ID)) {
                        GameWinnersAnnouncedMessage m = new GameWinnersAnnouncedMessage(e.getID(), e.getWinners());
                        out.writeObject(m);
                    }
                }
                case GameTerminatedEvent e -> {
                    if(e.getID().equals(ID)) {
                        GameTerminatedMessage m = new GameTerminatedMessage(e.getID());
                        out.writeObject(m);
                    }
                }
                case CardDrawnFromSourceEvent e -> {
                    if(e.getID().equals(ID)) {
                        CardDrawnFromSourceMessage m = new CardDrawnFromSourceMessage(e.getID(), e.getType(), e.getCard());
                        out.writeObject(m);
                    }
                }
                default -> throw new IllegalStateException("Unexpected value: " + event);
            }
        }

    }

    private void elaborate(NetMessage message) throws InterruptedException, NicknameAlreadyTakenException, IOException, LobbyDoesNotExistsException, CannotJoinMultipleLobbiesException, FullLobbyException, GameAlreadyStartedException, GameDoesNotExistException, UnexistentUserException, PlayerChatMismatchException, EmptyDeckException, HandIsFullException, WrongGamePhaseException, IllegalGoalChosenException {
        if(heartBeatDetector != null) heartBeatDetector.resetTimer();
        switch (message) {
            case MyNickname m -> {
                try {
                    c.login(m.getNickname());
                    setNickname(m.getNickname());
                    //this.heartBeatDetector = new HeartBeatDetector(this, c, socket);
                } catch (NicknameAlreadyTakenException e) {
                    LoginFail_NicknameAlreadyTaken errorMessage = new LoginFail_NicknameAlreadyTaken();
                    out.writeObject(m);
                }
            }
            case CreateLobbyMessage m -> {
                try {
                    c.createLobby(m.getNumOfPlayers(), m.getCreator().getNickname());
                    setID(c.getGh().getNumOfLobbies()-1);
                } catch (CannotJoinMultipleLobbiesException e) {
                    FailMessage failMessage = new FailMessage("you can't create a lobby when you are already in one", m.getCreator().getNickname());
                    out.writeObject(failMessage);
                }
            }
            case JoinLobbyMessage m -> {
                try {
                    c.joinLobby(m.getPlayer().getNickname(), m.getID());
                    setID(m.getID());
                } catch (FullLobbyException e) {
                    FailMessage failMessage = new FailMessage("you couldn't join the lobby because it was full", m.getPlayer().getNickname());
                    out.writeObject(failMessage);
                } catch (LobbyDoesNotExistsException e) {
                    FailMessage failMessage = new FailMessage("you couldn't join the lobby because it doesn't exist", m.getPlayer().getNickname());
                    out.writeObject(failMessage);
                } catch (CannotJoinMultipleLobbiesException e) {
                    FailMessage failMessage = new FailMessage("you can't join multiple lobbies", m.getPlayer().getNickname());
                    out.writeObject(failMessage);
                } catch (UnexistentUserException e) {
                    throw new RuntimeException(e);
                }
            }
            case LeaveLobbyMessage m -> {
                try {
                    c.leaveLobby(m.getPlayer().getNickname(), m.getID());
                    setID(null);
                } catch (LobbyDoesNotExistsException e) {
                    FailMessage failMessage = new FailMessage("you can't leave an inexistant lobby", m.getPlayer().getNickname());
                    out.writeObject(failMessage);
                }
            }
            case ChoosePawnMessage m -> {
                try {
                    c.choosePawn(m.getLobbyID(), m.getPlayer().getNickname(), m.getColor());
                } catch (PawnAlreadyTakenException e) {
                    FailMessage failMessage = new FailMessage("pawn already taken", m.getPlayer().getNickname());
                    out.writeObject(failMessage);
                } catch (LobbyDoesNotExistsException | GameAlreadyStartedException | IOException |
                         GameDoesNotExistException e) {
                    FailMessage failMessage = new FailMessage(e.toString(), m.getPlayer().getNickname());
                    out.writeObject(failMessage);
                }
            }
            case SendMessage m -> {
                try {
                    c.send(m.getM(), m.getLobbyID());
                } catch (GameDoesNotExistException | LobbyDoesNotExistsException | PlayerChatMismatchException |
                         UnexistentUserException e) {
                    FailMessage failMessage = new FailMessage(e.toString(), m.getM().getSender().getNickname());
                    out.writeObject(failMessage);
                }
            }
            case GetCurrentStatusMessage m -> {
                CurrentStatusMessage status = new CurrentStatusMessage(c.getGh().getLobbies());
                out.writeObject(status);
            }
            case ChooseCardSideMessage m -> {
                c.chooseCardSide(m.getID(), m.getNickname(), m.getSide());
            }
            case ChooseSecretGoalMessage m -> {
                try {
                    c.chooseSecretGoal(m.getID(), m.getNickname(), m.getGoalID());
                } catch (IllegalGoalChosenException e) {
                    FailMessage failMessage = new FailMessage("the goal you chose is not valid", m.getNickname());
                    out.writeObject(failMessage);
                }
            }
            case PlayCardMessage m -> {
                try {
                    c.playCard(m.getGameID(), m.getNickname(), m.getHandPos(), m.getCoords());
                } catch (IllegalActionException e) {
                    FailMessage failMessage = new FailMessage("you cannot play a card now", m.getNickname());
                    out.writeObject(failMessage);
                } catch (NotYourTurnException e) {
                    FailMessage failMessage = new FailMessage("it's not your turn", m.getNickname());
                    out.writeObject(failMessage);
                } catch (IllegalMoveException e) {
                    FailMessage failMessage = new FailMessage("you can't place the card on those coordinates", m.getNickname());
                    out.writeObject(failMessage);
                }
            }
            case DrawCardMessage m -> {
                try {
                    c.drawCard(m.getGameID(), m.getNickname(), m.getDeckTypeBox());
                } catch (IllegalActionException e) {
                    FailMessage failMessage = new FailMessage("you can't draw a card now", m.getNickname());
                    out.writeObject(failMessage);
                } catch (EmptyBufferException e) {
                    FailMessage failMessage = new FailMessage("the deck buffer you want to draw from is empty", m.getNickname());
                    out.writeObject(failMessage);
                } catch (NotYourTurnException e) {
                    FailMessage failMessage = new FailMessage("it's not your turn", m.getNickname());
                    out.writeObject(failMessage);
                } catch (EmptyDeckException e) {
                    FailMessage failMessage = new FailMessage("the deck you want to draw from is empty", m.getNickname());
                    out.writeObject(failMessage);
                }
            }
            case FlipCardMessage m -> {
                c.flipCard(m.getGameID(), m.getNickname(), m.getHandPos());
            }
            case HeartBeatMessage  m -> {
                System.out.println("heartbeat detected");

            }
            default -> throw new IllegalStateException("Unexpected value: " + message);
        }
    }

    public void setID(Integer ID) {
        this.ID = ID;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }
}
