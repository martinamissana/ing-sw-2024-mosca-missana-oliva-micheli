package it.polimi.ingsw.model.observer.events;

/**
 * LoginEvent class
 * Extends abstract class Event
 * Used to notify the virtual view when a player logs in
 */
public class LoginEvent extends Event {
    String nickname;

    /**
     * class constructor
     * @param nickname name of the user
     */
    public LoginEvent(String nickname) {
        this.nickname = nickname;
    }

    /**
     * getter
     * @return nickname of the user
     */
    public String getNickname() {
        return nickname;
    }
}
