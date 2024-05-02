package it.polimi.ingsw.view;

import it.polimi.ingsw.network.netMessage.NetMessage;
import it.polimi.ingsw.network.netMessage.c2s.DisconnectMessage;
import it.polimi.ingsw.network.netMessage.c2s.MyNickname;
import it.polimi.ingsw.network.netMessage.s2c.LoginMessage;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class TCPView extends View{
    private String ip;
    private final int port;
    private final ObjectInputStream in;
    private final ObjectOutputStream out;
    private final Socket socket;

    public TCPView(String ip, int port) throws IOException {
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
        System.out.println("elaborate has been called");
        switch (message) {
            case LoginMessage m -> {
                setNickname(m.getNickname());
                System.out.println("you are logged in!");
            }
            default -> throw new IllegalStateException("Unexpected value: " + message);
        }
    }

    public void login(String nickname) throws IOException {
        MyNickname m= new MyNickname(nickname);
        out.writeObject(m);
        System.out.println("you tried to login");
    }


}
