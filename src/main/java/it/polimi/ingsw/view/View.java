package it.polimi.ingsw.view;

import it.polimi.ingsw.controller.exceptions.*;
import it.polimi.ingsw.model.card.*;
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

/**
 * View class, it contains what the client can see and the methods the client can call to play
 */
public abstract class View extends ViewObservable {
    private String nickname;
    private HashMap<Integer, Lobby> lobbies = new HashMap<>();
    private Player player;
    private Integer ID; // both game and lobby ID
    private Hand hand = new Hand();
    private Field myField = new Field();
    private final HashMap<Player, Field> fields = new HashMap<>(); // all other players' fields
    private boolean yourTurn = false;
    private Action action;
    private Chat chat = new Chat();
    private Goal secretGoal;
    private Pawn pawn;
    private boolean lastRound = false;
    private ArrayList<Goal> secretGoalChoices = new ArrayList<>();
    private HashMap<Player, Integer> scoreboard = new HashMap<>();
    private HashMap<DeckBufferType, DeckBuffer> deckBuffers = new HashMap<>();
    private Card topResourceCard;
    private Card topGoldenCard;
    private Goal commonGoal1;
    private Goal commonGoal2;
    private GamePhase gamePhase;
    private ArrayList<Player> winners = new ArrayList<>();
    private HashMap<Player, Integer> goalsDone = new HashMap<>();


    /**
     * gets the nickname
     *
     * @return String nickname
     */
    public String getNickname() {
        return nickname;
    }

    /**
     * gets the deckBuffers
     *
     * @return HashMap <DeckBufferType, DeckBuffer>
     */
    public HashMap<DeckBufferType, DeckBuffer> getDeckBuffers() {
        return deckBuffers;
    }

    /**
     * gets the top card of the resource deck
     *
     * @return Card
     */
    public Card getTopResourceCard() {
        return topResourceCard;
    }

    /**
     * gets the lobbies
     *
     * @return HashMap <Integer, Lobby>
     */
    public HashMap<Integer, Lobby> getLobbies() {
        return lobbies;
    }

    /**
     * gets the player
     *
     * @return Player
     */
    public Player getPlayer() {
        return player;
    }

    /**
     * gets the ID of the lobby/game
     *
     * @return Integer
     */
    public Integer getID() {
        return ID;
    }

    /**
     * gets the hand of the player
     *
     * @return Hand
     */
    public Hand getHand() {
        return hand;
    }

    /**
     * gets the player field
     *
     * @return Field
     */
    public Field getMyField() {
        return myField;
    }

    /**
     * gets the other players' fields
     *
     * @return HashMap <Player, Field>
     */
    public HashMap<Player, Field> getFields() {
        return fields;
    }

    /**
     * returns true if it's the player turn, false if it's the turn of another player
     *
     * @return boolean
     */
    public boolean isYourTurn() {
        return yourTurn;
    }

    /**
     * gets the action [PLAY - DRAW]
     *
     * @return Action
     */
    public Action getAction() {
        return action;
    }

    /**
     * gets the chat
     *
     * @return Chat
     */
    public Chat getChat() {
        return chat;
    }

    /**
     * gets the player's secret goal
     *
     * @return Goal
     */
    public Goal getSecretGoal() {
        return secretGoal;
    }


    /**
     * gets the player's pawn
     *
     * @return Pawn
     */
    public Pawn getPawn() {
        return pawn;
    }


    /**
     * returns true if it's the last round of the match
     *
     * @return boolean
     */
    public boolean isLastRound() {
        return lastRound;
    }

    /**
     * gets an Arraylist with the two possible choices of the secret goal
     *
     * @return Arraylist <Goal>
     */
    public ArrayList<Goal> getSecretGoalChoices() {
        return secretGoalChoices;
    }

    /**
     * gets the player's scoreboard
     *
     * @return Hashmap <Player, Integer>
     */
    public HashMap<Player, Integer> getScoreboard() {
        return scoreboard;
    }

    /**
     * gets the game phase
     *
     * @return GamePhase
     */
    public GamePhase getGamePhase() {
        return gamePhase;
    }

    /**
     * gets the top card of the golden deck
     *
     * @return Card
     */
    public Card getTopGoldenCard() {
        return topGoldenCard;
    }

