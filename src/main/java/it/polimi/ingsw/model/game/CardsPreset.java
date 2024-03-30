package it.polimi.ingsw.model.game;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import it.polimi.ingsw.model.card.*;
import it.polimi.ingsw.model.serialization.CornerDeserializer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;


/**
 * Class CardPreset
 * defines the methods to obtain all type of cards in Codex Naturalis
 */
public class CardsPreset {

    /**
     * gets all the resource cards in a list
     * @return list<ResourceCard>
     * @throws IOException
     */
    public static List<ResourceCard> getResourceCards() throws IOException {
        List<ResourceCard> result;

        InputStream inputStream = CardsPreset.class.getClassLoader().getResourceAsStream("resource_cards.json");
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
        StringBuilder jsonBuilder = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
            jsonBuilder.append(line);
        }
        String json = jsonBuilder.toString();

        GsonBuilder builder = new GsonBuilder();
        builder.registerTypeAdapter(Corner.class, new CornerDeserializer());
        Gson gson = builder.create();
        ResourceCard[] arrayCard = gson.fromJson(json, ResourceCard[].class);
        result = List.of(arrayCard);

        return result;
    }


    /**
     * gets all the golden cards in a list
     * @return list<GoldenCard>
     * @throws IOException
     */
    public static List<GoldenCard> getGoldenCards() throws IOException {
        List<GoldenCard> result;

        InputStream inputStream = CardsPreset.class.getClassLoader().getResourceAsStream("golden_cards.json");
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
        StringBuilder jsonBuilder = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
            jsonBuilder.append(line);
        }
        String json = jsonBuilder.toString();

        GsonBuilder builder = new GsonBuilder();
        builder.registerTypeAdapter(Corner.class, new CornerDeserializer());
        Gson gson = builder.create();
        GoldenCard[] arrayCard = gson.fromJson(json, GoldenCard[].class);
        result = List.of(arrayCard);

        return result;
    }

    /**
     * gets all the starter cards in a list
     * @return list<StarterCard>
     * @throws IOException
     */
    public static List<StarterCard> getStarterCards() throws IOException {
        List<StarterCard> result;

        InputStream inputStream = CardsPreset.class.getClassLoader().getResourceAsStream("starter_cards.json");
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
        StringBuilder jsonBuilder = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
            jsonBuilder.append(line);
        }
        String json = jsonBuilder.toString();
        GsonBuilder builder = new GsonBuilder();
        builder.registerTypeAdapter(Corner.class, new CornerDeserializer());
        Gson gson = builder.create();
        StarterCard[] arrayCard = gson.fromJson(json, StarterCard[].class);
        result = List.of(arrayCard);

        return result;
    }
}
