package it.polimi.ingsw.view;

import it.polimi.ingsw.controller.exceptions.*;
import it.polimi.ingsw.model.card.Card;
import it.polimi.ingsw.model.card.CardSide;
import it.polimi.ingsw.model.card.ResourceCard;
import it.polimi.ingsw.model.card.StarterCard;
import it.polimi.ingsw.model.chat.Chat;
import it.polimi.ingsw.model.chat.Message;
import it.polimi.ingsw.model.deck.DeckBuffer;
import it.polimi.ingsw.model.deck.DeckBufferType;
import it.polimi.ingsw.model.deck.DeckType;
import it.polimi.ingsw.model.deck.DeckTypeBox;
import it.polimi.ingsw.model.exceptions.*;
import it.polimi.ingsw.model.game.Action;
import it.polimi.ingsw.model.game.GamePhase;
import it.polimi.ingsw.model.game.Lobby;
import it.polimi.ingsw.model.goal.Goal;
import it.polimi.ingsw.model.player.*;
import it.polimi.ingsw.network.netMessage.HeartBeatMessage;
import it.polimi.ingsw.network.netMessage.NetMessage;
import it.polimi.ingsw.network.netMessage.c2s.LobbyJoinedMessage;
import it.polimi.ingsw.network.netMessage.s2c.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

public abstract class View extends ViewObservable<NetMessage> {
    private String nickname;
    private HashMap<Integer, Lobby> lobbies = new HashMap<>();
    private Player player;
    private Integer ID; // both game and lobby ID
    private Hand hand = new Hand();
    private Field myField = new Field();
    private HashMap<Player, Field> fields = new HashMap<>(); // all other players' fields
    private boolean yourTurn = false;
    private Action action;
    private Chat chat = new Chat();
    private Goal secretGoal;
    private Pawn pawn;
    private boolean firstPlayer = false;
    private boolean lastRound;
    private ArrayList<Goal> secretGoalChoices = new ArrayList<>();
    private HashMap<Player, Integer> scoreboard = new HashMap<>();
    private HashMap<DeckBufferType, DeckBuffer> deckBuffers = new HashMap<>();
    private Card topResourceCard;
    private Card topGoldenCard;
    private Goal commonGoal1;
    private Goal commonGoal2;
    private GamePhase gamePhase;
    private ArrayList<Player> winners = new ArrayList<>();
    private final ArrayList<String> errorMessages = new ArrayList<>();

    public View() {
    }

    public String getNickname() { return nickname; }

    public HashMap<DeckBufferType, DeckBuffer> getDeckBuffers() { return deckBuffers; }

    public Card getTopResourceCard() { return topResourceCard; }

    public HashMap<Integer, Lobby> getLobbies(){ return lobbies; }

    public Player getPlayer() { return player; }

    public Integer getID() { return ID; }

    public Hand getHand() { return hand; }

    public Field getMyField() { return myField; }

    public HashMap<Player, Field> getFields() { return fields; }

    public boolean isYourTurn() { return yourTurn; }

    public Action getAction() { return action; }

    public Chat getChat() { return chat; }

    public Goal getSecretGoal() { return secretGoal; }

    public ArrayList<Player> getWinners() { return winners; }

    public ArrayList<String> getErrorMessages() { return errorMessages; }

    public Pawn getPawn() { return pawn; }

    public boolean isFirstPlayer() { return firstPlayer; }

    public boolean isLastRound() { return lastRound; }

    public ArrayList<Goal> getSecretGoalChoices() { return secretGoalChoices; }

    public HashMap<Player, Integer> getScoreboard() { return scoreboard; }

    public GamePhase getGamePhase() { return gamePhase; }

    public Card getTopGoldenCard() { return topGoldenCard; }

    public Goal getCommonGoal1() { return commonGoal1; }

    public Goal getCommonGoal2() { return commonGoal2; }


    public void setPlayer(Player player) { this.player = player; }

    public void setID(Integer ID) { this.ID = ID; }

