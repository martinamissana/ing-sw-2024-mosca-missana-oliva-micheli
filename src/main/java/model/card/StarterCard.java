package model.card;

import model.commonItem.Kingdom;

import java.util.Map;

public class StarterCard extends Card{
    private Map<Kingdom, Integer> permanentRes;

    public StarterCard(){
        //robe che dipendono dal json
    }
    public Map<Kingdom, Integer> getPermanentRes() {
        return this.permanentRes;
    }
}
