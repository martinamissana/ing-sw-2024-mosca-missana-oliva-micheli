package it.polimi.ingsw.network.TCP;

import it.polimi.ingsw.controller.Controller;
import it.polimi.ingsw.controller.exceptions.CannotJoinMultipleLobbiesException;
import it.polimi.ingsw.controller.exceptions.GameAlreadyStartedException;
import it.polimi.ingsw.controller.exceptions.UnexistentUserException;
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
                //System.out.println("waiting for next message");
                deserialized = (NetMessage) in.readObject();
                elaborate(deserialized);
            } while (deserialized.getClass() != DisconnectMessage.class);

            in.close();
            out.close();
            socket.close();
        } catch (IOException e) {
          //  System.err.println(e.getMessage());
        } catch (ClassNotFoundException | GameAlreadyStartedException | GameDoesNotExistException |
                 InterruptedException | NicknameAlreadyTakenException | LobbyDoesNotExistsException |
                 CannotJoinMultipleLobbiesException | FullLobbyException | UnexistentUserException e) {
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
                  //  System.out.println("Update that a user logged in: " + m.getNickname());
                }
                case LobbyCreatedEvent e -> {
                    LobbyCreatedMessage m= new LobbyCreatedMessage(e.getCreator(),e.getLobby(),e.getID());
                    out.writeObject(m);
                //    System.out.println("A lobby was created, allowed players: "+ e.getLobby().getNumOfPlayers() );
                }
                case LobbyJoinedEvent e -> {
                    LobbyJoinedMessage m= new LobbyJoinedMessage(e.getPlayer(),e.getID());
                    out.writeObject(m);
                    //System.out.println(e.getPlayer().getNickname()+" joined the lobby n."+ e.getID() );
                }
                case LobbyLeftEvent e -> {
                    LobbyLeftMessage m =new LobbyLeftMessage(e.getPlayer(),e.getLobby(),e.getID());
                    out.writeObject(m);
                 //   System.out.println(e.getPlayer().getNickname()+" left the lobby n."+ e.getID() );
                }
                case LobbyDeletedEvent e -> {
                    LobbyDeletedMessage m =new LobbyDeletedMessage(e.getID());
                    out.writeObject(m);
                //    System.out.println(" deleted the lobby n."+ e.getID() );
                }
                case PawnAssignedEvent e -> {
                    PawnAssignedMessage m= new PawnAssignedMessage(e.getPlayer(),e.getColor());
                    out.writeObject(m);
                //    System.out.println("assigned "+e.getColor()+" to " + e.getPlayer().getNickname() );
                }
                default -> throw new IllegalStateException("Unexpected value: " + event);
            }
        }

    }
    private void elaborate(NetMessage message) throws InterruptedException, NicknameAlreadyTakenException, IOException, LobbyDoesNotExistsException, CannotJoinMultipleLobbiesException, FullLobbyException, GameAlreadyStartedException, GameDoesNotExistException, UnexistentUserException {
        switch (message) {
            case MyNickname m -> {
                try{
                    c.login(m.getNickname());
                    //System.out.println("Logged in: " + m.getNickname());
                }catch (NicknameAlreadyTakenException e){
                    LoginFail_NicknameAlreadyTaken errorMessage=new LoginFail_NicknameAlreadyTaken();
                    out.writeObject(m);
                    //System.out.println("Couldn't log in: " + m.getNickname());
                }
            }

            case CreateLobbyMessage m ->{
                try{
                    c.createLobby(m.getNumOfPlayers(),m.getCreator().getNickname());
                    // System.out.println(m.getCreator().getNickname() +" created a lobby");
                }catch(CannotJoinMultipleLobbiesException e){
                    FailMessage failMessage=new FailMessage("you can't create a lobby when you are already in one",m.getCreator());
                    out.writeObject(failMessage);
                }

            }
            case JoinLobbyMessage m -> {
                try{
                    c.joinLobby(m.getPlayer().getNickname(),m.getID());
                  //  System.out.println(m.getPlayer().getNickname() +" joined a lobby");
                }catch (FullLobbyException e){
                    FailMessage failMessage=new FailMessage("you couldn't join the lobby because it was full",m.getPlayer());
                    out.writeObject(failMessage);
                }
                catch(LobbyDoesNotExistsException e){
                    FailMessage failMessage=new FailMessage("you couldn't join the lobby because it doesn't exist",m.getPlayer());
                    out.writeObject(failMessage);
                }
                catch(CannotJoinMultipleLobbiesException e){
                    FailMessage failMessage=new FailMessage("you can't join multiple lobbies",m.getPlayer());
                    out.writeObject(failMessage);
                } catch (UnexistentUserException e) {
                    throw new RuntimeException(e);
                }
            }
            case LeaveLobbyMessage m -> {
                try{
                    c.leaveLobby(m.getPlayer().getNickname(), m.getID());
                    //System.out.println(m.getPlayer().getNickname() +" left a lobby");
                }catch (LobbyDoesNotExistsException e){
                    FailMessage failMessage=new FailMessage("you can't join an inexistant lobby",m.getPlayer());
                    out.writeObject(failMessage);
                }
            }
            case ChoosePawnMessage m -> {
                try{
                    c.choosePawn(m.getLobbyID(),m.getPlayer().getNickname(),m.getColor());
                }catch (PawnAlreadyTakenException e){
                    FailMessage failMessage=new FailMessage("pawn already taken",m.getPlayer());
                    out.writeObject(failMessage);
                }
                catch (LobbyDoesNotExistsException| GameAlreadyStartedException | IOException | GameDoesNotExistException e ){
                    FailMessage failMessage=new FailMessage(e.toString(),m.getPlayer());
                    out.writeObject(failMessage);
                }
            }
            case GetCurrentStatusMessage m -> {
                CurrentStatusMessage status=new CurrentStatusMessage(c.getGh().getLobbies());
                out.writeObject(status);
            }
            default -> throw new IllegalStateException("Unexpected value: " + message);
        }
    }
}
