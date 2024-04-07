package it.polimi.ingsw.model.player;

import it.polimi.ingsw.model.card.Card;
import it.polimi.ingsw.model.card.ResourceCard;
import it.polimi.ingsw.model.card.StarterCard;
import it.polimi.ingsw.model.chat.Chat;
import it.polimi.ingsw.model.deck.Deck;
import it.polimi.ingsw.model.deck.DeckBuffer;
import it.polimi.ingsw.model.goal.Goal;

import java.io.Serializable;

public class Player implements Serializable {
    private final String nickname;
    private final Hand hand;
    private final Field field;
    private final Chat chat;
    private boolean goesFirst;
    private Goal privateGoal;
    private Pawn pawn;

    /**
     * Class constructor
     * @param nickname
     */
    public Player(String nickname) {
        this.nickname = nickname;
        this.hand = new Hand();
        this.field = new Field();
        this.chat = new Chat();
        this.goesFirst = false;
        this.privateGoal = null;
        this.pawn = null;
    }

    /**
     * Class constructor
     * @param goesFirst
     * @param nickname
     * @param pawn
     */
    // might not be needed
    public Player(String nickname, boolean goesFirst, Pawn pawn) {
        this.nickname = nickname;
        this.hand = new Hand();
        this.field = new Field();
        this.chat = new Chat();
        this.goesFirst = goesFirst;
        this.privateGoal = null;
        this.pawn = pawn;
    }

    /**
     * sets the player to be the one to go first
     * @param goesFirst
     */
    public void setGoesFirst(boolean goesFirst) { this.goesFirst = goesFirst; }

    /**
     * sets the player's private goal
     * @param goal
     */
    public void setPrivateGoal(Goal goal) { this.privateGoal = goal; }

    /**
     * sets the player's pawn
     * @param pawn
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
     * returns the player's private goal
     * @return Goal
     */
    public Goal getPrivateGoal() {
        return privateGoal;
    }

    /**
     * returns the player's pawn
     * @return Pawn
     */
    public Pawn getPawn() { return pawn; }

    // this should all be in the controller side of things
    /*
    public boolean playCard() {
        StarterCard card = (StarterCard) hand.getCard(0);
        field.addCard(card);
        hand.removeCard(card);
        return true;
    }
    public boolean playCard(int handPos, Coords coords) {
        ResourceCard card = (ResourceCard) hand.getCard(handPos);
        // very temporary
        int points = field.addCard((ResourceCard) card, coords);
        return hand.removeCard(card);
    }
    public boolean drawCard(Deck deck) {
        if (deck == null)
            return false;
        Card newCard = deck.draw();
        return hand.addCard(newCard);
    }
    public boolean drawCard(DeckBuffer deckBuffer) {
        if (deckBuffer == null)
            return false;
        Card newCard = deckBuffer.draw();
        return hand.addCard(newCard);
    }*/
}