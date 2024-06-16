package it.polimi.ingsw.model.game;

/**
 * this enumeration defines the state of a game, and is used to determine whether a method can be called at a certain time
 */
public enum GamePhase {
    PLACING_STARTER_CARD,
    CHOOSING_SECRET_GOAL,
    PLAYING_GAME
}
