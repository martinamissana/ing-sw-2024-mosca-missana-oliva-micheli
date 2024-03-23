package model.player;

import model.chat.Chat;

import java.awt.*;

public class Player {
    private boolean firstPlayer;
    private String nickname;
    private Color playerColor;
    private Hand playerHand;
    private Field playerField;
    private Chat playerChat;
    public boolean getFirstPlayer() { return firstPlayer; }
    public String getNickname() { return nickname; }
    public Color getPlayerColor() { return playerColor; }
    public Hand getPlayerHand() { return playerHand; }
    public Chat getPlayerChat() { return playerChat; }
    public Field getPlayerField() { return playerField; }
}