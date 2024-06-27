package it.polimi.ingsw.model.game;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import it.polimi.ingsw.model.card.Corner;
import it.polimi.ingsw.model.card.GoldenCard;
import it.polimi.ingsw.model.card.ResourceCard;
import it.polimi.ingsw.model.card.StarterCard;
import it.polimi.ingsw.model.serialization.CornerDeserializer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * Class CardPreset<br>
 * Defines methods to obtain all cards in Codex Naturalis
 */
public class CardsPreset {

    /**
     * Gets a list of all resource cards
     * @return list of Resource cards
     * @throws IOException thrown if I/O operations are interrupted or failed
     */
    public static ArrayList<ResourceCard> getResourceCards() throws IOException {
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

        return new ArrayList<>(Arrays.asList(arrayCard));
    }


    /**
     * Gets a list of all golden cards
     * @return list of golden cards
     * @throws IOException thrown if I/O operations are interrupted or failed
     */
    public static ArrayList<GoldenCard> getGoldenCards() throws IOException {
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

        return new ArrayList<>(Arrays.asList(arrayCard));
    }

    /**
     * Gets a list of all starter cards
     * @return list of starter cards
     * @throws IOException thrown if I/O operations are interrupted or failed
     */
    public static ArrayList<StarterCard> getStarterCards() throws IOException {
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

        return new ArrayList<>(Arrays.asList(arrayCard));
    }
}
