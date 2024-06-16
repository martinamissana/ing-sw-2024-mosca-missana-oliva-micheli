package it.polimi.ingsw.model.game;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import it.polimi.ingsw.model.commonItem.ItemBox;
import it.polimi.ingsw.model.goal.*;
import it.polimi.ingsw.model.serialization.ItemboxDeserializer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * Class GoalsPreset
 * defines the methods to obtain all type of goals in Codex Naturalis
 */
public class GoalsPreset {

    /**
     * gets all the diagonal goals in a list
     *
     * @return ArrayList<DiagonalGoal>
     * @throws IOException produced by failed or interrupted I/O operations
     */
    public static ArrayList<DiagonalGoal> getDiagonalGoals() throws IOException {

        InputStream inputStream = CardsPreset.class.getClassLoader().getResourceAsStream("diagonal_goals.json");

        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
        StringBuilder jsonBuilder = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
            jsonBuilder.append(line);
        }
        String json = jsonBuilder.toString();

        GsonBuilder builder = new GsonBuilder();

        Gson gson = builder.create();

        DiagonalGoal[] arrayGoal = gson.fromJson(json, DiagonalGoal[].class);

        return new ArrayList<>(Arrays.asList(arrayGoal));
    }

    /**
     * gets all the L_shape goals in a list
     *
     * @return ArrayList<L_ShapeGoal>
     * @throws IOException
     */
    public static ArrayList<L_ShapeGoal> getLShapeGoals() throws IOException {

        InputStream inputStream = CardsPreset.class.getClassLoader().getResourceAsStream("L_shape_goals.json");

        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
        StringBuilder jsonBuilder = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
            jsonBuilder.append(line);
        }
        String json = jsonBuilder.toString();

        GsonBuilder builder = new GsonBuilder();

        Gson gson = builder.create();

        L_ShapeGoal[] arrayGoal = gson.fromJson(json, L_ShapeGoal[].class);

        return new ArrayList<>(Arrays.asList(arrayGoal));
    }

    /**
     * gets all the resource goals in a list
     *
     * @return ArrayList<ResourceGoal>
     * @throws IOException
     */
    public static ArrayList<ResourceGoal> getResourceGoals() throws IOException {

        InputStream inputStream = CardsPreset.class.getClassLoader().getResourceAsStream("resource_goals.json");

        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
        StringBuilder jsonBuilder = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
            jsonBuilder.append(line);
        }
        String json = jsonBuilder.toString();

        GsonBuilder builder = new GsonBuilder();
        builder.registerTypeAdapter(ItemBox.class, new ItemboxDeserializer());
        Gson gson = builder.create();

        ResourceGoal[] arrayGoal = gson.fromJson(json, ResourceGoal[].class);

        return new ArrayList<>(Arrays.asList(arrayGoal));
    }
}
