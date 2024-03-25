package model.player;

import model.card.Card;
import model.card.ResourceCard;
import model.card.StarterCard;
import model.chat.Chat;
import model.deck.Deck;
import model.deck.DeckBuffer;
import model.game.Game;
import model.goal.Goal;

public class Player {
    private Game game;
    private boolean firstPlayer;
    private String nickname;
    private Pawn playerPawn;
    private Hand playerHand;
    private Field playerField;
    private Goal playerGoal;
    private Chat playerChat;
    public Game getGame() { return game; }
    public boolean getFirstPlayer() { return firstPlayer; }
    public String getNickname() { return nickname; }
    public Pawn getPlayerColor() { return playerPawn; }
    public Hand getPlayerHand() { return playerHand; }
    public Field getPlayerField() { return playerField; }
    public Chat getPlayerChat() { return playerChat; }

    public boolean playCard() {
        StarterCard card = (StarterCard) playerHand.getCard(0);
        playerField.addCard(card);
        playerHand.removeCard(card);
        return true;
    }
    public boolean playCard(int handPos, Coords coords) {
        ResourceCard card = (ResourceCard) playerHand.getCard(handPos);
        boolean cardWasAdded = playerField.addCard((ResourceCard) card, coords);
        if (!cardWasAdded)
            return false;
        return playerHand.removeCard(card);
    }
    public boolean drawCard(Deck deck) {
        if (deck == null)
            return false;
        Card newCard = deck.draw();
        return playerHand.addCard(newCard);
    }
    public boolean drawCard(DeckBuffer deckBuffer) {
        if (deckBuffer == null)
            return false;
        Card newCard = deckBuffer.draw();
        return playerHand.addCard(newCard);
    }
}