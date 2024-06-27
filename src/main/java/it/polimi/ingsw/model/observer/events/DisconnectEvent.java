package it.polimi.ingsw.model.observer.events;

import it.polimi.ingsw.controller.Controller;

/**
 *  DisconnectEvent class<br>
 *  Extends abstract class Event<br>
 *  Used to notify the RMIVirtualView if the client crashes
 */
public class DisconnectEvent extends Event {
    Controller c;
    String nickname;
    Integer ID;

    /**
     * Class constructor
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
     * @return controller
     */
    public Controller getC() { return c; }

    /**
     * @return nickname
     */
    public String getNickname() { return nickname; }

    /**
     * @return ID of the game/lobby
     */
    public Integer getID() { return ID; }
}
