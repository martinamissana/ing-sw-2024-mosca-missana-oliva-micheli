package model.card;

import model.commonItem.Kingdom;

public class ResourceCard extends Card  {
    private int points;
    private Kingdom kingdom;

    public ResourceCard(){
        //robe che dipendono dal json
    }

    public int getPoints() {
        return this.points;
    }

    public Kingdom getKingdom() {
        return this.kingdom;
    }
}
