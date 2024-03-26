package it.polimi.ingsw.model.card;

import it.polimi.ingsw.model.commonItem.ItemBox;

public class Corner {
    private ItemBox item;

    public Corner(ItemBox item){
        this.item=item;
    }
    public ItemBox getItem() {
        return this.item;
    }

}
