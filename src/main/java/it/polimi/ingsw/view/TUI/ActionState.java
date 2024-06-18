package it.polimi.ingsw.view.TUI;

/**
 * States of allowed actions in a game turn used in TUI
 */
public enum ActionState {
    PLAY_SELECT_CARD,
    PLAY_SELECT_COORDS,
    DRAW;

    private int num;

    /**
     * Class constructor
     */
    ActionState() {
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