package it.polimi.ingsw.model.serialization;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import it.polimi.ingsw.model.card.Corner;
import it.polimi.ingsw.model.commonItem.CornerStatus;
import it.polimi.ingsw.model.commonItem.ItemBox;
import it.polimi.ingsw.model.commonItem.Kingdom;
import it.polimi.ingsw.model.commonItem.Resource;

import java.lang.reflect.Type;

public class CornerDeserializer implements JsonDeserializer<Corner> {
    @Override
    public Corner deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
        String value = jsonElement.getAsString();
        ItemBox item;
        if(value.equals("EMPTY"))
            item = jsonDeserializationContext.deserialize(jsonElement, CornerStatus.class);
        else if(value.equals("ANIMAL") ||
                value.equals("FUNGI") ||
                value.equals("INSECT") ||
                value.equals("PLANT"))
            item = jsonDeserializationContext.deserialize(jsonElement, Kingdom.class);
        else if(value.equals("QUILL") ||
                value.equals("INKWELL") ||
                value.equals("MANUSCRIPT"))
            item = jsonDeserializationContext.deserialize(jsonElement, Resource.class);
        else
            throw new JsonParseException("Item not valid: " + value);
        return new Corner(item);
    }
}
