package it.polimi.ingsw.network.TCP;

import it.polimi.ingsw.controller.Controller;
import it.polimi.ingsw.model.exceptions.NicknameAlreadyTakenException;
import it.polimi.ingsw.model.observer.Observer;
import it.polimi.ingsw.model.observer.events.Event;
import it.polimi.ingsw.model.observer.events.LoginEvent;
import it.polimi.ingsw.network.netMessage.NetMessage;
import it.polimi.ingsw.network.netMessage.c2s.DisconnectMessage;
import it.polimi.ingsw.network.netMessage.c2s.MyNickname;
import it.polimi.ingsw.network.netMessage.s2c.LoginMessage;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class TCPClientHandler implements Runnable, Observer {
    private final Socket socket;
    private ObjectInputStream in;

    private ObjectOutputStream out;
    private  Controller c;
    public TCPClientHandler(Socket socket) {
        this.socket = socket;
    }
    public void run() {
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
        } catch (NicknameAlreadyTakenException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public void setC(Controller c) {
        this.c = c;
    }

    @Override
    public void update(Event event) throws IOException{
        if (socket.isConnected()){
            switch (event.getEventType()) {
                case LOGIN -> {
                    LoginMessage m = new LoginMessage(((LoginEvent) event).getNickname());
                    out.writeObject(m);
                }
            }
        }
    }
    private void elaborate(NetMessage message) throws InterruptedException, NicknameAlreadyTakenException, IOException {
        switch (message) {
            case MyNickname m -> {
                c.login(m.getNickname());
            }
            default -> throw new IllegalStateException("Unexpected value: " + message);
        }
    }
}
