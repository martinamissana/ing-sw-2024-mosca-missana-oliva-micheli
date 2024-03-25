package model.card;

import model.commonItem.Kingdom;
import model.commonItem.Resource;

import java.util.HashMap;
import java.util.Map;

public class GoldenCard extends ResourceCard{
    private HashMap<Kingdom,Integer> requirements;
    private GoldenCardType type;
    private Resource pointResource; //is null if type!=RESOURCE

    public GoldenCard(){
        //robe che dipendono dal json
    }

    public HashMap<Kingdom, Integer> getRequirements() {
        return this.requirements;
    }

    public GoldenCardType getType() {
        return this.type;
    }

    public Resource getPointResource() {
        return this.pointResource;
    }

}
