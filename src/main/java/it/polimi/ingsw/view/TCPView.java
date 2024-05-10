package it.polimi.ingsw.view;

import it.polimi.ingsw.controller.exceptions.IllegalActionException;
import it.polimi.ingsw.controller.exceptions.NotYourTurnException;
import it.polimi.ingsw.model.card.CardSide;
import it.polimi.ingsw.model.card.ResourceCard;
import it.polimi.ingsw.model.chat.Message;
import it.polimi.ingsw.model.deck.DeckBufferType;
import it.polimi.ingsw.model.deck.DeckType;
import it.polimi.ingsw.model.deck.DeckTypeBox;
import it.polimi.ingsw.model.exceptions.*;
import it.polimi.ingsw.model.player.Coords;
import it.polimi.ingsw.model.player.Pawn;
import it.polimi.ingsw.model.player.Player;
import it.polimi.ingsw.network.netMessage.NetMessage;
import it.polimi.ingsw.network.netMessage.c2s.*;
import it.polimi.ingsw.network.netMessage.s2c.*;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.rmi.RemoteException;

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
        } catch (ClassNotFoundException | FullLobbyException | NicknameAlreadyTakenException | HandIsFullException |
                 IllegalMoveException e) {
            throw new RuntimeException(e);
        }
    }

    private void elaborate(NetMessage message) throws IOException, FullLobbyException, NicknameAlreadyTakenException, HandIsFullException, IllegalMoveException {
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
                    //System.out.println("your lobby was created," + m.getID() + " allowed players: " + m.getLobby().getNumOfPlayers());
                }// else System.out.println("A lobby was created, allowed players: " + m.getLobby().getNumOfPlayers());
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
                    } else {
                        super.setFirstPlayer(false);
                        super.setYourTurn(false);
                    }
                    super.setScoreboard(m.getScoreboard());
                    super.setDeckBuffers(m.getDeckBuffers());
                    super.setTopResourceCard(m.getTopResourceCard());
                    super.setTopGoldenCard(m.getTopGoldenCard());
                    super.setCommonGoal1(m.getCommonGoal1());
                    super.setCommonGoal2(m.getCommonGoal2());
                    super.setGamePhase(m.getGamePhase());
                    super.setAction(m.getAction());
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
            case CardPlacedOnFieldMessage m -> {
                if (m.getNickname().equals(super.getNickname())) {
                    super.getMyField().addCard((ResourceCard) m.getCard(), m.getCoords());
                } else {
                    for (Player p : super.getFields().keySet()) {
                        if (m.getNickname().equals(p.getNickname()))
                            p.getField().addCard((ResourceCard) m.getCard(), m.getCoords());
                    }
                }
            }
            case GamePhaseChangedMessage m -> {
                if (m.getID().equals(super.getID()))
                    super.setGamePhase(m.getGamePhase());
            }
            case PersonalGoalsListAssignedMessage m -> {
                if (m.getPlayer().equals(super.getPlayer()))
                    super.setPersonalGoalChoices(m.getList());
            }
            case PersonalGoalAssignedMessage m -> {
                if (m.getPlayer().equals(super.getPlayer()))
                    super.setPrivateGoal(m.getGoal());
            }
            case GameActionSwitchedMessage m -> {
                if (m.getID().equals(super.getID()))
                    super.setAction(m.getAction());
            }
            case LastRoundStartedMessage m -> {
                if (m.getID().equals(super.getID()))
                    super.setLastRound(true);
            }
            case TurnChangedMessage m -> {
                if (m.getID().equals(super.getID()) && m.getNickname().equals(super.getNickname()))
                    super.setYourTurn(true);
                else if (m.getID().equals(super.getID()) && !m.getNickname().equals(super.getNickname()))
                    super.setYourTurn(false);
            }
            case GameWinnersAnnouncedMessage m -> {
                if (m.getID().equals(super.getID()))
                    super.setWinners(m.getWinners());
            }
            case GameTerminatedMessage m -> {
                if (m.getID().equals(super.getID())) {
                    super.setFirstPlayer(false);
                    super.setYourTurn(false);
                    super.setScoreboard(null);
                    super.setDeckBuffers(null);
                    super.setTopResourceCard(null);
                    super.setTopGoldenCard(null);
                    super.setCommonGoal1(null);
                    super.setCommonGoal2(null);
                    super.setGamePhase(null);
                    super.setAction(null);
                    super.setID(null);
                    super.setPawn(null);
                }
            }
            case CardDrawnFromSourceMessage m -> {
                if (m.getID().equals(super.getID())) {
                    if (m.getType().equals(DeckType.RESOURCE))
                        super.setTopResourceCard(m.getCard());
                    if (m.getType().equals(DeckType.GOLDEN))
                        super.setTopGoldenCard(m.getCard());
                    else super.setCardInDeckBuffer((DeckBufferType) m.getType(), m.getCard());
                }
            }
            case FailMessage m -> {
                super.getErrorMessages().add(m.getMessage());
            }
            default -> throw new IllegalStateException("Unexpected value: " + message);
        }
        notify(message);
    }

    @Override
    public void login(String nickname) throws NicknameAlreadyTakenException, IOException {
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
    public void choosePersonalGoal(int goalID) throws IOException {
        ChoosePersonalGoalMessage m = new ChoosePersonalGoalMessage(super.getID(), super.getNickname(), goalID);
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
        ChooseCardSideMessage m = new ChooseCardSideMessage(super.getID(), super.getNickname(), side);
        out.writeObject(m);
    }

    @Override
    public void playCard(int handPos, Coords coords) throws IOException {
        PlayCardMessage m = new PlayCardMessage(super.getID(), super.getNickname(), handPos, coords);
        out.writeObject(m);
    }

    @Override
    public void drawCard(DeckTypeBox deckTypeBox) throws IllegalActionException, EmptyBufferException, NotYourTurnException, EmptyDeckException, GameDoesNotExistException, HandIsFullException {

    }

    @Override
    public void flipCard(int handPos) throws GameDoesNotExistException {

    }

    @Override
    public void heartbeat() throws RemoteException {

    }
}