    public void setHand(Hand hand) { this.hand = hand; }

    public void setMyField(Field myField) { this.myField = myField; }

    public void setFields(HashMap<Player, Field> fields) { this.fields = fields; }

    public void setLobbies(HashMap<Integer, Lobby> lobbies) { this.lobbies = lobbies; }

    public void setYourTurn(boolean yourTurn) { this.yourTurn = yourTurn; }

    public void setAction(Action action) { this.action = action; }

    public void setChat(Chat chat) { this.chat = chat; }

    public void setSecretGoal(Goal secretGoal) { this.secretGoal = secretGoal; }

    public void setPawn(Pawn pawn) { this.pawn = pawn; }

    public void setFirstPlayer(boolean firstPlayer) { this.firstPlayer = firstPlayer; }

    public void setLastRound(boolean lastRound) { this.lastRound = lastRound; }

    public void setSecretGoalChoices(ArrayList<Goal> secretGoalChoices) { this.secretGoalChoices = secretGoalChoices; }

    public void setScoreboard(HashMap<Player, Integer> scoreboard) { this.scoreboard = scoreboard; }

    public void setGamePhase(GamePhase gamePhase) { this.gamePhase = gamePhase; }

    public void setDeckBuffers(HashMap<DeckBufferType, DeckBuffer> deckBuffers) { this.deckBuffers = deckBuffers; }

    public void setCardInDeckBuffer(DeckBufferType deckBufferType, Card card) {
        DeckBuffer d = new DeckBuffer(null);
        d.setCard((ResourceCard) card);
        this.deckBuffers.put(deckBufferType, d);
    }

    public void setTopResourceCard(Card topResourceCard) { this.topResourceCard = topResourceCard; }

    public void setTopGoldenCard(Card topGoldenCard) { this.topGoldenCard = topGoldenCard; }

    public void setCommonGoal1(Goal commonGoal1) { this.commonGoal1 = commonGoal1; }

    public void setCommonGoal2(Goal commonGoal2) { this.commonGoal2 = commonGoal2; }

    public void setNickname(String nickname) { this.nickname = nickname; }

    public void setWinners(ArrayList<Player> winners) {
        this.winners = winners;
    }

    public abstract void login(String nickname) throws NicknameAlreadyTakenException, IOException, FullLobbyException, ClassNotFoundException;

    public abstract void createLobby(int numOfPlayers) throws LobbyDoesNotExistsException, IOException, FullLobbyException, NicknameAlreadyTakenException, ClassNotFoundException, CannotJoinMultipleLobbiesException, UnexistentUserException;

    public abstract void joinLobby(int lobbyID) throws FullLobbyException, NicknameAlreadyTakenException, LobbyDoesNotExistsException, IOException, ClassNotFoundException, CannotJoinMultipleLobbiesException, UnexistentUserException;

    public abstract void leaveLobby() throws GameAlreadyStartedException, LobbyDoesNotExistsException, IOException, FullLobbyException, NicknameAlreadyTakenException, ClassNotFoundException, GameDoesNotExistException, UnexistentUserException;

    public abstract void sendMessage(Message message) throws IOException, LobbyDoesNotExistsException, GameDoesNotExistException, UnexistentUserException, PlayerChatMismatchException;

    public abstract void choosePawn(Pawn color) throws PawnAlreadyTakenException, IOException, LobbyDoesNotExistsException, GameAlreadyStartedException, GameDoesNotExistException, UnexistentUserException;

    public abstract void chooseSecretGoal(int goalID) throws IOException, IllegalGoalChosenException, WrongGamePhaseException, GameDoesNotExistException, UnexistentUserException;

    public abstract void chooseCardSide(CardSide side) throws IOException, EmptyDeckException, GameDoesNotExistException, HandIsFullException, UnexistentUserException, WrongGamePhaseException;

    public abstract void playCard(int handPos, Coords coords) throws IllegalActionException, NotYourTurnException, IllegalMoveException, GameDoesNotExistException, LobbyDoesNotExistsException, UnexistentUserException, IOException;

