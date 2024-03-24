package model.player;

import model.card.Card;
import java.util.ArrayList;

public class Hand {
    private final Player player;
    private final ArrayList<Card> handList;
    public Hand(Player p) {
        this.player = p;
        this.handList = new ArrayList<Card>();
    }
    public Player getPlayer() { return player; }
    public Card getCard(int pos) {
        return handList.get(pos);
    }
    public boolean addCard(Card card) {
        if (this.handList.size() > 2) // does it need to check card == null?
            return false;
        return this.handList.add(card);
    }
    public boolean removeCard(Card card) {
        return this.handList.remove(card);
    }
}