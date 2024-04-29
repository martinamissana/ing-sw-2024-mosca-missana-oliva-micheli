package it.polimi.ingsw.network.netMessage;

public class LoginMessage extends NetMessage {
    String nickname;

    public LoginMessage(String nickname) {
        this.nickname = nickname;
    }
}
