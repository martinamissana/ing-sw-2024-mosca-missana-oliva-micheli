package it.polimi.ingsw.view.TUI;

public enum ChatState {
    SELECT_CHAT,
    SEND_MESSAGE;

    private int num;
    ChatState() {
        this.num = -1;
    }

    public void setNum(int num) {
        this.num = num;
    }

    public int getNum() {
        return this.num;
    }
}
