package it.polimi.ingsw.model.game;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import it.polimi.ingsw.model.commonItem.ItemBox;
import it.polimi.ingsw.model.goal.DiagonalGoal;
import it.polimi.ingsw.model.goal.L_ShapeGoal;
import it.polimi.ingsw.model.goal.ResourceGoal;
import it.polimi.ingsw.model.serialization.ItemboxDeserializer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * Class GoalsPreset<br>
 * Defines methods to obtain all goals in Codex Naturalis
 */
public class GoalsPreset {

    /**
     * Gets a list of all diagonal goals
     * @return ArrayList of diagonal goals
     * @throws IOException thrown if I/O operations are interrupted or failed
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
     * Gets a list of all L_shape goals
     * @return ArrayList of L_ShapeGoals
     * @throws IOException thrown if I/O operations are interrupted or failed
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
     * Gets a list of all resource goals
     * @return ArrayList of Resource Goals
     * @throws IOException thrown if I/O operations are interrupted or failed
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