    public abstract void drawCard(DeckTypeBox deckTypeBox) throws IllegalActionException, EmptyBufferException, NotYourTurnException, EmptyDeckException, GameDoesNotExistException, HandIsFullException, LobbyDoesNotExistsException, IOException, UnexistentUserException;

    public abstract void getCurrentStatus() throws IOException, FullLobbyException, NicknameAlreadyTakenException, ClassNotFoundException;

    public abstract void heartbeat() throws IOException, ClassNotFoundException;

    public abstract void disconnect() throws IOException;

    public void elaborate(NetMessage message) throws IOException, FullLobbyException, NicknameAlreadyTakenException, HandIsFullException, IllegalMoveException {
        switch (message) {
            case LoginMessage m -> {
                if(Objects.equals(m.getNickname(), nickname)){
                    notify(m);
                }
            }
            case LoginFail_NicknameAlreadyTaken m -> {
                Thread.currentThread().interrupt();
                notify(message);
                disconnect();
            }
            case LobbyCreatedMessage m -> {
                lobbies.put(m.getID(), m.getLobby());
                if (m.getCreator().equals(player)) {
                    ID=m.getID();
                    notify(m);
                }
            }
            case LobbyJoinedMessage m -> {
                if (m.getPlayer().getNickname().equals(player.getNickname()))
                    ID=m.getID();
                try {
                    getLobbies().get(m.getID()).addPlayer(m.getPlayer());
                } catch (FullLobbyException e) {
                    e.printStackTrace();
                }
                if ( m.getID() != null && m.getID().equals(ID))
                    notify(message);
            }
            case LobbyLeftMessage m -> {
                if (m.getPlayer().getNickname().equals(nickname)) {
                    ID=null;
                    pawn=null;
                    notify(m);
                }
                lobbies.get(m.getID()).removePlayer(m.getPlayer());
                scoreboard.remove(m.getPlayer());
                if (pawn != null && m.getID() != null && m.getID().equals(ID))
                    notify(message);
            }
            case LobbyDeletedMessage m -> {
               lobbies.remove(m.getID());
            }
            case PawnAssignedMessage m -> {
                if (m.getPlayer().getNickname().equals(nickname)) {
                    pawn=m.getColor();
                    notify(message);
                } else {
                    for (Player p : lobbies.get(m.getLobbyID()).getPlayers()) {
                        if (p.equals(m.getPlayer())) p.setPawn(m.getColor());
                    }
                }
            }
            case CurrentStatusMessage m -> {
                lobbies.putAll(m.getLobbies());
            }
            case ChatMessageAddedMessage m -> {
                if (m.getM().getSender().equals(player)) {
                    chat.getSentMessages().add(m.getM());
                } else if (m.getM().isGlobal() || m.getM().getReceiver().equals(player)) {
                    chat.getReceivedMessages().add(m.getM());
                }
                if (m.getLobbyID() != null && m.getLobbyID().equals(ID)) notify(message);
            }
            case GameCreatedMessage m -> {
                if (m.getID().equals(ID)) {
                    if (m.getFirstPlayer().equals(player)) {
                        firstPlayer=true;
                        yourTurn=true;
                    } else {
                        firstPlayer=false;
                        yourTurn=false;
                    }
                    scoreboard=m.getScoreboard();
                    deckBuffers=m.getDeckBuffers();
                    topResourceCard=m.getTopResourceCard();
                    topGoldenCard=m.getTopGoldenCard();
                    commonGoal1=m.getCommonGoal1();
                    commonGoal2=m.getCommonGoal2();
                    gamePhase=m.getGamePhase();
                    action=m.getAction();
                    notify(message);
                }
            }
            case CardAddedToHandMessage m -> {
                if (m.getPlayer().equals(player)) {
                    try {
                        hand.addCard(m.getCard());
                    } catch (HandIsFullException e) {
                        e.printStackTrace();
                    }
                }
            }
            case CardRemovedFromHandMessage m -> {
                if (m.getPlayer().equals(player)) {
                    hand.removeCard(m.getCard());
                }
            }
            case CardPlacedOnFieldMessage m -> {
                if (m.getNickname().equals(nickname)) {
                    if (m.getCard().getClass().equals(StarterCard.class)) {
                        myField.addCard((StarterCard) m.getCard());
                        notify(m);
                    } else try {
                        myField.addCard((ResourceCard) m.getCard(), m.getCoords());
                        notify(message);
                    } catch (IllegalMoveException e) {
                        e.printStackTrace();
                    }
                } else {
                    for (Player p : fields.keySet()) {
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
            }
            case GamePhaseChangedMessage m -> {
                if (m.getID().equals(ID)) {
                    gamePhase=m.getGamePhase();
                    notify(m);
                }
            }
            case SecretGoalsListAssignedMessage m -> {
                if (m.getPlayer().equals(player))
                    secretGoalChoices=m.getList();
            }
            case SecretGoalAssignedMessage m -> {
                if (m.getPlayer().equals(player))
                    secretGoal=m.getGoal();
            }
            case GameActionSwitchedMessage m -> {
                if (m.getID().equals(ID))
                    action=m.getAction();
            }
            case LastRoundStartedMessage m -> {
                if (m.getID().equals(ID))
                    lastRound=true;
            }
            case TurnChangedMessage m -> {
                if (m.getID().equals(ID) && m.getNickname().equals(nickname))
                    yourTurn=true;
                else if (m.getID().equals(ID) && !m.getNickname().equals(nickname))
                    yourTurn=false;
                if (m.getID().equals(ID)) notify(m);
            }
            case GameWinnersAnnouncedMessage m -> {
                if (m.getID().equals(ID)) {
                    winners=m.getWinners();
                    notify(m);
                }
            }
            case GameTerminatedMessage m -> {
                if (m.getID().equals(ID)) {
                    lobbies.remove(ID);
                    firstPlayer=false;
                    yourTurn=false;
                    scoreboard=null;
                    deckBuffers=null;
                    topResourceCard=null;
                    topGoldenCard=null;
                    commonGoal1=null;
                    commonGoal2=null;
                    gamePhase=null;
                    action=null;
                    ID=null;
                    pawn=null;
                    notify(m);
                }
            }
            case ScoreIncrementedMessage m -> {
                if (ID != null && ID.equals(m.getID())) {
                    scoreboard.put(m.getPlayer(), getScoreboard().get(m.getPlayer()) + m.getPoints());
                }
            }
            case CardDrawnFromSourceMessage m -> {
                if (m.getID().equals(ID)) {
                    switch (m.getType()) {
                        case DeckType.RESOURCE -> topResourceCard=m.getCard();
                        case DeckType.GOLDEN -> topGoldenCard=m.getCard();
                        case DeckBufferType.RES1 -> {
                            DeckBuffer d = new DeckBuffer(null);
                            d.setCard((ResourceCard) m.getCard());
                            this.deckBuffers.put(DeckBufferType.RES1, d);
                        }
                        case DeckBufferType.RES2 -> {
                            DeckBuffer d = new DeckBuffer(null);
                            d.setCard((ResourceCard) m.getCard());
                            this.deckBuffers.put(DeckBufferType.RES2, d);
                        }
                        case DeckBufferType.GOLD1 -> {
                            DeckBuffer d = new DeckBuffer(null);
                            d.setCard((ResourceCard) m.getCard());
                            this.deckBuffers.put(DeckBufferType.GOLD1, d);
                        }
                        case DeckBufferType.GOLD2 -> {
                            DeckBuffer d = new DeckBuffer(null);
                            d.setCard((ResourceCard) m.getCard());
                            this.deckBuffers.put(DeckBufferType.GOLD2, d);
                        }
                        default -> throw new IllegalStateException("Unexpected value: " + m.getType());
                    }
                }
            }
            case HeartBeatMessage m -> {
            }
            default -> throw new IllegalStateException("Unexpected value: " + message);
        }
    }
}