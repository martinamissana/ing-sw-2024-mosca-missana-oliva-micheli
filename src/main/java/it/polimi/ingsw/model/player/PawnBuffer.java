package it.polimi.ingsw.model.player;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;

/**
 * A list of all available pawns in a lobby
 */
public class PawnBuffer implements Serializable {
    private final ArrayList<Pawn> pawnList = new ArrayList<>();

    /**
     * Class constructor
     */
    public PawnBuffer() { Collections.addAll(pawnList, Pawn.values()); }

    /**
     * @return the list of available pawns in the lobby
     */
    public ArrayList<Pawn> getPawnList() { return pawnList; }
}
