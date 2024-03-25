package model.card;

import model.commonItem.Kingdom;

import java.util.HashMap;
import java.util.Map;

public class StarterCard extends Card{
    private HashMap<Kingdom, Integer> permanentRes;

    public StarterCard(){
        //robe che dipendono dal json
    }
    public HashMap<Kingdom, Integer> getPermanentRes() {
        return this.permanentRes;
    }
}
