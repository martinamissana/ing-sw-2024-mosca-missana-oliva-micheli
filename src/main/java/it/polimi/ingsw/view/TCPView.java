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

    /**
     * called when the client wants to log in, it creates MyNickname message and sends it to the server
     *
     * @param nickname name of the player
     * @throws IOException general class of exceptions produced by failed or interrupted I/O operations
     */
    @Override
    public void login(String nickname) throws IOException {
        MyNickname m = new MyNickname(nickname);
        super.setPlayer(new Player(nickname));
        super.setNickname(nickname);
        out.writeObject(m);
    }

    /**
     * called when someone wants to create a lobby, it creates CreateLobbyMessage and sends it to the server
     *
     * @param numOfPlayers number of players of the lobby
     * @throws IOException general class of exceptions produced by failed or interrupted I/O operations
     */
    @Override
    public void createLobby(int numOfPlayers) throws IOException {
        CreateLobbyMessage m = new CreateLobbyMessage(numOfPlayers, super.getPlayer());
        out.writeObject(m);
    }

    /**
     * called when the client wants to join a specified lobby, it creates JoinLobbyMessage and sends it to the server
     *
     * @param lobbyID ID of the lobby to join
     * @throws IOException general class of exceptions produced by failed or interrupted I/O operations
     */
    @Override
    public void joinLobby(int lobbyID) throws IOException {
        JoinLobbyMessage m = new JoinLobbyMessage(super.getPlayer(), lobbyID);
        out.writeObject(m);
    }

    /**
     * called when the client wants to leave the lobby, it creates LeaveLobbyMessage and sends it to the server
     *
     * @throws IOException general class of exceptions produced by failed or interrupted I/O operations
     */
    @Override
    public void leaveLobby() throws IOException {
        LeaveLobbyMessage m = new LeaveLobbyMessage(super.getPlayer(), super.getID());
        out.writeObject(m);
    }

    /**
     * called to choose the color of the pawn, it creates ChoosePawnMessage and sends it to the server
     *
     * @param color color of the pawn chosen
     * @throws IOException general class of exceptions produced by failed or interrupted I/O operations
     */
    @Override
    public void choosePawn(Pawn color) throws IOException {
        ChoosePawnMessage m = new ChoosePawnMessage(super.getID(), super.getPlayer(), color);
        out.writeObject(m);
    }

    /**
     * called to choose the secret goal, it creates ChooseSecretGoal and sends it to the server
     *
     * @param goalID ID of the goal chosen
     * @throws IOException general class of exceptions produced by failed or interrupted I/O operations
     */
    @Override
    public void chooseSecretGoal(int goalID) throws IOException {
        ChooseSecretGoalMessage m = new ChooseSecretGoalMessage(super.getID(), super.getNickname(), goalID);
        out.writeObject(m);
    }

    /**
     * called to update the client on the lobbies created before their login, it creates GetCurrentStatusMessage and sends it to the server
     *
     * @throws IOException general class of exceptions produced by failed or interrupted I/O operations
     */
    @Override
    public void getCurrentStatus() throws IOException {
        GetCurrentStatusMessage m = new GetCurrentStatusMessage();
        out.writeObject(m);
    }

    /**
     * called to send a message in chat, it creates SendMessage and sends it to the server
     *
     * @param message message containing the sender, receiver and text of the chat message
     * @throws IOException general class of exceptions produced by failed or interrupted I/O operations
     */
    @Override
    public void sendMessage(Message message) throws IOException {
        SendMessage m = new SendMessage(message, super.getID());
        out.writeObject(m);
    }

    /**
     * called to choose the side for the starter card to place it in the field, it creates ChooseCardMessage and sends it to the server
     *
     * @param side [FRONT - BACK] side of the starter card
     * @throws IOException general class of exceptions produced by failed or interrupted I/O operations
     */
    @Override
    public void chooseCardSide(CardSide side) throws IOException {
        ChooseCardSideMessage m = new ChooseCardSideMessage(super.getID(), super.getNickname(), side);
        out.writeObject(m);
    }

    /**
     * called to place a card in the field, it creates PlayCardMessage and sends it to the server
     *
     * @param handPos position of the card in the player hand
     * @param coords  coordinates in the field where the client wants to place the card
     * @throws IOException general class of exceptions produced by failed or interrupted I/O operations
     */
    @Override
    public void playCard(int handPos, Coords coords, CardSide side) throws IOException {
        PlayCardMessage m = new PlayCardMessage(super.getID(), super.getNickname(), handPos, coords, side);
        out.writeObject(m);
    }

    /**
     * called to draw a card from a specified deck/deckBuffer, it creates DrawCardMessage and sends it to the server
     *
     * @param deckTypeBox deck/deckBuffer the player wants to draw from
     * @throws IOException general class of exceptions produced by failed or interrupted I/O operations
     */
    @Override
    public void drawCard(DeckTypeBox deckTypeBox) throws IOException {
        DrawCardMessage m = new DrawCardMessage(super.getID(), super.getNickname(), deckTypeBox);
        out.writeObject(m);
    }

    /**
     * called to send an HeartBeatMessage to check if the server is still alive.
     * if an IOException is caught the client will assume tha the server has crashed and will disconnect
     */
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

    /**
     * called to disconnect the client, it closes the socket and interrupts the current thread notifying the client
     *
     * @throws IOException general class of exceptions produced by failed or interrupted I/O operations
     */
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
