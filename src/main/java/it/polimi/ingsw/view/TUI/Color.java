package it.polimi.ingsw.view.TUI;

/**
 * Enumeration of all colors used in TUI
 */
public enum Color {
    reset ("\u001B[0m"),
    console ("\u001B[38;2;255;165;0m"),
    warning ("\u001B[31m"),

    // PAWNS:
    green ("\u001B[32m"),
    blue ("\u001B[34m"),
    yellow ("\u001B[93m"),
    red ("\u001B[31m"),
    black ("\u001B[40m"),

    // CARDS:
    animal ("\u001B[30;46m"),
    fungi ("\u001B[30;41m"),
    insect ("\u001B[30;45m"),
    plant ("\u001B[30;42m"),
    gold ("\u001B[30;43m"),
    starter ("\u001B[30;48;2;245;200;157m"),
    corner ("\u001B[30;48;2;200;150;100m");

    public final String label;

    /**
     * Class constructor
     * @param label convert color into his String label
     */
    Color(String label) {
        this.label = label;
    }

    @Override
    public String toString() {
        return label;
    }
}
