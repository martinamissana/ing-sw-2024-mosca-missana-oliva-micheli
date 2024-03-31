package it.polimi.ingsw.model.chat;

import it.polimi.ingsw.model.player.Player;

/**
 * Class Message
 * contains all the attributes associated to a message and the method to send it
 */
public class Message {
    private final String text;
    private final Player sender;
    private final Player receiver;
    private final boolean isGlobal;

    /**
     * CLass constructor
     * @param text
     * @param sender
     * @param receiver
     * @param global
     */
    public Message(String text, Player sender, Player receiver, boolean global) {
        this.sender = sender;
        this.text = text;
        this.receiver = receiver; //null if global
        this.isGlobal = global;
    }

    /**
     * gets the text
     * @return message text
     */
    public String getText() {
        return text;
    }

    /**
     * gets the sender of the message
     * @return  message sender
     */
    public Player getSender() {
        return sender;
    }

    /**
     * gets the receiver of the message
     * @return  message receiver
     */
    public Player getReceiver() {
        return receiver;
    }

    /**
     * tells if a message is global
     * @return message isGlobal
     */
    public boolean isGlobal() {
        return isGlobal;
    }


    /**
     * sends the message  either to the specified receiver, or to every player in the global chat,
     * adds the message in the list of sent messages of the sender too
     */
    public void send() {
        if(!isGlobal) {
            sender.getChat().getSentMessages().add(this);
            receiver.getChat().getReceivedMessages().add(this);
        }
        else {
            //che fare se global?
        }
    }
}