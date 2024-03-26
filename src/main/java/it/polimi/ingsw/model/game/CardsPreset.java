package it.polimi.ingsw.model.game;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import it.polimi.ingsw.model.card.Card;
import it.polimi.ingsw.model.card.Corner;
import it.polimi.ingsw.model.card.GoldenCard;
import it.polimi.ingsw.model.card.ResourceCard;
import it.polimi.ingsw.model.serialization.CornerDeserializer;

import java.util.List;

public class CardsPreset {

    public static List<ResourceCard> getResourceCards(){
        List<ResourceCard> result;
        String json = """
                [{
                    "points" : 2,
                    "kingdom" : "FUNGI",
                    "cardID" : 0,
                    "side" : "FRONT",
                    "frontCorner" : {
                        "NORTH" : "FUNGI",
                        "SOUTH" : "FUNGI"
                    },
                    "backCorner" : {
                        "SOUTH" : "FUNGI"
                    }
                }]""";

        GsonBuilder builder = new GsonBuilder();
        builder.registerTypeAdapter(Corner.class, new CornerDeserializer());

        Gson gson = builder.create();

        ResourceCard[] arrayCard = gson.fromJson(json, ResourceCard[].class);

        result = List.of(arrayCard);

        return result;
    }


    public static List<GoldenCard> getGoldenCards(){
        List<GoldenCard> result;
        String json = """
                [{
                    "points" : 2,
                    "kingdom" : "FUNGI",
                    "cardID" : 0,
                    "side" : "FRONT",
                    "frontCorner" : {
                        "NORTH" : "FUNGI",
                        "SOUTH" : "FUNGI"
                    },
                    "backCorner" : {
                        "SOUTH" : "FUNGI"
                    },
                    "type" : "RESOURCE",
                    "requirements" : {
                        "FUNGI" : 2,
                        "ANIMAL" : 1
                    },
                    "pointResource" : "INKWELL"
                }]""";

        GsonBuilder builder = new GsonBuilder();
        builder.registerTypeAdapter(Corner.class, new CornerDeserializer());

        Gson gson = builder.create();

        GoldenCard[] arrayCard = gson.fromJson(json, GoldenCard[].class);

        result = List.of(arrayCard);

        return result;
    }
}
