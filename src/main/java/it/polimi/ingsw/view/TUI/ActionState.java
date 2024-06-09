package it.polimi.ingsw.view.TUI;

public enum ActionState {
    PLAY_SELECT_CARD,
    PLAY_SELECT_COORDS,
    DRAW;

    private int num;
    ActionState() {
        this.num = -1;
    }

    public void setNum(int num) {
        this.num = num;
    }

    public int getNum() {
        return this.num;
    }
}