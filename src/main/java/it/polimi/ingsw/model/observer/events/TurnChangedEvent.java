package it.polimi.ingsw.model.observer.events;

public class TurnChangedEvent  extends Event{
    private final Integer ID;
    private final String nickname; //nickname of the player of the current turn
    private final boolean lastRound;

    public TurnChangedEvent(Integer ID, String nickname, boolean lastRound) {
        this.ID = ID;
        this.nickname = nickname;
        this.lastRound = lastRound;
    }

    public Integer getID() {
        return ID;
    }

    public String getNickname() {
        return nickname;
    }

    public boolean isLastRound() {
        return lastRound;
    }
}
