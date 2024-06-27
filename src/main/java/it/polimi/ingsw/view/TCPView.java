package it.polimi.ingsw.view;

import it.polimi.ingsw.model.card.CardSide;
import it.polimi.ingsw.model.chat.Message;
import it.polimi.ingsw.model.deck.DeckTypeBox;
import it.polimi.ingsw.model.exceptions.NicknameAlreadyTakenException;
import it.polimi.ingsw.model.player.Coords;
import it.polimi.ingsw.model.player.Pawn;
import it.polimi.ingsw.model.player.Player;
import it.polimi.ingsw.network.netMessage.HeartBeatMessage;
import it.polimi.ingsw.network.netMessage.NetMessage;
import it.polimi.ingsw.network.netMessage.c2s.*;
import it.polimi.ingsw.network.netMessage.s2c.JoinLobbyMessage;
import it.polimi.ingsw.network.netMessage.s2c.LoginFail_NicknameAlreadyTaken;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

/**
 * TCP View Class
 * extends the abstract class View
 * the methods represent the action that the client can do
 */
public class TCPView extends View {
    private String ip;
    private final int port;
    private final ObjectInputStream in;
    private final ObjectOutputStream out;
    private final Socket socket;

    /**
     * Class Constructor, connects the client to the port of the server
     *
     * @param ip   ip address of the server
     * @param port port of the server
     * @throws IOException general class of exceptions produced by failed or interrupted I/O operations
     */
    public TCPView(String ip, int port) throws IOException {
        super();
        this.ip = ip;
        this.port = port;
        this.socket = new Socket(ip, port);
        this.in = new ObjectInputStream(socket.getInputStream());
        this.out = new ObjectOutputStream(socket.getOutputStream());
        new Thread(() -> {
            while (!socket.isClosed()) {
                heartbeat();
            }
        }).start();
    }

    /**
     * starts the connection and waits for messages from server
     *
     * @throws IOException            general class of exceptions produced by failed or interrupted I/O operations
     * @throws ClassNotFoundException thrown when an application tries to load in a class through its string name but no definition for the class with the specified name could be found
     */
    public void startClient() throws IOException, ClassNotFoundException {
        try {

            NetMessage deserialized;
            do {
                deserialized = (NetMessage) in.readObject();
                elaborate(deserialized);
            } while (!socket.isClosed());
            disconnect(super.getNickname());
        } catch (IOException | ClassNotFoundException e) {
            disconnect(super.getNickname());

        } catch ( NicknameAlreadyTakenException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void login(String nickname) throws IOException {
        LoginRequestMessage m = new LoginRequestMessage(nickname);
        super.setPlayer(new Player(nickname));
        super.setNickname(nickname);
        out.writeObject(m);
    }

    @Override
    public void createLobby(int numOfPlayers) throws IOException {
        CreateLobbyMessage m = new CreateLobbyMessage(numOfPlayers, super.getPlayer());
        out.writeObject(m);
    }

    @Override
    public void joinLobby(int lobbyID) throws IOException {
        JoinLobbyMessage m = new JoinLobbyMessage(super.getPlayer(), lobbyID);
        out.writeObject(m);
    }

    @Override
    public void leaveLobby() throws IOException {
        LeaveLobbyMessage m = new LeaveLobbyMessage(super.getPlayer(), super.getID());
        out.writeObject(m);
    }

    @Override
    public void choosePawn(Pawn color) throws IOException {
        ChoosePawnMessage m = new ChoosePawnMessage(super.getID(), super.getPlayer(), color);
        out.writeObject(m);
    }

    @Override
    public void chooseSecretGoal(int goalID) throws IOException {
        ChooseSecretGoalMessage m = new ChooseSecretGoalMessage(super.getID(), super.getNickname(), goalID);
        out.writeObject(m);
    }

    @Override
    public void getCurrentStatus() throws IOException {
        GetCurrentStatusMessage m = new GetCurrentStatusMessage();
        out.writeObject(m);
    }

    @Override
    public void sendMessage(Message message) throws IOException {
        SendMessage m = new SendMessage(message, super.getID());
        out.writeObject(m);
    }

    @Override
    public void chooseCardSide(CardSide side) throws IOException {
        ChooseCardSideMessage m = new ChooseCardSideMessage(super.getID(), super.getNickname(), side);
        out.writeObject(m);
    }

    @Override
    public void playCard(int handPos, Coords coords, CardSide side) throws IOException {
        PlayCardMessage m = new PlayCardMessage(super.getID(), super.getNickname(), handPos, coords, side);
        out.writeObject(m);
    }

    @Override
    public void drawCard(DeckTypeBox deckTypeBox) throws IOException {
        DrawCardMessage m = new DrawCardMessage(super.getID(), super.getNickname(), deckTypeBox);
        out.writeObject(m);
    }

    @Override
    public void heartbeat() {
        HeartBeatMessage m = new HeartBeatMessage();
        try {
            Thread.sleep(3000);
            out.writeObject(m);
        } catch (IOException e) {
            try {
                disconnect(super.getNickname());
            } catch (IOException ignored) {
            }
        } catch (InterruptedException ignored) {
        }

    }

    @Override
    public void disconnect(String nickname) throws IOException {
        in.close();
        out.close();
        socket.close();
        Thread.currentThread().interrupt();
        if (super.getNickname() != null) notify(new DisconnectMessage());
        else notify(new LoginFail_NicknameAlreadyTaken());
    }


}
