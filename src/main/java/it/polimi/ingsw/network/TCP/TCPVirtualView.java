package it.polimi.ingsw.network.TCP;

import it.polimi.ingsw.controller.Controller;
import it.polimi.ingsw.controller.exceptions.CannotJoinMultipleLobbiesException;
import it.polimi.ingsw.controller.exceptions.GameAlreadyStartedException;
import it.polimi.ingsw.controller.exceptions.PlayerChatMismatchException;
import it.polimi.ingsw.controller.exceptions.UnexistentUserException;
import it.polimi.ingsw.model.deck.DeckBufferType;
import it.polimi.ingsw.model.exceptions.*;
import it.polimi.ingsw.model.observer.Observer;
import it.polimi.ingsw.model.observer.events.*;
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

    public TCPVirtualView(Socket socket, Controller c) throws IOException {
        this.socket = socket;
        this.out = new ObjectOutputStream(socket.getOutputStream());
        this.in = new ObjectInputStream(socket.getInputStream());
        this.c = c;
        c.getGh().addObserver(this);
    }

    public void run() {
        try {
            NetMessage deserialized;
            do {
                deserialized = (NetMessage) in.readObject();
                elaborate(deserialized);
            } while (deserialized.getClass() != DisconnectMessage.class);

            in.close();
            out.close();
            socket.close();
        } catch (IOException | ClassNotFoundException | GameAlreadyStartedException | GameDoesNotExistException |
                 InterruptedException | NicknameAlreadyTakenException | LobbyDoesNotExistsException |
                 CannotJoinMultipleLobbiesException | FullLobbyException | UnexistentUserException |
                 PlayerChatMismatchException e) {
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
                    ChatMessageAddedEvent m = new ChatMessageAddedEvent(e.getM(), e.getLobbyID());
                    out.writeObject(m);
                }
                case GameCreatedEvent e -> {
                    GameCreatedMessage m = new GameCreatedMessage(e.getID(),e.getFirstPlayer(),e.getScoreboard(),e.getTopResourceCard(),e.getTopGoldenCard(),e.getCommonGoal1(),e.getCommonGoal2(),e.getGamePhase(),e.getDeckBuffers().get(DeckBufferType.RES1),e.getDeckBuffers().get(DeckBufferType.RES2),e.getDeckBuffers().get(DeckBufferType.GOLD1),e.getDeckBuffers().get(DeckBufferType.GOLD2) );
                    out.writeObject(m);
                }
                default -> throw new IllegalStateException("Unexpected value: " + event);
            }
        }

    }

    private void elaborate(NetMessage message) throws InterruptedException, NicknameAlreadyTakenException, IOException, LobbyDoesNotExistsException, CannotJoinMultipleLobbiesException, FullLobbyException, GameAlreadyStartedException, GameDoesNotExistException, UnexistentUserException, PlayerChatMismatchException {
        switch (message) {
            case MyNickname m -> {
                try {
                    c.login(m.getNickname());
                } catch (NicknameAlreadyTakenException e) {
                    LoginFail_NicknameAlreadyTaken errorMessage = new LoginFail_NicknameAlreadyTaken();
                    out.writeObject(m);
                }
            }
            case CreateLobbyMessage m -> {
                try {
                    c.createLobby(m.getNumOfPlayers(), m.getCreator().getNickname());
                } catch (CannotJoinMultipleLobbiesException e) {
                    FailMessage failMessage = new FailMessage("you can't create a lobby when you are already in one", m.getCreator());
                    out.writeObject(failMessage);
                }
            }
            case JoinLobbyMessage m -> {
                try {
                    c.joinLobby(m.getPlayer().getNickname(), m.getID());
                } catch (FullLobbyException e) {
                    FailMessage failMessage = new FailMessage("you couldn't join the lobby because it was full", m.getPlayer());
                    out.writeObject(failMessage);
                } catch (LobbyDoesNotExistsException e) {
                    FailMessage failMessage = new FailMessage("you couldn't join the lobby because it doesn't exist", m.getPlayer());
                    out.writeObject(failMessage);
                } catch (CannotJoinMultipleLobbiesException e) {
                    FailMessage failMessage = new FailMessage("you can't join multiple lobbies", m.getPlayer());
                    out.writeObject(failMessage);
                } catch (UnexistentUserException e) {
                    throw new RuntimeException(e);
                }
            }
            case LeaveLobbyMessage m -> {
                try {
                    c.leaveLobby(m.getPlayer().getNickname(), m.getID());
                } catch (LobbyDoesNotExistsException e) {
                    FailMessage failMessage = new FailMessage("you can't leave an inexistant lobby", m.getPlayer());
                    out.writeObject(failMessage);
                }
            }
            case ChoosePawnMessage m -> {
                try {
                    c.choosePawn(m.getLobbyID(), m.getPlayer().getNickname(), m.getColor());
                } catch (PawnAlreadyTakenException e) {
                    FailMessage failMessage = new FailMessage("pawn already taken", m.getPlayer());
                    out.writeObject(failMessage);
                } catch (LobbyDoesNotExistsException | GameAlreadyStartedException | IOException |
                         GameDoesNotExistException e) {
                    FailMessage failMessage = new FailMessage(e.toString(), m.getPlayer());
                    out.writeObject(failMessage);
                }
            }
            case SendMessage m -> {
                try {
                    c.send(m.getM(), m.getLobbyID());
                } catch (GameDoesNotExistException | LobbyDoesNotExistsException | PlayerChatMismatchException |
                         UnexistentUserException e) {
                    FailMessage failMessage = new FailMessage(e.toString(), m.getM().getSender());
                    out.writeObject(failMessage);
                }

            }
            case GetCurrentStatusMessage m -> {
                CurrentStatusMessage status = new CurrentStatusMessage(c.getGh().getLobbies());
                out.writeObject(status);
            }
            default -> throw new IllegalStateException("Unexpected value: " + message);
        }
    }
}
