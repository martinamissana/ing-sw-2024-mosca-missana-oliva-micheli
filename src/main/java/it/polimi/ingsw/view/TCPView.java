package it.polimi.ingsw.view;

import it.polimi.ingsw.network.netMessage.NetMessage;
import it.polimi.ingsw.network.netMessage.c2s.DisconnectMessage;
import it.polimi.ingsw.network.netMessage.s2c.LoginMessage;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class TCPView extends View{
    private String ip;
    private final int port;
    private ObjectInputStream in;
    private ObjectOutputStream out;
    private final Socket socket;

    public TCPView(String ip, int port) throws IOException {
        this.ip = ip;
        this.port = port;
        this.socket = new Socket(ip, port);

    }

    public void startClient() throws IOException, ClassNotFoundException {
        try {
            ObjectInputStream in = new ObjectInputStream(socket.getInputStream());
            ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
            NetMessage deserialized;
// Leggo e scrivo nella connessione finche' non ricevo "quit"
            do {
                in.readObject();
                deserialized = (NetMessage) in.readObject();
                elaborate(deserialized);
            } while (deserialized.getClass() != DisconnectMessage.class);

// Chiudo gli stream e il socket
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
        switch (message) {
            case LoginMessage m -> {
                setNickname(m.getNickname());
            }
            default -> throw new IllegalStateException("Unexpected value: " + message);
        }
    }

    public void login(String nickname) throws IOException {
        LoginMessage m= new LoginMessage(nickname);
        out.writeObject(m);
    }


}
