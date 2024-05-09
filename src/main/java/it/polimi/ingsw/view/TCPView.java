package it.polimi.ingsw.view;

import it.polimi.ingsw.model.card.CardSide;
import it.polimi.ingsw.model.chat.Message;
import it.polimi.ingsw.model.exceptions.FullLobbyException;
import it.polimi.ingsw.model.exceptions.HandIsFullException;
import it.polimi.ingsw.model.exceptions.NicknameAlreadyTakenException;
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
                deserialized = (NetMessage) in.readObject();
                elaborate(deserialized);

            } while (deserialized.getClass() != DisconnectMessage.class);

            in.close();
            out.close();
            socket.close();
        } catch (IOException e) {
            System.err.println(e.getMessage());
        } catch (ClassNotFoundException | FullLobbyException | NicknameAlreadyTakenException | HandIsFullException e) {
            throw new RuntimeException(e);
        }
    }

    private void elaborate(NetMessage message) throws IOException, FullLobbyException, NicknameAlreadyTakenException, HandIsFullException {
        switch (message) {
            case LoginMessage m -> {
            }
            //TODO: REVIEW THIS WITH DISCONNECTION LOGIC
            case LoginFail_NicknameAlreadyTaken m -> {
                DisconnectMessage disconnectMessage = new DisconnectMessage();
                out.writeObject(disconnectMessage);
            }
            case LobbyCreatedMessage m -> {
                if (m.getCreator().equals(super.getPlayer())) {
                    super.setID(m.getID());
                    System.out.println("your lobby was created," + m.getID() + " allowed players: " + m.getLobby().getNumOfPlayers());
                } else System.out.println("A lobby was created, allowed players: " + m.getLobby().getNumOfPlayers());
                super.getLobbies().put(m.getID(), m.getLobby());
            }
            case LobbyJoinedMessage m -> {
                if (m.getPlayer().getNickname().equals(super.getPlayer().getNickname()))
                    super.setID(m.getID());
                super.getLobbies().get(m.getID()).addPlayer(m.getPlayer());
            }
            case LobbyLeftMessage m -> {
                if (m.getPlayer().getNickname().equals(super.getNickname())) {
                    super.setID(null);
                    super.setPawn(null);
                }
                super.getLobbies().get(m.getID()).removePlayer(m.getPlayer());
            }
            case LobbyDeletedMessage m -> {
                super.getLobbies().remove(m.getID());
            }
            case PawnAssignedMessage m -> {
                if (m.getPlayer().getNickname().equals(super.getNickname()))
                    super.setPawn(m.getColor());
                else {
                    for (Player p : super.getLobbies().get(m.getLobbyID()).getPlayers()) {
                        if (p.equals(m.getPlayer())) p.setPawn(m.getColor());
                    }
                }
            }
            case CurrentStatusMessage m -> {
                super.getLobbies().putAll(m.getLobbies());
            }
            case ChatMessageAddedMessage m -> {
                if (m.getM().getSender().equals(super.getPlayer())) {
                    super.getChat().getSentMessages().add(m.getM());
                } else if (m.getM().isGlobal() || m.getM().getReceiver().equals(super.getPlayer())) {
                    super.getChat().getReceivedMessages().add(m.getM());
                }
            }
            case GameCreatedMessage m -> {
                if (m.getID().equals(super.getID())) {
                    if (m.getFirstPlayer().equals(super.getPlayer())) {
                        super.setFirstPlayer(true);
                        super.setYourTurn(true);
                    }
                    super.setScoreboard(m.getScoreboard());
                    super.setDeckBuffers(m.getDeckBuffers());
                    super.setTopResourceCard(m.getTopResourceCard());
                    super.setTopGoldenCard(m.getTopGoldenCard());
                    super.setCommonGoal1(m.getCommonGoal1());
                    super.setCommonGoal2(m.getCommonGoal2());
                    super.setGamePhase(m.getGamePhase());
                }
            }
            case CardAddedToHandMessage m -> {
                if (m.getPlayer().equals(super.getPlayer())) {
                    super.getHand().addCard(m.getCard());
                }
            }
            case CardRemovedFromHandMessage m -> {
                if (m.getPlayer().equals(super.getPlayer())) {
                    super.getHand().removeCard(m.getCard());
                }
            }
            case FailMessage m -> {
                super.getErrorMessages().add(m.getMessage());
            }
            default -> throw new IllegalStateException("Unexpected value: " + message);
        }
        notify(message);
    }

    public void login(String nickname) throws IOException, FullLobbyException, NicknameAlreadyTakenException, ClassNotFoundException {
        MyNickname m = new MyNickname(nickname);
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
    public void getCurrentStatus() throws IOException {
        GetCurrentStatusMessage m = new GetCurrentStatusMessage();
        out.writeObject(m);
    }

    @Override
    public synchronized void sendMessage(Message message) throws IOException {
        SendMessage m = new SendMessage(message, super.getID());
        out.writeObject(m);
    }

    @Override
    public void chooseCardSide(CardSide side) throws IOException {
        ChooseCardSideMessage m =new ChooseCardSideMessage(super.getID(),super.getNickname(),side);
        out.writeObject(m);
    }
}
