package it.polimi.ingsw.model.observer.events;

/**
 * LoginEvent class<br>
 * Extends abstract class Event<br>
 * Used to notify the virtual view when a player logs in
 */
public class LoginEvent extends Event {
    String nickname;

    /**
     * Class constructor
     * @param nickname name of the user
     */
    public LoginEvent(String nickname) { this.nickname = nickname; }

    /**
     * @return nickname of the user
     */
    public String getNickname() { return nickname; }
}
