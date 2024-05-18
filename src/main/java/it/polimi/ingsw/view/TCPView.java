package it.polimi.ingsw.view;

import it.polimi.ingsw.model.card.CardSide;
import it.polimi.ingsw.model.card.ResourceCard;
import it.polimi.ingsw.model.card.StarterCard;
import it.polimi.ingsw.model.chat.Message;
import it.polimi.ingsw.model.deck.DeckBufferType;
import it.polimi.ingsw.model.deck.DeckType;
import it.polimi.ingsw.model.deck.DeckTypeBox;
import it.polimi.ingsw.model.exceptions.FullLobbyException;
import it.polimi.ingsw.model.exceptions.HandIsFullException;
import it.polimi.ingsw.model.exceptions.IllegalMoveException;
import it.polimi.ingsw.model.player.Coords;
import it.polimi.ingsw.model.player.Pawn;
import it.polimi.ingsw.model.player.Player;
import it.polimi.ingsw.network.netMessage.HeartBeatMessage;
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
        new Thread(() -> {
            while (!socket.isClosed()) {
                checkServerConnection();
            }
        }).start();
    }


    public void startClient() throws IOException, ClassNotFoundException {
        try {

            NetMessage deserialized;
            do {
                deserialized = (NetMessage) in.readObject();
                elaborate(deserialized);
            } while (!socket.isClosed());

        } catch (IOException | ClassNotFoundException e) {
            System.err.println(e.getMessage());

        }
    }

    private void elaborate(NetMessage message) throws IOException {
        switch (message) {
            case LoginMessage m -> {
                notify(message);
            }
            case LoginFail_NicknameAlreadyTaken m -> {
                disconnect();
                Thread.currentThread().interrupt();
                notify(message);
            }
            case LobbyCreatedMessage m -> {
                if (m.getCreator().equals(super.getPlayer())) {
                    super.setID(m.getID());
                    notify(message);
                }
                super.getLobbies().put(m.getID(), m.getLobby());
            }
            case LobbyJoinedMessage m -> {
                if (m.getPlayer().getNickname().equals(super.getPlayer().getNickname()))
                    super.setID(m.getID());
                try {
                    getLobbies().get(m.getID()).addPlayer(m.getPlayer());
                } catch (FullLobbyException e) {
                    e.printStackTrace();
                }
                if (m.getID() != null && m.getID().equals(super.getID())) notify(message);
            }
            case LobbyLeftMessage m -> {
                if (m.getID() != null && m.getID().equals(super.getID())) notify(message);
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
                if (m.getPlayer().getNickname().equals(super.getNickname())) {
                    super.setPawn(m.getColor());
                    notify(message);
                } else {
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
                if (m.getLobbyID() != null && m.getLobbyID().equals(super.getID())) notify(message);
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
                    notify(message);
                }
            }
            case CardAddedToHandMessage m -> {
                if (m.getPlayer().equals(super.getPlayer())) {
                    try {
                        super.getHand().addCard(m.getCard());
                        notify(message);
                    } catch (HandIsFullException e) {
                        e.printStackTrace();
                    }
                }
            }
            case CardRemovedFromHandMessage m -> {
                if (m.getPlayer().equals(super.getPlayer())) {
                    super.getHand().removeCard(m.getCard());
                    notify(message);
                }
            }
            case CardPlacedOnFieldMessage m -> {
                if (m.getNickname().equals(super.getNickname())) {
                    if (m.getCard().getClass().equals(StarterCard.class))
                        super.getMyField().addCard((StarterCard) m.getCard());
                    else try {
                        super.getMyField().addCard((ResourceCard) m.getCard(), m.getCoords());
                    } catch (IllegalMoveException e) {
                        e.printStackTrace();
                    }
                } else {
                    for (Player p : super.getFields().keySet()) {
                        if (m.getNickname().equals(p.getNickname())) {
                            if (m.getCard().getClass().equals(StarterCard.class))
                                p.getField().addCard((StarterCard) m.getCard());
                            else try {
                                p.getField().addCard((ResourceCard) m.getCard(), m.getCoords());
                            } catch (IllegalMoveException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }
                if (m.getID() != null && m.getID().equals(super.getID())) notify(message);
            }
            case GamePhaseChangedMessage m -> {
                if (m.getID().equals(super.getID())) {
                    super.setGamePhase(m.getGamePhase());
                    notify(m);
                }
            }
            case SecretGoalsListAssignedMessage m -> {
                if (m.getPlayer().equals(super.getPlayer())) {
                    super.setSecretGoalChoices(m.getList());
                    notify(m);
                }
            }
            case SecretGoalAssignedMessage m -> {
                if (m.getPlayer().equals(super.getPlayer())) {
                    super.setSecretGoal(m.getGoal());
                    notify(m);
                }
            }
            case GameActionSwitchedMessage m -> {
                if (m.getID().equals(super.getID())) {
                    super.setAction(m.getAction());
                    notify(m);
                }
            }
            case LastRoundStartedMessage m -> {
                if (m.getID().equals(super.getID())) {
                    super.setLastRound(true);
                    notify(m);
                }
            }
            case TurnChangedMessage m -> {
                if (m.getID().equals(super.getID()) && m.getNickname().equals(super.getNickname()))
                    super.setYourTurn(true);
                else if (m.getID().equals(super.getID()) && !m.getNickname().equals(super.getNickname()))
                    super.setYourTurn(false);
                if (m.getID().equals(super.getID())) notify(m);
            }
            case GameWinnersAnnouncedMessage m -> {
                if (m.getID().equals(super.getID())) {
                    super.setWinners(m.getWinners());
                    notify(m);
                }
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
                    notify(m);
                }
            }
            case CardDrawnFromSourceMessage m -> {
                if (m.getID().equals(super.getID())) {
                    switch (m.getType()) {
                        case DeckType.RESOURCE -> super.setTopResourceCard(m.getCard());
                        case DeckType.GOLDEN -> super.setTopGoldenCard(m.getCard());
                        case DeckBufferType.RES1 -> super.setCardInDeckBuffer(DeckBufferType.RES1, m.getCard());
                        case DeckBufferType.RES2 -> super.setCardInDeckBuffer(DeckBufferType.RES2, m.getCard());
                        case DeckBufferType.GOLD1 -> super.setCardInDeckBuffer(DeckBufferType.GOLD1, m.getCard());
                        case DeckBufferType.GOLD2 -> super.setCardInDeckBuffer(DeckBufferType.GOLD2, m.getCard());
                        default -> throw new IllegalStateException("Unexpected value: " + m.getType());
                    }
                    notify(m);
                }
            }
            case ScoreIncrementedMessage m -> {
                if (super.getID() != null && super.getID().equals(m.getID())) {
                    super.getScoreboard().put(m.getPlayer(), getScoreboard().get(m.getPlayer()) + m.getPoints());
                    notify(m);
                }
            }
            case FailMessage m -> {
                super.getErrorMessages().add(m.getMessage());
                notify(m);
            }
            case HeartBeatMessage m -> {
            }
            default -> throw new IllegalStateException("Unexpected value: " + message);
        }

    }

    @Override
    public void login(String nickname) throws IOException {
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
    public void chooseSecretGoal(int goalID) throws IOException {
        ChooseSecretGoalMessage m = new ChooseSecretGoalMessage(super.getID(), super.getNickname(), goalID);
        out.writeObject(m);
    }

    @Override
    public void getCurrentStatus() throws IOException {
        GetCurrentStatusMessage m = new GetCurrentStatusMessage();
        out.writeObject(m);
    }

    @Override
    public void sendMessage(Message message) throws IOException {
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
    public void drawCard(DeckTypeBox deckTypeBox) throws IOException {
        DrawCardMessage m = new DrawCardMessage(super.getID(), super.getNickname(), deckTypeBox);
        out.writeObject(m);
    }

    @Override
    public void flipCard(int handPos) throws IOException {
        FlipCardMessage m = new FlipCardMessage(super.getID(), super.getNickname(), handPos);
        out.writeObject(m);
        super.getHand().getCard(handPos).flip();
    }

    @Override
    public void heartbeat() throws IOException {
        HeartBeatMessage m = new HeartBeatMessage();
        out.writeObject(m);
    }

    public void disconnect() throws IOException {
        in.close();
        out.close();
        socket.close();
        Thread.currentThread().interrupt();
        notify(new DisconnectMessage());
    }

    public void checkServerConnection() {
        HeartBeatMessage m = new HeartBeatMessage();
        try {
            Thread.sleep(3000);
            out.writeObject(m);
        } catch (IOException e) {
            try {
                disconnect();
            } catch (IOException ignored) {
            }
        } catch (InterruptedException ignored) {
        }
    }

}
