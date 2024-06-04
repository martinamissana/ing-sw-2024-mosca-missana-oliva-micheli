package it.polimi.ingsw.model.observer.events;

import it.polimi.ingsw.controller.Controller;

/**
 *  DisconnectEvent class
 *  Extends abstract class Event
 *  Used to notify the RMIVirtualView if the client crashes
 */
public class DisconnectEvent extends Event {
    Controller c;
    String nickname;
    Integer ID;

    /**
     * class constructor
     * @param c controller
     * @param nickname name of the player
     * @param ID ID of the game/lobby
     */
    public DisconnectEvent(Controller c, String nickname,Integer ID) {
        this.c=c;
        this.nickname = nickname;
        this.ID = ID;
    }

    /**
     * getter
     * @return controller
     */
    public Controller getC() {
        return c;
    }

    /**
     * getter
     * @return nickname
     */
    public String getNickname() {
        return nickname;
    }

    /**
     * getter
     * @return ID of the game/lobby
     */
    public Integer getID() {
        return ID;
    }
}
