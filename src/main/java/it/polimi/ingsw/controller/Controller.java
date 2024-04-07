package it.polimi.ingsw.controller;

import it.polimi.ingsw.model.game.GameHandler;

public class Controller {
    GameHandler gh;

    public Controller(GameHandler gh) {
        this.gh = gh;
    }

    public GameHandler getGh() {
        return gh;
    }
}