    /**
     * gets the first common goal of the game
     *
     * @return Goal
     */
    public Goal getCommonGoal1() {
        return commonGoal1;
    }

    /**
     * gets the second common goal of the game
     *
     * @return Goal
     */
    public Goal getCommonGoal2() {
        return commonGoal2;
    }

    public HashMap<Player, Integer> getGoalsDone() {
        return goalsDone;
    }

    /**
     * sets the player
     *
     * @param player user
     */
    public void setPlayer(Player player) {
        this.player = player;
    }

    /**
     * sets the ID of the lobby/game
     *
     * @param ID lobby/game
     */
    public void setID(Integer ID) {
        this.ID = ID;
    }

    public void setHand(Hand hand) {
        this.hand = hand;
    }


    public void setLobbies(HashMap<Integer, Lobby> lobbies) {
        this.lobbies = lobbies;
    }


    /**
     * sets the Action [PLAY - DRAW]
     *
     * @param action which action the player should perform
     */
    public void setAction(Action action) {
        this.action = action;
    }

    /**
     * sets the chat
     *
     * @param chat chat
     */
    public void setChat(Chat chat) {
        this.chat = chat;
    }

    /**
     * sets the pawn color
     *
     * @param pawn color of the pawn
     */
    public void setPawn(Pawn pawn) {
        this.pawn = pawn;
    }


    public void setScoreboard(HashMap<Player, Integer> scoreboard) {
        this.scoreboard = scoreboard;
    }

    /**
     * sets the nickname
     *
     * @param nickname name of the player
     */
    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public abstract void login(String nickname) throws NicknameAlreadyTakenException, IOException, FullLobbyException, ClassNotFoundException;

    public abstract void createLobby(int numOfPlayers) throws LobbyDoesNotExistException, IOException, FullLobbyException, NicknameAlreadyTakenException, ClassNotFoundException, CannotJoinMultipleLobbiesException, UnexistentUserException;

    public abstract void joinLobby(int lobbyID) throws FullLobbyException, NicknameAlreadyTakenException, LobbyDoesNotExistException, IOException, ClassNotFoundException, CannotJoinMultipleLobbiesException, UnexistentUserException;

    public abstract void leaveLobby() throws GameAlreadyStartedException, LobbyDoesNotExistException, IOException, FullLobbyException, NicknameAlreadyTakenException, ClassNotFoundException, GameDoesNotExistException, UnexistentUserException;

    public abstract void sendMessage(Message message) throws IOException, LobbyDoesNotExistException, GameDoesNotExistException, UnexistentUserException, PlayerChatMismatchException;

    public abstract void choosePawn(Pawn color) throws PawnAlreadyTakenException, IOException, LobbyDoesNotExistException, GameAlreadyStartedException, GameDoesNotExistException, UnexistentUserException;

    public abstract void chooseSecretGoal(int goalID) throws IOException, IllegalGoalChosenException, WrongGamePhaseException, GameDoesNotExistException, UnexistentUserException;

    public abstract void chooseCardSide(CardSide side) throws IOException, EmptyDeckException, GameDoesNotExistException, HandIsFullException, UnexistentUserException, WrongGamePhaseException;

    public abstract void playCard(int handPos, Coords coords, CardSide side) throws IllegalActionException, NotYourTurnException, IllegalMoveException, GameDoesNotExistException, LobbyDoesNotExistException, UnexistentUserException, IOException;

    public abstract void drawCard(DeckTypeBox deckTypeBox) throws IllegalActionException, EmptyBufferException, NotYourTurnException, EmptyDeckException, GameDoesNotExistException, HandIsFullException, LobbyDoesNotExistException, IOException, UnexistentUserException;

    public abstract void getCurrentStatus() throws IOException, FullLobbyException, NicknameAlreadyTakenException, ClassNotFoundException;

    public abstract void heartbeat() throws IOException, ClassNotFoundException;

    public abstract void disconnect(String nickname) throws IOException;

