package it.polimi.ingsw.view.TUI;

/**
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
     * Setter
     * @param num used to save integers without creating global variables in TUI
     */
    public void setNum(int num) {
        this.num = num;
    }

    /**
     * Getter
     * @return stored integer
     */
    public int getNum() {
        return this.num;
    }
}
