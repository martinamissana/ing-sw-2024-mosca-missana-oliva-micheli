package it.polimi.ingsw.view;

import java.io.IOException;

public abstract class View {
    private String nickname;

    public View(){

    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public void login(String nickname) throws IOException {};


}
