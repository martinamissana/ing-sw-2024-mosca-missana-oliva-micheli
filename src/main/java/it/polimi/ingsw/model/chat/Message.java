package it.polimi.ingsw.model.chat;

import it.polimi.ingsw.model.player.Player;

import java.io.Serializable;

/**
 * Class Message<br>
 * contains all attributes associated to a message and the method to send it
 */
public class Message implements Serializable {
    private final String text;
    private final Player sender;
    private final Player receiver;
    private final boolean isGlobal;
    private int order;
    private static int counter;

    /**
     * Class constructor
     * @param text     text contained in message
     * @param sender   sender of the message
     * @param receiver receiver of the message
     * @param global   indicates whether the message is global ({@code true}) or not ({@code false})
     */
    public Message(String text, Player sender, Player receiver, boolean global) {
        this.text = text;
        this.sender = sender;
        this.receiver = receiver; // null if global
        this.isGlobal = global;
        this.order = counter;
    }

    /**
     * @return the text contained in message
     */
    public String getText() { return text; }

    /**
     * @return the sender of the message
     */
    public Player getSender() { return sender; }

    /**
     * @return the receiver of the message
     */
    public Player getReceiver() { return receiver; }

    /**
     * @return {@code true} if the message is global, {@code false} if it isn't
     */
    public boolean isGlobal() { return isGlobal; }

    /**
     * @return the order assigned to the message
     */
    public int getOrder() { return order; }

    /**
     * @return the counter used to order the messages in the order they are sent
     */
    public static int getCounter() { return counter; }

    /**
     * @param order the order assigned to the message
     */
    public void setOrder(int order) { this.order = order; }

    /**
     * @param counter the counter used to order the messages in the order they are sent
     */
    public static void setCounter(int counter) { Message.counter = counter; }
}