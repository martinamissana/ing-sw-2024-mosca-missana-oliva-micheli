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

/**
 * The virtual view on the server associated with a view on the client side
 */
public class TCPVirtualView implements Runnable, Observer {
    private final Socket socket;
    private final ObjectInputStream in;

    private final ObjectOutputStream out;
    private final Controller c;
    private Integer ID;
    private String nickname;


    /**
     * Class constructor
     *
     * @param socket the socket to communicate trough TCP
     * @param c      the linked Controller
     * @throws IOException produced by failed or interrupted I/O operations
     */
    public TCPVirtualView(Socket socket, Controller c) throws IOException {
        this.socket = socket;
        this.out = new ObjectOutputStream(socket.getOutputStream());
        this.in = new ObjectInputStream(socket.getInputStream());
        this.c = c;
        c.getGh().addObserver(this);
        new Thread(() -> {
            while (!socket.isClosed()) {
                try {
                    checkClientConnection();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }).start();
    }

    /**
     * getter
     *
     * @return the object input stream used to receive the messages
     */
    public ObjectInputStream getIn() {
        return in;
    }

    /**
     * getter
     *
     * @return the object output stream used to send the messages
     */
    public ObjectOutputStream getOut() {
        return out;
    }

    /**
     * getter
     *
     * @return the nickname linked to the virtual view
     */
    public String getNickname() {
        return nickname;
    }

    /**
     * getter
     *
     * @return the lobby/game ID linked to the virtual view
     */
    public Integer getID() {
        return ID;
    }

    /**
     * setter
     *
     * @param ID the lobby/game ID assigned to the virtual view
     */
    public void setID(Integer ID) {
        this.ID = ID;
    }

    /**
     * setter
     *
     * @param nickname the nickname assigned to the virtual view
     */
    public void setNickname(String nickname) {
        this.nickname = nickname;
    }


    @Override
    public void run() {
        try {
            NetMessage deserialized;
            do {
                deserialized = (NetMessage) in.readObject();
                elaborate(deserialized);
            } while (!socket.isClosed());

            Thread.currentThread().interrupt();

        } catch (IOException | ClassNotFoundException e) {
            try {
                disconnect();
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        }
    }


    @Override
    public void update(Event event) throws IOException {
        if (socket.isConnected()) {
            switch (event) {
                case LoginEvent e -> {
                    if (e.getNickname().equals(this.nickname)) {
                        LoginMessage m = new LoginMessage(e.getNickname());
                        out.writeObject(m);
                    }
                }
                case LobbyCreatedEvent e -> {
                    LobbyCreatedMessage m = new LobbyCreatedMessage(e.getCreator(), e.getLobby(), e.getID());
                    out.writeObject(m);
                }
                case LobbyJoinedEvent e -> {
                    LobbyJoinedMessage m = new LobbyJoinedMessage(e.getPlayer(), e.getID());
                    out.writeObject(m);
                }
                case CurrentStatusEvent ignored -> {
                }
                case LobbyLeftEvent e -> {
                    if (e.getPlayer().getNickname().equals(this.nickname)) this.ID = null;
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
                    if (e.getLobbyID().equals(ID) && (e.getM().isGlobal() || (e.getM().getSender().getNickname().equals(nickname) || e.getM().getReceiver().getNickname().equals(nickname)))) {
                        ChatMessageAddedMessage m = new ChatMessageAddedMessage(e.getM(), e.getLobbyID());
                        out.writeObject(m);
                    }
                }
                case GameCreatedEvent e -> {
                    if (e.getID().equals(ID)) {
                        GameCreatedMessage m = new GameCreatedMessage(e.getID(), e.getFirstPlayer(), e.getScoreboard(), e.getTopResourceCard(), e.getTopGoldenCard(), e.getCommonGoal1(), e.getCommonGoal2(), e.getGamePhase(), e.getDeckBuffers().get(DeckBufferType.RES1), e.getDeckBuffers().get(DeckBufferType.RES2), e.getDeckBuffers().get(DeckBufferType.GOLD1), e.getDeckBuffers().get(DeckBufferType.GOLD2));
                        out.writeObject(m);
                    }

                }
                case CardAddedToHandEvent e -> {
                    if (e.getID().equals(ID)) {
                        CardAddedToHandMessage m = new CardAddedToHandMessage(e.getPlayer(), e.getCard());
                        out.writeObject(m);
                    }
                }
                case CardRemovedFromHandEvent e -> {
                    if (e.getPlayer().getNickname().equals(nickname)) {
                        CardRemovedFromHandMessage m = new CardRemovedFromHandMessage(e.getPlayer(), e.getCard());
                        out.writeObject(m);
                    }
                }
                case CardPlacedOnFieldEvent e -> {
                    if (e.getID().equals(ID)) {
                        CardPlacedOnFieldMessage m = new CardPlacedOnFieldMessage(e.getCoords(), e.getID(), e.getCard(), e.getCard().getSide(), e.getNickname());
                        out.writeObject(m);
                    }
                }
                case GamePhaseChangedEvent e -> {
                    if (e.getID().equals(ID)) {
                        GamePhaseChangedMessage m = new GamePhaseChangedMessage(e.getID(), e.getGamePhase());
                        out.writeObject(m);
                    }
                }
                case SecretGoalsListAssignedEvent e -> {
                    if (e.getPlayer().getNickname().equals(nickname)) {
                        SecretGoalsListAssignedMessage m = new SecretGoalsListAssignedMessage(e.getList(), e.getPlayer());
                        out.writeObject(m);
                    }
                }
                case SecretGoalAssignedEvent e -> {
                    if (e.getPlayer().getNickname().equals(nickname)) {
                        SecretGoalAssignedMessage m = new SecretGoalAssignedMessage(e.getPlayer(), e.getGoal());
                        out.writeObject(m);
                    }
                }
                case GameActionSwitchedEvent e -> {
                    if (e.getID().equals(ID)) {
                        GameActionSwitchedMessage m = new GameActionSwitchedMessage(e.getID(), e.getAction());
                        out.writeObject(m);
                    }
                }
                case LastRoundStartedEvent e -> {
                    if (e.getID().equals(ID)) {
                        LastRoundStartedMessage m = new LastRoundStartedMessage(e.getID());
                        out.writeObject(m);
                    }
                }
                case TurnChangedEvent e -> {
                    if (e.getID().equals(ID)) {
                        TurnChangedMessage m = new TurnChangedMessage(e.getID(), e.getNickname(), e.isLastRound());
                        out.writeObject(m);
                    }
                }
                case GameWinnersAnnouncedEvent e -> {
                    if (e.getID().equals(ID)) {
                        GameWinnersAnnouncedMessage m = new GameWinnersAnnouncedMessage(e.getID(), e.getWinners(), e.getGoalsDone());
                        out.writeObject(m);
                    }
                }
                case GameTerminatedEvent e -> {
                    if (e.getID().equals(ID)) {
                        this.ID = null;
                        GameTerminatedMessage m = new GameTerminatedMessage(e.getID());
                        out.writeObject(m);
                    }
                }
                case CardDrawnFromSourceEvent e -> {
                    if (e.getID().equals(ID)) {
                        CardDrawnFromSourceMessage m = new CardDrawnFromSourceMessage(e.getID(), e.getType(), e.getCard());
                        out.writeObject(m);
                    }
                }
                case ScoreIncrementedEvent e -> {
                    if (e.getID().equals(ID)) {
                        ScoreIncrementedMessage m = new ScoreIncrementedMessage(e.getID(), e.getPlayer(), e.getPoints());
                        out.writeObject(m);
                    }
                }
                default -> throw new IllegalStateException("Unexpected value: " + event);
            }
        }

    }

    /**
     * elaborates the messages received from the client through the connection
     *
     * @param message the received message
     * @throws IOException produced by failed or interrupted I/O operations
     */
    private void elaborate(NetMessage message) throws IOException {
        switch (message) {
            case MyNickname m -> {
                try {
                    setNickname(m.getNickname());
                    c.login(m.getNickname());
                } catch (NicknameAlreadyTakenException e) {
                    if (m.getNickname().equals(this.nickname)) {
                        setNickname(null);
                        LoginFail_NicknameAlreadyTaken errorMessage = new LoginFail_NicknameAlreadyTaken();
                        out.writeObject(errorMessage);
                    }
                }
            }
            case CreateLobbyMessage m -> {
                try {
                    c.createLobby(m.getNumOfPlayers(), m.getCreator().getNickname());
                    setID(c.getGh().getNumOfLobbies() - 1);
                } catch (CannotJoinMultipleLobbiesException e) {
                    FailMessage failMessage = new FailMessage("Already in lobby", m.getCreator().getNickname());
                    out.writeObject(failMessage);
                } catch (LobbyDoesNotExistsException | UnexistentUserException e) {
                    e.printStackTrace();
                }
            }
            case JoinLobbyMessage m -> {
                try {
                    c.joinLobby(m.getPlayer().getNickname(), m.getID());
                    setID(m.getID());
                } catch (FullLobbyException e) {
                    FailMessage failMessage = new FailMessage("Full lobby", m.getPlayer().getNickname());
                    out.writeObject(failMessage);
                } catch (LobbyDoesNotExistsException e) {
                    FailMessage failMessage = new FailMessage("ID not found", m.getPlayer().getNickname());
                    out.writeObject(failMessage);
                } catch (CannotJoinMultipleLobbiesException e) {
                    FailMessage failMessage = new FailMessage("Already in lobby", m.getPlayer().getNickname());
                    out.writeObject(failMessage);
                } catch (UnexistentUserException e) {
                    e.printStackTrace();
                }
            }
            case LeaveLobbyMessage m -> {
                try {
                    c.leaveLobby(m.getPlayer().getNickname(), m.getID());
                    setID(null);
                } catch (LobbyDoesNotExistsException e) {
                    FailMessage failMessage = new FailMessage("ID not found", m.getPlayer().getNickname());
                    out.writeObject(failMessage);
                } catch (GameDoesNotExistException | UnexistentUserException e) {
                    e.printStackTrace();
                }
            }
            case ChoosePawnMessage m -> {
                try {
                    c.choosePawn(m.getLobbyID(), m.getPlayer().getNickname(), m.getColor());
                } catch (PawnAlreadyTakenException e) {
                    FailMessage failMessage = new FailMessage("Pawn taken", m.getPlayer().getNickname());
                    out.writeObject(failMessage);
                } catch (LobbyDoesNotExistsException | GameAlreadyStartedException | IOException |
                         GameDoesNotExistException e) {
                    FailMessage failMessage = new FailMessage(e.toString(), m.getPlayer().getNickname());
                    out.writeObject(failMessage);
                } catch (UnexistentUserException e) {
                    e.printStackTrace();
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
            case GetCurrentStatusMessage ignored -> {
                CurrentStatusMessage status = new CurrentStatusMessage(c.getGh().getLobbies(), "");
                out.writeObject(status);
            }
            case ChooseCardSideMessage m -> {
                try {
                    c.chooseCardSide(m.getID(), m.getNickname(), m.getSide());
                } catch (WrongGamePhaseException | EmptyDeckException | GameDoesNotExistException |
                         HandIsFullException | UnexistentUserException e) {
                    e.printStackTrace();
                }
            }
            case ChooseSecretGoalMessage m -> {
                try {
                    try {
                        c.chooseSecretGoal(m.getID(), m.getNickname(), m.getGoalID());
                    } catch (WrongGamePhaseException e) {
                        FailMessage failMessage = new FailMessage("Wrong phase", m.getNickname());
                        out.writeObject(failMessage);
                    } catch (GameDoesNotExistException | UnexistentUserException e) {
                        e.printStackTrace();
                    }
                } catch (IllegalGoalChosenException e) {
                    FailMessage failMessage = new FailMessage("Goal not valid", m.getNickname());
                    out.writeObject(failMessage);
                }
            }
            case PlayCardMessage m -> {
                try {
                    c.playCard(m.getGameID(), m.getNickname(), m.getHandPos(), m.getCoords(), m.getSide());
                } catch (IllegalActionException e) {
                    FailMessage failMessage = new FailMessage("Not play action", m.getNickname());
                    out.writeObject(failMessage);
                } catch (NotYourTurnException e) {
                    FailMessage failMessage = new FailMessage("Not your turn", m.getNickname());
                    out.writeObject(failMessage);
                } catch (IllegalMoveException e) {
                    if (e.getClass().equals(IllegalCoordsException.class)) {
                        FailMessage failMessage = new FailMessage("Illegal coords", m.getNickname());
                        out.writeObject(failMessage);
                    } else if (e.getClass().equals(RequirementsNotSatisfiedException.class)) {
                        FailMessage failMessage = new FailMessage("requirements not satisfied", m.getNickname());
                        out.writeObject(failMessage);
                    }
                } catch (LobbyDoesNotExistsException | GameDoesNotExistException | UnexistentUserException e) {
                    e.printStackTrace();
                }
            }
            case DrawCardMessage m -> {
                try {
                    c.drawCard(m.getGameID(), m.getNickname(), m.getDeckTypeBox());
                } catch (IllegalActionException e) {
                    FailMessage failMessage = new FailMessage("Not draw action", m.getNickname());
                    out.writeObject(failMessage);
                } catch (EmptyBufferException e) {
                    FailMessage failMessage = new FailMessage("Deck buffer empty", m.getNickname());
                    out.writeObject(failMessage);
                } catch (NotYourTurnException e) {
                    FailMessage failMessage = new FailMessage("Not your turn", m.getNickname());
                    out.writeObject(failMessage);
                } catch (EmptyDeckException e) {
                    FailMessage failMessage = new FailMessage("Deck empty", m.getNickname());
                    out.writeObject(failMessage);
                } catch (GameDoesNotExistException | UnexistentUserException | LobbyDoesNotExistsException e) {
                    e.printStackTrace();
                } catch (HandIsFullException e) {
                    FailMessage failMessage = new FailMessage("Hand full", m.getNickname());
                    out.writeObject(failMessage);
                }
            }
            case FlipCardMessage m -> {
                try {
                    c.flipCard(m.getGameID(), m.getNickname(), m.getHandPos());
                } catch (GameDoesNotExistException | UnexistentUserException e) {
                    e.printStackTrace();
                }
            }
            case HeartBeatMessage m -> {
            }
            default -> throw new IllegalStateException("Unexpected value: " + message);
        }
    }

    /**
     * called to send an HeartBeatMessage to check if the client is still alive,
     * if an IOException is caught the client will assume tha the client has crashed and will disconnect
     */
    public void checkClientConnection() throws IOException {
        HeartBeatMessage m = new HeartBeatMessage();
        try {
            Thread.sleep(3000);
            out.writeObject(m);
        } catch (IOException | InterruptedException e) {
            disconnect();
        }
    }

    /**
     * removes the virtual view from the observer, eventually makes the linked player leave the lobby and finally removes them from the user list,
     * it also interrupts the thread the virtual view is running on
     * @throws IOException produced by failed or interrupted I/O operations
     */
    private void disconnect() throws IOException {
        c.getGh().removeObserver(this);
        try {
            if (ID != null)
                c.leaveLobby(nickname, ID);
        } catch (LobbyDoesNotExistsException | GameDoesNotExistException | UnexistentUserException e) {
            throw new RuntimeException(e);
        }
        c.getGh().removeUser(nickname);
        socket.close();
        Thread.currentThread().interrupt();
    }
}
