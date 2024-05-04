package it.polimi.ingsw.view;

import it.polimi.ingsw.controller.exceptions.GameAlreadyStartedException;
import it.polimi.ingsw.controller.exceptions.IllegalActionException;
import it.polimi.ingsw.controller.exceptions.NotYourTurnException;
import it.polimi.ingsw.model.card.Card;
import it.polimi.ingsw.model.card.CardSide;
import it.polimi.ingsw.model.chat.Chat;
import it.polimi.ingsw.model.chat.Message;
import it.polimi.ingsw.model.deck.DeckBuffer;
import it.polimi.ingsw.model.deck.DeckBufferType;
import it.polimi.ingsw.model.deck.DeckTypeBox;
import it.polimi.ingsw.model.exceptions.*;
import it.polimi.ingsw.model.game.Action;
import it.polimi.ingsw.model.game.GamePhase;
import it.polimi.ingsw.model.game.Lobby;
import it.polimi.ingsw.model.goal.Goal;
import it.polimi.ingsw.model.player.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public abstract class View {
    private String nickname;
    protected final HashMap<Integer, Lobby> lobbies = new HashMap<>();
    private Player player;
    private Integer ID; // both game and lobby ID
    private Hand hand;
    private Field myField;
    private HashMap<Player, Field> fields; // all other players' fields
    private boolean yourTurn;
    private Action action;
    private Chat chat;
    private Goal privateGoal;
    private Pawn pawn;
    private HashMap<Player, Pawn> pawns; // all other players' pawns
    private boolean firstPlayer;
    private boolean lastRound;
    private ArrayList<Goal> personalGoalChoices;
    private HashMap<Player, Integer> scoreboard;
    private HashMap<DeckBufferType, DeckBuffer> deckBuffers = new HashMap<>();
    private Card topResourceCard;
    private Card topGoldenCard;
    private Goal commonGoal1;
    private Goal commonGoal2;
    private GamePhase gamePhase;
    private final ArrayList<String> errorMessages=new ArrayList<>();

    public View() {
    }

    public String getNickname() {
        return nickname;
    }

    public HashMap<DeckBufferType, DeckBuffer> getDeckBuffers() {
        return deckBuffers;
    }

    public Card getTopResourceCard() {
        return topResourceCard;
    }

    public HashMap<Integer, Lobby> getLobbies() {
        return lobbies;
    }

    public Player getPlayer() {
        return player;
    }

    public Integer getID() {
        return ID;
    }

    public Hand getHand() {
        return hand;
    }

    public Field getMyField() {
        return myField;
    }

    public HashMap<Player, Field> getFields() {
        return fields;
    }

    public boolean isYourTurn() {
        return yourTurn;
    }

    public Action getAction() {
        return action;
    }

    public Chat getChat() {
        return chat;
    }

    public Goal getPrivateGoal() {
        return privateGoal;
    }

    public ArrayList<String> getErrorMessages() {return errorMessages;}

    public Pawn getPawn() {
        return pawn;
    }

    public HashMap<Player, Pawn> getPawns() {
        return pawns;
    }

    public boolean isFirstPlayer() {
        return firstPlayer;
    }

    public boolean isLastRound() {
        return lastRound;
    }

    public ArrayList<Goal> getPersonalGoalChoices() {
        return personalGoalChoices;
    }

    public HashMap<Player, Integer> getScoreboard() {
        return scoreboard;
    }

    public GamePhase getGamePhase() {
        return gamePhase;
    }

    public Card getTopGoldenCard() {
        return topGoldenCard;
    }

    public Goal getCommonGoal1() {
        return commonGoal1;
    }

    public Goal getCommonGoal2() {
        return commonGoal2;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public void setID(Integer ID) {
        this.ID = ID;
    }

    public void setHand(Hand hand) {
        this.hand = hand;
    }

    public void setMyField(Field myField) {
        this.myField = myField;
    }

    public void setFields(HashMap<Player, Field> fields) {
        this.fields = fields;
    }

    public void setYourTurn(boolean yourTurn) {
        this.yourTurn = yourTurn;
    }

    public void setAction(Action action) {
        this.action = action;
    }

    public void setChat(Chat chat) {
        this.chat = chat;
    }

    public void setPrivateGoal(Goal privateGoal) {
        this.privateGoal = privateGoal;
    }

    public void setPawn(Pawn pawn) {
        this.pawn = pawn;
    }

    public void setPawns(HashMap<Player, Pawn> pawns) {
        this.pawns = pawns;
    }

    public void setFirstPlayer(boolean firstPlayer) {
        this.firstPlayer = firstPlayer;
    }

    public void setLastRound(boolean lastRound) {
        this.lastRound = lastRound;
    }

    public void setPersonalGoalChoices(ArrayList<Goal> personalGoalChoices) {
        this.personalGoalChoices = personalGoalChoices;
    }

    public void setScoreboard(HashMap<Player, Integer> scoreboard) {
        this.scoreboard = scoreboard;
    }

    public void setGamePhase(GamePhase gamePhase) {
        this.gamePhase = gamePhase;
    }

    public void setDeckBuffers(HashMap<DeckBufferType, DeckBuffer> deckBuffers) {
        this.deckBuffers = deckBuffers;
    }

    public void setTopResourceCard(Card topResourceCard) {
        this.topResourceCard = topResourceCard;
    }

    public void setTopGoldenCard(Card topGoldenCard) {
        this.topGoldenCard = topGoldenCard;
    }

    public void setCommonGoal1(Goal commonGoal1) {
        this.commonGoal1 = commonGoal1;
    }

    public void setCommonGoal2(Goal commonGoal2) {
        this.commonGoal2 = commonGoal2;
    }

    public void setNickname(String nickname) { this.nickname = nickname; }

    public void createLobby(int numOfPlayers) throws LobbyDoesNotExistsException, IOException {

    }

    public void joinLobby(Player player, int lobbyID) throws FullLobbyException, NicknameAlreadyTakenException, LobbyDoesNotExistsException, IOException {

    }

    public void leaveLobby(Player player, int lobbyID) throws GameAlreadyStartedException, LobbyDoesNotExistsException {

    }

    public void sendMessage(Integer gameID, Message message) throws LobbyDoesNotExistsException, GameDoesNotExistException {

    }

    public void choosePawn(Integer lobbyID, Player player, Pawn color) throws LobbyDoesNotExistsException, PawnAlreadyTakenException {

    }

    public void choosePersonalGoal(Player player, Goal goal) {

    }

    public void leaveGame(Integer gameID, Player player) throws LobbyDoesNotExistsException, GameDoesNotExistException {

    }

    public void chooseCardSide(Integer gameID, Player player, CardSide side) {

    }

    public void playCard(Integer gameID, Player player, int handPos, Coords coords) throws IllegalActionException, NotYourTurnException, IllegalMoveException, GameDoesNotExistException {

    }

    public void drawCard(Integer gameID, Player player, DeckTypeBox deckTypeBox) throws IllegalActionException, EmptyBufferException, NotYourTurnException, EmptyDeckException, GameDoesNotExistException, HandIsFullException {

    }

    public void flipCard(Integer gameID, Player player, int handPos) throws GameDoesNotExistException {

    }
}