    /**
     * receives the messages from the server, notifies TUI and GUI and modifies the view
     *
     * @param message network message received from the server
     * @throws IOException                   general class of exceptions produced by failed or interrupted I/O operations
     * @throws NicknameAlreadyTakenException thrown if the nickname is already taken
     */
    public void elaborate(NetMessage message) throws IOException, NicknameAlreadyTakenException {
        switch (message) {
            case LoginSuccessMessage m -> {
                if (nickname.equals(m.getNickname())) {
                    notify(m);
                }
            }
            case LoginFail_NicknameAlreadyTaken m -> {
                Thread.currentThread().interrupt();
                nickname = null;
                disconnect(null);
            }
            case LobbyCreatedMessage m -> {
                lobbies.put(m.getID(), m.getLobby());
                lobbies.get(m.getID()).getPlayers().getFirst().initialize();

                if (m.getCreator().equals(player)) {
                    initialize();
                    player.initialize();
                    ID = m.getID();
                    notify(m);
                }
                if (ID == null) notify(m);
            }
            case LobbyJoinedMessage m -> {
                if (m.getPlayer().getNickname().equals(player.getNickname())) {
                    initialize();
                    player.initialize();
                    ID = m.getID();
                    gamePhase = null;
                }
                try {
                    getLobbies().get(m.getID()).addPlayer(m.getPlayer());
                    lobbies.get(m.getID()).getPlayers().getLast().initialize();
                } catch (FullLobbyException e) {
                    e.printStackTrace();
                }

                if (m.getID().equals(ID) || ID == null) notify(m);
            }
            case LobbyLeftMessage m -> {
                for (Player p : lobbies.get(m.getID()).getPlayers())
                    if (p.equals(m.getPlayer()) && p.getPawn() != null) {
                        p.setPawn(null);
                        p.initialize();
                    }

                lobbies.get(m.getID()).removePlayer(m.getPlayer());

                if (scoreboard != null) scoreboard.remove(m.getPlayer());

                if (m.getPlayer().getNickname().equals(nickname)) {
                    initialize();
                    notify(m);

                } else if (m.getID().equals(ID) || ID == null) {
                    if (gamePhase != null) {
                        pawn = null;
                        player.setPawn(null);
                    }
                    notify(m);
                }
            }
            case LobbyDeletedMessage m -> {
                lobbies.remove(m.getID());
                if (ID == null)
                    notify(message);
            }
            case PawnAssignedMessage m -> {
                for (Player p : lobbies.get(m.getID()).getPlayers()) {
                    if (p.equals(m.getPlayer())) p.setPawn(m.getColor());
                }
                if (m.getPlayer().getNickname().equals(nickname)) {
                    pawn = m.getColor();
                    notify(message);
                } else if (m.getID().equals(ID)) {
                    notify(message);
                }
            }
            case CurrentStatusMessage m -> {
                lobbies.putAll(m.getLobbies());
                notify(m);
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
                    for (Player p : lobbies.get(ID).getPlayers())
                        fields.put(p, new Field());
                    if (m.getFirstPlayer().equals(player)) {
                        yourTurn = true;
                    } else yourTurn = false;
                    scoreboard = m.getScoreboard();
                    deckBuffers = m.getDeckBuffers();
                    topResourceCard = m.getTopResourceCard();
                    topGoldenCard = m.getTopGoldenCard();
                    commonGoal1 = m.getCommonGoal1();
                    commonGoal2 = m.getCommonGoal2();
                    gamePhase = m.getGamePhase();
                    action = m.getAction();
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
                if (!m.getSide().equals(m.getCard().getSide())) m.getCard().flip();

                if (m.getNickname().equals(nickname)) {
                    if (m.getCard().getClass().equals(StarterCard.class)) {
                        myField.addCard((StarterCard) m.getCard());
                        notify(m);
                    } else try {
                        ResourceCard card = (ResourceCard) m.getCard();
                        if (!m.getSide().equals(card.getSide())) card.flip();

                        myField.addCard(card, m.getCoords());
                        notify(m);
                    } catch (IllegalMoveException e) {
                        e.printStackTrace();
                    }

                } else {
                    for (Player p : fields.keySet()) {
                        if (m.getNickname().equals(p.getNickname())) {
                            if (m.getCard().getClass().equals(StarterCard.class)) {
                                fields.get(p).addCard((StarterCard) m.getCard());
                                notify(m);
                            } else try {
                                fields.get(p).addCard((ResourceCard) m.getCard(), m.getCoords());
                                notify(m);
                            } catch (IllegalMoveException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }
            }
            case GamePhaseChangedMessage m -> {
                if (m.getID().equals(ID)) {
                    gamePhase = m.getGamePhase();
                    notify(m);
                }
            }
            case SecretGoalsListAssignedMessage m -> {
                if (m.getPlayer().equals(player))
                    secretGoalChoices = m.getList();
            }
            case SecretGoalAssignedMessage m -> {
                if (m.getPlayer().equals(player)) {
                    secretGoal = m.getGoal();
                    notify(m);
                }
            }
            case GameActionSwitchedMessage m -> {
                if (m.getID().equals(ID)) {
                    action = m.getAction();
                    notify(m);
                }
            }
            case TurnChangedMessage m -> {
                if (m.getID().equals(ID) && m.getNickname().equals(nickname))
                    yourTurn = true;
                else if (m.getID().equals(ID) && !m.getNickname().equals(nickname))
                    yourTurn = false;
                if (m.getID().equals(ID)) {
                    lastRound = m.isLastRound();
                    notify(m);
                }
            }
            case GameWinnersAnnouncedMessage m -> {
                if (m.getID().equals(ID)) {
                    winners = m.getWinners();
                    goalsDone = m.getGoalsDone();
                    notify(m);
                }
            }
            case GameTerminatedMessage m -> {
                if (m.getID().equals(ID)) {
                    lobbies.remove(ID);
                    initialize();
                    notify(m);
                }
            }
            case ScoreIncrementedMessage m -> {
                if (ID != null && ID.equals(m.getID())) {
                    scoreboard.put(m.getPlayer(), getScoreboard().get(m.getPlayer()) + m.getPoints());
                    notify(m);
                }
            }
            case CardDrawnFromSourceMessage m -> {
                if (m.getID().equals(ID)) {
                    switch (m.getType()) {
                        case DeckType.RESOURCE -> {
                            topResourceCard = m.getCard();
                            if (topResourceCard != null && topResourceCard.getSide().equals(CardSide.FRONT))
                                topResourceCard.flip();
                        }

                        case DeckType.GOLDEN -> {
                            topGoldenCard = m.getCard();
                            if (topGoldenCard != null && topGoldenCard.getSide().equals(CardSide.FRONT))
                                topGoldenCard.flip();
                        }

                        case DeckBufferType.RES1, DeckBufferType.RES2 -> {
                            if (topResourceCard != null && topResourceCard.getSide().equals(CardSide.BACK))
                                topResourceCard.flip();
                            deckBuffers.get((DeckBufferType) m.getType()).setCard((ResourceCard) topResourceCard);

                            topResourceCard = m.getCard();
                            if (topResourceCard != null && topResourceCard.getSide().equals(CardSide.FRONT))
                                topResourceCard.flip();
                        }

                        case DeckBufferType.GOLD1, DeckBufferType.GOLD2 -> {
                            if (topGoldenCard != null && topGoldenCard.getSide().equals(CardSide.BACK))
                                topGoldenCard.flip();
                            deckBuffers.get((DeckBufferType) m.getType()).setCard((GoldenCard) topGoldenCard);

                            topGoldenCard = m.getCard();
                            if (topGoldenCard != null && topGoldenCard.getSide().equals(CardSide.FRONT))
                                topGoldenCard.flip();
                        }

                        default -> throw new IllegalStateException("Unexpected value: " + m.getType());
                    }
                    notify(m);
                }
            }
            case HeartBeatMessage ignored -> {
            }
            case FailMessage m -> {
                if (m.getNickname().equals(nickname))
                    notify(m);
            }
            default -> throw new IllegalStateException("Unexpected value: " + message);
        }
    }

    /**
     * Initializes the value of the view when needed (when someone leaves a game or joins a lobby)
     */
    public void initialize() {
        player.initialize();
        yourTurn = false;
        lastRound = false;
        scoreboard = null;
        deckBuffers.clear();
        commonGoal1 = null;
        commonGoal2 = null;
        action = null;
        ID = null;
        pawn = null;
        hand.removeAllCards();
        secretGoalChoices.clear();
        secretGoal = null;
        chat = new Chat();
        myField = new Field();
        topResourceCard = null;
        topGoldenCard = null;
        commonGoal1 = null;
        commonGoal2 = null;
        winners.clear();
    }
}