package it.polimi.ingsw.model.observer;

import it.polimi.ingsw.model.game.GameHandler;

public class Listener implements Observer {
    private GameHandler gh;
    @Override
    public void update(GameHandler gh) {
        this.setGameHandler(gh);
    }

    public GameHandler getGameHandler() {
        return gh;
    }

    public void setGameHandler(GameHandler gh) {
        this.gh = gh;
    }
}
