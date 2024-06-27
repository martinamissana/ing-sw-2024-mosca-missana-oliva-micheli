package it.polimi.ingsw.view.TUI;

/**
 * Class ChatState
 * States of chat actions used in TUI
 */
public enum ChatState {
    SELECT_CHAT,
    SEND_MESSAGE;

    private int num;

    /**
     * Class constructor
     */
    ChatState() {
        this.num = -1;
    }

    /**
     * @param num used to save integers without creating global variables in TUI
     */
    public void setNum(int num) {
        this.num = num;
    }

    /**
     * @return stored integer
     */
    public int getNum() {
        return this.num;
    }
}
