package it.polimi.ingsw.model.player;

import it.polimi.ingsw.model.chat.Chat;
import it.polimi.ingsw.model.goal.Goal;

import java.io.Serializable;
import java.util.ArrayList;

public class Player implements Serializable {
    private final String nickname;
    private final Hand hand;
    private final Field field;
    private final Chat chat;
    private boolean goesFirst;
    private Goal secretGoal;
    private Pawn pawn;
    private ArrayList<Goal> choosableGoals;

    /**
     * Class constructor
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
    }

    /**
     * Class constructor
     * @param nickname player's nickname
     * @param goesFirst signals that the player is the first of each turn in the game
     * @param pawn player's pawn
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
    }

    /**
     * sets the player to be the one to go first
     * @param goesFirst signals that the player is the first of each turn in the game
     */
    public void setGoesFirst(boolean goesFirst) { this.goesFirst = goesFirst; }

    /**
     * sets the player's secret goal
     * @param goal goal to set as the player's private goal
     */
    public void setSecretGoal(Goal goal) { this.secretGoal = goal; }

    /**
     * sets the player's pawn
     * @param pawn pawn to set
     */
    public void setPawn(Pawn pawn) { this.pawn = pawn; }

    /**
     * returns the player's nickname
     * @return String
     */
    public String getNickname() { return nickname; }

    /**
     * returns the player's hand
     * @return Hand
     */
    public Hand getHand() { return hand; }

    /**
     * returns the player's field
     * @return Field
     */
    public Field getField() { return field; }

    /**
     * returns the player's chat
     * @return Chat
     */
    public Chat getChat() { return chat; }

    /**
     * returns whether the player goes first or not
     * @return boolean
     */
    public boolean getGoesFirst() { return goesFirst; }

    /**
     * returns the player's secret goal
     * @return Goal
     */
    public Goal getSecretGoal() {
        return secretGoal;
    }

    /**
     * returns the player's pawn
     * @return Pawn
     */
    public Pawn getPawn() { return pawn; }

    /**
     * getter
     * @return the list of choosable goals
     */
    public ArrayList<Goal> getChoosableGoals() {
        return choosableGoals;
    }

    public boolean equals(Player p) {
        if(this.nickname.equals(p.getNickname()))return true;
        else return false;
    }
}