package it.polimi.ingsw.network.TCP;

import it.polimi.ingsw.controller.Controller;
import it.polimi.ingsw.controller.exceptions.CannotJoinMultipleLobbiesException;
import it.polimi.ingsw.model.exceptions.FullLobbyException;
import it.polimi.ingsw.model.exceptions.LobbyDoesNotExistsException;
import it.polimi.ingsw.model.exceptions.NicknameAlreadyTakenException;
import it.polimi.ingsw.model.observer.Observer;
import it.polimi.ingsw.model.observer.events.Event;
import it.polimi.ingsw.model.observer.events.LobbyCreatedEvent;
import it.polimi.ingsw.model.observer.events.LobbyJoinedEvent;
import it.polimi.ingsw.model.observer.events.LoginEvent;
import it.polimi.ingsw.network.netMessage.NetMessage;
import it.polimi.ingsw.network.netMessage.c2s.CreateLobbyMessage;
import it.polimi.ingsw.network.netMessage.c2s.DisconnectMessage;
import it.polimi.ingsw.network.netMessage.c2s.LobbyJoinedMessage;
import it.polimi.ingsw.network.netMessage.c2s.MyNickname;
import it.polimi.ingsw.network.netMessage.s2c.*;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class TCPVirtualView implements Runnable, Observer {
    private final Socket socket;
    private final ObjectInputStream in;

    private final ObjectOutputStream out;
    private  Controller c;
    public TCPVirtualView(Socket socket,Controller c)  throws IOException  {
        this.socket = socket;
        this.out = new ObjectOutputStream(socket.getOutputStream());
        this.in = new ObjectInputStream(socket.getInputStream());
        this.c=c;
        c.getGh().addObserver(this);

    }
    public void run() {
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
        } catch (NicknameAlreadyTakenException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } catch (LobbyDoesNotExistsException | CannotJoinMultipleLobbiesException | FullLobbyException e) {
            throw new RuntimeException(e);
        }
    }

    public void setC(Controller c) {
        this.c = c;
    }

    @Override
    public void update(Event event) throws IOException{
        //System.out.println("update has been called");
        if (socket.isConnected()){
            switch (event) {
                case LoginEvent e -> {
                    LoginMessage m = new LoginMessage(((LoginEvent) event).getNickname());
                    out.writeObject(m);
                    System.out.println("Update that a user logged in: " + m.getNickname());
                }
                case LobbyCreatedEvent e -> {
                    LobbyCreatedMessage m= new LobbyCreatedMessage(e.getCreator(),e.getLobby(),e.getID());
                    out.writeObject(m);
                    System.out.println("A lobby was created, allowed players: "+ e.getLobby().getNumOfPlayers() );
                }
                case LobbyJoinedEvent e -> {
                    LobbyJoinedMessage m= new LobbyJoinedMessage(e.getPlayer(),e.getID());
                    out.writeObject(m);
                    System.out.println(e.getPlayer()+"joined the lobby n."+ e.getID() );
                }
                default -> throw new IllegalStateException("Unexpected value: " + event);
            }
        }

    }
    private void elaborate(NetMessage message) throws InterruptedException, NicknameAlreadyTakenException, IOException, LobbyDoesNotExistsException, CannotJoinMultipleLobbiesException, FullLobbyException {
        //System.out.println("elaborate has been called");
        switch (message) {
            case MyNickname m -> {
                try{
                    c.login(m.getNickname());
                    System.out.println("Logged in: " + m.getNickname());
                }catch (NicknameAlreadyTakenException e){
                    LoginFail_NicknameAlreadyTaken errorMessage=new LoginFail_NicknameAlreadyTaken();
                    out.writeObject(m);
                    System.out.println("Couldn't log in: " + m.getNickname());
                }
            }
            case CreateLobbyMessage m ->{
                c.createLobby(m.getNumOfPlayers(),m.getCreator());
                System.out.println(m.getCreator().getNickname() +" created a lobby");
            }
            case JoinLobbyMessage m -> {
                try{
                    c.joinLobby(m.getPlayer(),m.getID());
                    System.out.println(m.getPlayer().getNickname() +" created a lobby");
                }catch (FullLobbyException e){
                    FailMessage failMessage=new FailMessage("you couldn't join the lobby because it was full");
                    out.writeObject(failMessage);
                }
                catch(LobbyDoesNotExistsException e){
                    FailMessage failMessage=new FailMessage("you couldn't join the lobby because it doesn't exist");
                    out.writeObject(failMessage);
                }

            }
            default -> throw new IllegalStateException("Unexpected value: " + message);
        }
    }
}
