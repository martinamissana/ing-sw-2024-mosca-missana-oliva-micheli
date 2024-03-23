package model.player;

import model.card.Card;
import model.card.GoldenCard;
import model.card.ResourceCard;
import model.card.StarterCard;
import model.commonItem.ItemBox;
import model.commonItem.Resource;

import java.util.Map;

public class Field {
    private Card[][] matrix;
    private Map<ItemBox, Integer> totalResources;
    public Card[][] getMatrix() {
        return matrix;
    }
    public Map<ItemBox, Integer> getTotalResources() {
        return totalResources;
    }
    public boolean addCard(StarterCard card, int row, int col) {
        return false;
    }
    public boolean addCard(ResourceCard card, int row, int col) {
        return false;
    }
    public boolean addCard(GoldenCard card, int row, int col) {
        return false;
    }
    private boolean checkIfPlaceable(int row, int col) {
        return false;
    }
    private boolean checkRequirements(GoldenCard card) {
        return false;
    }
    private void newResTotal(Card card, int row, int col) { return; }
    private int evaluatePoints(ResourceCard card) {
        return 0;
    }
    private int evaluatePoints(GoldenCard card, int row, int col) {
        return 0;
    }
    private boolean checkTurn() {
        return false;
    }
    private void moveCard(Card card, int row, int col) { return; }
}