package it.polimi.ingsw.network.message;

public class LoginMessage extends NetMessage {
    String nickname;

    public LoginMessage(String nickname) {
        this.nickname = nickname;
    }
}
