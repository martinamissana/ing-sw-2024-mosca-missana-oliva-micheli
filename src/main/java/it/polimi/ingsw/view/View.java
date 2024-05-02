package it.polimi.ingsw.view;

public abstract class View {
    private String nickname;

    public View(String nickname){

    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public void login(String nickname)  {};



}
