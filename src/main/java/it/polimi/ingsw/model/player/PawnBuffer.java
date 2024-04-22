package it.polimi.ingsw.model.player;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;

/**
 * List of all the available pawns
 */
public class PawnBuffer implements Serializable {
    private final ArrayList<Pawn> pawnList = new ArrayList<>();

    /**
     * Class constructor
     */
    public PawnBuffer() {
        Collections.addAll(pawnList, Pawn.values());
    }

    /**
     * getter
     * @return ArrayList<Pawn> - the list of available pawns
     */
    public ArrayList<Pawn> getPawnList() {
        return pawnList;
    }
}
