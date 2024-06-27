package it.polimi.ingsw.model.player;

import it.polimi.ingsw.model.chat.Chat;
import it.polimi.ingsw.model.goal.Goal;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Objects;

/**
 * Player Class<br>
 * Organizes all the data structures of a player in a lobby and match
 */
public class Player implements Serializable {

    private final String nickname;
    private final Hand hand;
    private Field field;
    private Chat chat;
    private Goal secretGoal;
    private Pawn pawn;
    private final ArrayList<Goal> choosableGoals;
    private int goalsDone;

    /**
     * Class constructor
     * @param nickname player's nickname
     */
    public Player(String nickname) {
        this.nickname = nickname;
        this.hand = new Hand();
        this.field = new Field();
        this.chat = new Chat();
        this.secretGoal = null;
        this.pawn = null;
        this.choosableGoals = new ArrayList<>();
        this.goalsDone = 0;
    }

    /**
     * Returns the player's nickname
     * @return String
     */
    public String getNickname() { return nickname; }

    /**
     * Returns the player's hand
     * @return Hand
     */
    public Hand getHand() { return hand; }

    /**
     * Returns the player's field
     * @return Field
     */
    public Field getField() { return field; }

    /**
     * Returns the player's chat
     * @return Chat
     */
    public Chat getChat() { return chat; }

    /**
     * Returns the player's secret goal
     * @return Goal
     */
    public Goal getSecretGoal() { return secretGoal; }

    /**
     * Sets the player's secret goal
     * @param goal goal to set as the player's private goal
     */
    public void setSecretGoal(Goal goal) { this.secretGoal = goal; }

    /**
     * @return the number of goals completed by the player
     */
    public int getGoalsDone() { return goalsDone; }

    /**
     * Increments the counter of completed goals by one
     */
    public void addGoalDone() { this.goalsDone++; }

    /**
     * Returns the player's pawn
     * @return Pawn
     */
    public Pawn getPawn() { return pawn; }

    /**
     * Sets the player's pawn
     * @param pawn pawn to set
     */
    public void setPawn(Pawn pawn) { this.pawn = pawn; }

    /**
     * Initializes all game-related attributes. Used when a player is no longer in a lobby or in a game.
     */
    public void initialize() {
        this.hand.removeAllCards();
        this.field = new Field();
        this.chat = new Chat();
        this.secretGoal = null;
        this.pawn = null;
        this.choosableGoals.clear();
        this.goalsDone = 0;
    }

    /**
     * @return the list of goals the player can choose from
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