package it.polimi.ingsw.model.observer.events;

public class LoginEvent extends Event {
    String nickname;

    public String getNickname() {
        return nickname;
    }
}
