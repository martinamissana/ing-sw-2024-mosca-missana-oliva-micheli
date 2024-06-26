package it.polimi.ingsw.network.netMessage.c2s;

import it.polimi.ingsw.network.netMessage.NetMessage;

/**
 * Class ChooseSecretGoalMessage
 * used to choose the secret goal from the list of goals given by the server
 */
public class ChooseSecretGoalMessage extends NetMessage {
    private final Integer ID;
    private final String nickname;
    private final int goalID;

    /**
     * Class constructor
     * @param ID the game ID
     * @param nickname the name of the player
     * @param goalID the ID of the chosen goal
     */
    public ChooseSecretGoalMessage(Integer ID, String nickname, int goalID) {
        this.ID = ID;
        this.nickname = nickname;
        this.goalID = goalID;
    }

    /**
     * @return Integer
     */
    public Integer getID() {
        return ID;
    }

    /**
     * @return String
     */
    public String getNickname() {
        return nickname;
    }

    /**
     * @return int
     */
    public int getGoalID() {
        return goalID;
    }
}
