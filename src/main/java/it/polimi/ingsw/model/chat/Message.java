package it.polimi.ingsw.model.chat;

import it.polimi.ingsw.model.player.Player;

import java.io.Serializable;

/**
 * Class Message
 * contains all the attributes associated to a message and the method to send it
 */
public class Message implements Serializable {
    private final String text;
    private final Player sender;
    private final Player receiver;
    private final boolean isGlobal;
    private int order;
    private static int counter;

    /**
     * CLass constructor
     *
     * @param text     the text of the message
     * @param sender   the sender of the message
     * @param receiver the receiver of the message
     * @param global    indicates if the message is global
     */
    public Message(String text, Player sender, Player receiver, boolean global) {
        this.sender = sender;
        this.text = text;
        this.receiver = receiver; //null if global message
        this.isGlobal = global;
        this.order = counter;
    }

    /**
     * getter
     *
     * @return String the message text
     */
    public String getText() {
        return text;
    }

    /**
     * getter
     *
     * @return Player the message sender
     */
    public Player getSender() {
        return sender;
    }

    /**
     * getter
     *
     * @return Player the message receiver
     */
    public Player getReceiver() {
        return receiver;
    }

    /**
     * tells if a message is global
     *
     * @return message isGlobal
     */
    public boolean isGlobal() {
        return isGlobal;
    }

    /**
     * getter
     *
     * @return int the order assigned to the message
     */
    public int getOrder() {
        return order;
    }

    /**
     * getter
     *
     * @return int the counter used to order the messages in the order they are sent
     */
    public static int getCounter() {
        return counter;
    }

    /**
     * setter
     *
     * @param counter the counter used to order the messages in the order they are sent
     */
    public static void setCounter(int counter) {
        Message.counter = counter;
    }

    /**
     * setter
     *
     * @param order the order assigned to the message
     */
    public void setOrder(int order) {
        this.order = order;
    }
}