package it.polimi.ingsw.model.game;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import it.polimi.ingsw.model.card.Card;
import it.polimi.ingsw.model.card.ResourceCard;

import java.util.List;

public class CardsPreset {

    public static List<ResourceCard> getResourceCards(){
        List<ResourceCard> result;
        String json = "{\n" +
                "    \"points\" : 2,\n" +
                "    \"kingdom\" : \"FUNGI\",\n" +
                "    \"cardID\" : 0,\n" +
                "    \"side\" : \"FRONT\",\n" +
                "    \"frontCorner\" : {\n" +
                "        \"NORTH\" : \"FUNGI\"\n" +
                "    },\n" +
                "    \"frontCorner\" : {\n" +
                "        \"SOUTH\" : \"FUNGI\"\n" +
                "    }\n" +
                "}";

        GsonBuilder builder = new GsonBuilder();


        Gson gson = builder.create();

        ResourceCard card = gson.fromJson(json, ResourceCard.class);

        result = List.of(card);

        return result;
    }

}
