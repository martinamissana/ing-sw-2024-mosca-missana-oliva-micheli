package it.polimi.ingsw.model.player;

import it.polimi.ingsw.model.exceptions.PawnAlreadyTakenException;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * PawnBuffer Class
 * used to allow the player to choose an available pawn
 */
public class PawnBuffer implements Serializable {
    private final ArrayList<Pawn> pawnList=new ArrayList<>();

    /**
     * Class constructor
     */
    public PawnBuffer() {
        pawnList.add(Pawn.RED);
        pawnList.add(Pawn.BLUE);
        pawnList.add(Pawn.GREEN);
        pawnList.add(Pawn.YELLOW);
    }

    /**
     * getter
     * @return ArrayList<Pawn> - the list of available pawns
     */
    public ArrayList<Pawn> getPawnList() {
        return pawnList;
    }

    /**
     * allows the player to choose a pawn
     * @param color - the color of the desired pawn
     * @return Pawn - the chosen pawn
     * @throws PawnAlreadyTakenException - if the chosen pawn has already been taken
     */
    public Pawn choosePawn(Pawn color) throws PawnAlreadyTakenException {
        if(pawnList.contains(color))pawnList.remove(color);
        else throw new PawnAlreadyTakenException();
        return color;
    }
}
