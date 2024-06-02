package it.polimi.ingsw.model.observer.events;

import it.polimi.ingsw.controller.Controller;

public class DisconnectEvent extends Event {
    Controller c;
    String nickname;
    Integer ID;

    public DisconnectEvent(Controller c, String nickname,Integer ID) {
        this.c=c;
        this.nickname = nickname;
        this.ID = ID;
    }

    public Controller getC() {
        return c;
    }

    public String getNickname() {
        return nickname;
    }

    public Integer getID() {
        return ID;
    }
}
