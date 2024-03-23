package model.player;

import model.card.Card;

import java.util.Optional;

public class Hand {
    private Player player;
    private Optional<Card>[] handArray;
    public Player getPlayer() { return player; }
    public Optional<Card>[] getHandArray() { return handArray; }
    public Optional<Card> getCard(int pos) { return handArray[pos]; }
    public boolean playCard(Card card) { return false; }
    public void removeCard(Card card) { return; }
    public void addToHand(Card card) { return; }
}
