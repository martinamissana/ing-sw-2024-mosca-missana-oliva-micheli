package it.polimi.ingsw.model.observer.events;

/**
 * TurnChangedEvent class<br>
 * Extends abstract class Event<br>
 * Used to notify the virtual view when the turn is changed
 */
public class TurnChangedEvent  extends Event{
    private final Integer ID;
    private final String nickname; //nickname of the player of the current turn
    private final boolean lastRound;

    /**
     * class constructor
     * @param ID game ID
     * @param nickname name of the player of the current turn
     * @param lastRound true if it's last round
     */
    public TurnChangedEvent(Integer ID, String nickname, boolean lastRound) {
        this.ID = ID;
        this.nickname = nickname;
        this.lastRound = lastRound;
    }

    /**
     * @return ID
     */
    public Integer getID() { return ID; }

    /**
     * @return nickname
     */
    public String getNickname() { return nickname; }

    /**
     * @return boolean true if it's last round
     */
    public boolean isLastRound() { return lastRound; }
}
