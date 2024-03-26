package it.polimi.ingsw.model.player;

import it.polimi.ingsw.model.card.Card;
import it.polimi.ingsw.model.card.ResourceCard;
import it.polimi.ingsw.model.card.StarterCard;
import it.polimi.ingsw.model.chat.Chat;
import it.polimi.ingsw.model.deck.Deck;
import it.polimi.ingsw.model.deck.DeckBuffer;
import it.polimi.ingsw.model.goal.Goal;

public class Player {
    private boolean isFirstPlayer;
    private String nickname;
    private Pawn pawn;
    private Hand hand;
    private Field field;
    private Goal privateGoal;
    private Chat chat;
    public boolean getFirstPlayer() { return isFirstPlayer; }
    public String getNickname() { return nickname; }
    public Pawn getPlayerColor() { return pawn; }
    public Hand getPlayerHand() { return hand; }
    public Field getPlayerField() { return field; }
    public Chat getPlayerChat() { return chat; }

    public boolean playCard() {
        StarterCard card = (StarterCard) hand.getCard(0);
        field.addCard(card);
        hand.removeCard(card);
        return true;
    }
    public boolean playCard(int handPos, Coords coords) {
        ResourceCard card = (ResourceCard) hand.getCard(handPos);
        boolean cardWasAdded = field.addCard((ResourceCard) card, coords);
        if (!cardWasAdded)
            return false;
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
    }
}