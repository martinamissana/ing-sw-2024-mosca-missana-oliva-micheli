package it.polimi.ingsw.model.player;

import it.polimi.ingsw.model.chat.Chat;
import it.polimi.ingsw.model.goal.Goal;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Objects;

public class Player implements Serializable {
    private final String nickname;
    private final Hand hand;
    private Field field;
    private Chat chat;
    private boolean goesFirst;
    private Goal secretGoal;
    private Pawn pawn;
    private ArrayList<Goal> choosableGoals;
    private int goalsDone;

    /**
     * Class constructor
     *
     * @param nickname player's nickname
     */
    public Player(String nickname) {
        this.nickname = nickname;
        this.hand = new Hand();
        this.field = new Field();
        this.chat = new Chat();
        this.goesFirst = false;
        this.secretGoal = null;
        this.pawn = null;
        this.choosableGoals = new ArrayList<>();
        this.goalsDone = 0;
    }

    /**
     * Class constructor
     *
     * @param nickname  player's nickname
     * @param goesFirst signals that the player is the first of each turn in the game
     * @param pawn      player's pawn
     */
    // might not be needed
    public Player(String nickname, boolean goesFirst, Pawn pawn) {
        this.nickname = nickname;
        this.hand = new Hand();
        this.field = new Field();
        this.chat = new Chat();
        this.goesFirst = goesFirst;
        this.secretGoal = null;
        this.pawn = pawn;
        this.goalsDone = 0;
    }

    /**
     * sets the player to be the one to go first
     *
     * @param goesFirst signals that the player is the first of each turn in the game
     */
    public void setGoesFirst(boolean goesFirst) { this.goesFirst = goesFirst; }

    /**
     * sets the player's secret goal
     *
     * @param goal goal to set as the player's private goal
     */
    public void setSecretGoal(Goal goal) { this.secretGoal = goal; }

    /**
     * sets the player's pawn
     *
     * @param pawn pawn to set
     */
    public void setPawn(Pawn pawn) { this.pawn = pawn; }

    /**
     * returns the player's nickname
     *
     * @return String
     */
    public String getNickname() { return nickname; }

    /**
     * returns the player's hand
     *
     * @return Hand
     */
    public Hand getHand() { return hand; }

    /**
     * returns the player's field
     *
     * @return Field
     */
    public Field getField() { return field; }

    /**
     * returns the player's chat
     *
     * @return Chat
     */
    public Chat getChat() { return chat; }

    /**
     * returns whether the player goes first or not
     *
     * @return boolean
     */
    public boolean getGoesFirst() { return goesFirst; }

    /**
     * returns the player's secret goal
     *
     * @return Goal
     */
    public Goal getSecretGoal() {
        return secretGoal;
    }

    public int getGoalsDone() {
        return goalsDone;
    }

    public void addGoalDone() {
        this.goalsDone++;
    }

    /**
     * returns the player's pawn
     *
     * @return Pawn
     */
    public Pawn getPawn() { return pawn; }

    /**
     * initializes all the attributes related to a game, used when a players is no longer in a lobby or in a game
     */
    public void initialize() {
        this.hand.removeAllCards();
        this.field = new Field();
        this.chat = new Chat();
        this.goesFirst = false;
        this.secretGoal = null;
        this.pawn = null;
        this.choosableGoals.clear();
        this.goalsDone = 0;
    }

    /**
     * getter
     *
     * @return the list of choosable goals
     */
    public ArrayList<Goal> getChoosableGoals() {
        return choosableGoals;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Player player = (Player) o;
        return Objects.equals(nickname, player.nickname);
    }

    @Override
    public int hashCode() {
        return Objects.hash(nickname);
    }
}