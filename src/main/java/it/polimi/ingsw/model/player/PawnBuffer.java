package it.polimi.ingsw.model.player;

import it.polimi.ingsw.model.exceptions.PawnAlreadyTakenException;

import java.util.ArrayList;

public class PawnBuffer {
    private ArrayList<Pawn> pawnList=new ArrayList<>();

    public PawnBuffer() {
        pawnList.add(Pawn.RED);
        pawnList.add(Pawn.BLUE);
        pawnList.add(Pawn.GREEN);
        pawnList.add(Pawn.YELLOW);
    }

    public ArrayList<Pawn> getPawnList() {
        return pawnList;
    }

    public Pawn choosePawn(Pawn color) throws PawnAlreadyTakenException {
        if(pawnList.contains(color))pawnList.remove(color);
        else throw new PawnAlreadyTakenException();
        return color;
    }
}
