package it.polimi.ingsw.view;

import it.polimi.ingsw.model.exceptions.LobbyDoesNotExistsException;
import it.polimi.ingsw.model.player.Player;
import it.polimi.ingsw.network.netMessage.NetMessage;
import it.polimi.ingsw.network.netMessage.c2s.CreateLobbyMessage;
import it.polimi.ingsw.network.netMessage.c2s.DisconnectMessage;
import it.polimi.ingsw.network.netMessage.c2s.MyNickname;
import it.polimi.ingsw.network.netMessage.s2c.LobbyCreatedMessage;
import it.polimi.ingsw.network.netMessage.s2c.LoginFail_NicknameAlreadyTaken;
import it.polimi.ingsw.network.netMessage.s2c.LoginMessage;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class TCPView extends View {
    private String ip;
    private final int port;
    private final ObjectInputStream in;
    private final ObjectOutputStream out;
    private final Socket socket;

    public TCPView(String ip, int port) throws IOException {
        super();
        this.ip = ip;
        this.port = port;
        this.socket = new Socket(ip, port);
        this.in = new ObjectInputStream(socket.getInputStream());
        this.out = new ObjectOutputStream(socket.getOutputStream());
    }

    public void startClient() throws IOException, ClassNotFoundException {
        try {

            NetMessage deserialized;

            do {
                System.out.println("waiting for next message");
                deserialized = (NetMessage) in.readObject();
                elaborate(deserialized);
            } while (deserialized.getClass() != DisconnectMessage.class);

            in.close();
            out.close();
            socket.close();
        } catch (IOException e) {
            System.err.println(e.getMessage());
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    private void elaborate(NetMessage message) throws IOException {
        //System.out.println("elaborate has been called");
        switch (message) {
            case LoginMessage m -> {
                setNickname(m.getNickname());
                System.out.println("you are logged in!");
            }
            case LobbyCreatedMessage m -> {
                super.getLobbies().put(m.getID(),m.getLobby());
                if(m.getCreator()==super.getPlayer()) System.out.println("your lobby was created, allowed players: "+ m.getLobby().getNumOfPlayers() );
                else System.out.println("A lobby was created, allowed players: "+ m.getLobby().getNumOfPlayers() );
            }
            case LoginFail_NicknameAlreadyTaken m -> {
                System.out.println("you are not logged in, disconnection");
                DisconnectMessage disconnectMessage= new DisconnectMessage();
                out.writeObject(disconnectMessage);
            }
            default -> throw new IllegalStateException("Unexpected value: " + message);
        }
    }

    public void login(String nickname) throws IOException {
        MyNickname m= new MyNickname(nickname);
        super.setPlayer(new Player(nickname));
        out.writeObject(m);
        System.out.println("you tried to login");
    }

    @Override
    public void createLobby(int numOfPlayers) throws LobbyDoesNotExistsException, IOException {
        CreateLobbyMessage m=new CreateLobbyMessage(numOfPlayers,super.getPlayer());
        out.writeObject(m);
        System.out.println("you tried to create a lobby");
    }


}
