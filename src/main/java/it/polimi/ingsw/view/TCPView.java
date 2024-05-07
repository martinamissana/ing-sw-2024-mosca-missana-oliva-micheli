package it.polimi.ingsw.view;

import it.polimi.ingsw.controller.exceptions.GameAlreadyStartedException;
import it.polimi.ingsw.model.exceptions.FullLobbyException;
import it.polimi.ingsw.model.exceptions.LobbyDoesNotExistsException;
import it.polimi.ingsw.model.exceptions.NicknameAlreadyTakenException;
import it.polimi.ingsw.model.exceptions.PawnAlreadyTakenException;
import it.polimi.ingsw.model.game.Lobby;
import it.polimi.ingsw.model.player.Pawn;
import it.polimi.ingsw.model.player.Player;
import it.polimi.ingsw.network.netMessage.NetMessage;
import it.polimi.ingsw.network.netMessage.c2s.*;
import it.polimi.ingsw.network.netMessage.s2c.*;

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
    //private boolean reading;
    private NetMessage deserialized;

    public TCPView(String ip, int port) throws IOException {
        super();
        this.ip = ip;
        this.port = port;
        this.socket = new Socket(ip, port);
        this.in = new ObjectInputStream(socket.getInputStream());
        this.out = new ObjectOutputStream(socket.getOutputStream());
        //this.reading = false;
    }

    public void startClient() throws IOException, ClassNotFoundException {
        try {

            //NetMessage deserialized;

            do {
                //System.out.println("waiting for next message");
                deserialized = (NetMessage) in.readObject();

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

    private void elaborate(NetMessage message) throws IOException, FullLobbyException, NicknameAlreadyTakenException {
        //System.out.println("elaborate has been called");
        switch (message) {
            case LoginMessage m -> {
              //  System.out.println("you are logged in!");
            }
            case LobbyCreatedMessage m -> {
                //super.getLobbies().put(m.getID(),m.getLobby());
                if(m.getCreator().equals(super.getPlayer()))super.setID(m.getID());
                if(m.getCreator().equals(super.getPlayer()))System.out.println("your lobby was created,"+ m.getID() +" allowed players: "+ m.getLobby().getNumOfPlayers() );
             //   else System.out.println("A lobby was created, allowed players: "+ m.getLobby().getNumOfPlayers() );
                super.getLobbies().put(m.getID(),m.getLobby());
            }
            //TODO: REVIEW THIS WITH DISCONNECTION LOGIC
            case LoginFail_NicknameAlreadyTaken m -> {
               // System.out.println("you are not logged in, disconnection");
                DisconnectMessage disconnectMessage= new DisconnectMessage();
                out.writeObject(disconnectMessage);
            }
            case LobbyJoinedMessage m -> {
                if(m.getPlayer().getNickname().equals(super.getPlayer().getNickname())){
                //    System.out.println("you joined the lobby! "+ m.getID());
                    super.setID(m.getID());
                }
              //  else System.out.println("someone joined a lobby!");
                super.getLobbies().get(m.getID()).addPlayer( m.getPlayer());

            }
            case FailMessage m -> {
                super.getErrorMessages().add(m.getMessage());
              //  System.out.println(m.getMessage());
            }
            case LobbyLeftMessage m -> {
                if(m.getPlayer().getNickname().equals(super.getNickname())){
                    System.out.println("you left the lobby!");
                    super.setID(null);
                }
               // else {System.out.println("someone left a lobby !");}
                super.getLobbies().get(m.getID()).removePlayer(m.getPlayer());

            }
            case LobbyDeletedMessage m ->{
                super.getLobbies().remove(m.getID());
              //  System.out.println("a lobby was deleted!");
            }
            case PawnAssignedMessage m ->{
                if(m.getPlayer().getNickname().equals(super.getNickname())){
                        super.setPawn(m.getColor());
                      //  System.out.println("you were assigned pawn:"+ super.getPawn());
                    }
                    else{
                        super.getPawns().put(m.getPlayer(),m.getColor());
                      //  System.out.println(m.getPlayer().getNickname()+" was assigned"+ super.getPawn());
                    }
            }
            case CurrentStatusMessage m ->{
                super.getLobbies().putAll(m.getLobbies());
                for (Lobby l : super.getLobbies().values()){
                    for(Player p:l.getPlayers()){
                        if(p.getPawn()!=null) super.getPawns().put(p,p.getPawn());
                    }
                }
            }
            default -> throw new IllegalStateException("Unexpected value: " + message);
        }
    }

    public void login(String nickname) throws IOException, FullLobbyException, NicknameAlreadyTakenException {
        MyNickname m= new MyNickname(nickname);
        super.setPlayer(new Player(nickname));
        super.setNickname(nickname);
        out.writeObject(m);
        elaborate(deserialized);
     //   System.out.println("you tried to login");
    }

    @Override
    public void createLobby(int numOfPlayers) throws LobbyDoesNotExistsException, IOException, FullLobbyException, NicknameAlreadyTakenException {
        CreateLobbyMessage m=new CreateLobbyMessage(numOfPlayers,super.getPlayer());
        out.writeObject(m);
        elaborate(deserialized);
      //  System.out.println("you tried to create a lobby");
    }

    @Override
    public void joinLobby( int lobbyID) throws FullLobbyException, NicknameAlreadyTakenException, LobbyDoesNotExistsException, IOException {
        JoinLobbyMessage m = new JoinLobbyMessage(super.getPlayer(),lobbyID);
        System.out.println("JoinLobby");
        out.writeObject(m);
        elaborate(deserialized);
     //   System.out.println("you tried to join a lobby");
    }

    @Override
    public void leaveLobby() throws GameAlreadyStartedException, LobbyDoesNotExistsException, IOException, FullLobbyException, NicknameAlreadyTakenException {
        LeaveLobbyMessage m = new LeaveLobbyMessage(super.getPlayer(),super.getID());
        out.writeObject(m);
        elaborate(deserialized);
      //  System.out.println("you tried to leave a lobby");
    }

    @Override
    public void choosePawn(Pawn color) throws LobbyDoesNotExistsException, PawnAlreadyTakenException, IOException, FullLobbyException, NicknameAlreadyTakenException {
      //  System.out.println(this.getPlayer().getNickname()+" ID is "+this.getID());
        ChoosePawnMessage m= new ChoosePawnMessage(super.getID(),super.getPlayer(),color);
       // System.out.println("ChoosePawn");
        out.writeObject(m);
       // System.out.println("you tried to choose a pawn");
        elaborate(deserialized);
    }

    @Override
    public void getCurrentStatus() throws IOException, FullLobbyException, NicknameAlreadyTakenException {
        GetCurrentStatusMessage m =new GetCurrentStatusMessage();
        out.writeObject(m);
        elaborate(deserialized);
    }
}
