package it.polimi.ingsw.model.game;

/**
 * Enumeration that defines the state of a game. It's used to synchronize player actions at the start of a match.
 */
public enum GamePhase {
    PLACING_STARTER_CARD,
    CHOOSING_SECRET_GOAL,
    PLAYING_GAME
}
