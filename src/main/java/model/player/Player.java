package model.player;

import model.card.Card;
import model.card.ResourceCard;
import model.card.StarterCard;
import model.chat.Chat;
import model.deck.Deck;
import model.deck.DeckBuffer;
import model.game.Game;

public class Player {
    private Game game;
    private boolean firstPlayer;
    private String nickname;
    private Pawn playerPawn;
    private Hand playerHand;
    private Field playerField;
    private Chat playerChat;
    public Game getGame() { return game; }
    public boolean getFirstPlayer() { return firstPlayer; }
    public String getNickname() { return nickname; }
    public Pawn getPlayerColor() { return playerPawn; }
    public Hand getPlayerHand() { return playerHand; }
    public Field getPlayerField() { return playerField; }
    public Chat getPlayerChat() { return playerChat; }

    public boolean playCard(int handPos) {
        StarterCard card = (StarterCard) playerHand.getCard(handPos);
        playerField.addCard(card);
        playerHand.removeCard(card);
        return true;
    }
    public boolean playCard(int handPos, int row, int col) {
        ResourceCard card = (ResourceCard) playerHand.getCard(handPos);
        boolean cardWasAdded = playerField.addCard((ResourceCard) card, row, col);
        if (!cardWasAdded)
            return false;
        return playerHand.removeCard(card);
    }
    public boolean drawCard(Deck deck) {
        Card newCard = deck.draw();
        return playerHand.addCard(newCard);
    }
    public boolean drawCard(DeckBuffer deckBuffer) {
        Card newCard = deckBuffer.draw();
        return playerHand.addCard(newCard);
    }
}