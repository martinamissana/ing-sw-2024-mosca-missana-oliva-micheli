package it.polimi.ingsw.view.GUI;

import it.polimi.ingsw.model.game.Lobby;
import it.polimi.ingsw.model.player.Pawn;
import it.polimi.ingsw.model.player.Player;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Class WinnersController
 * handles the final screen
 */
public class WinnersController {
    @FXML
    private Circle pawn1;
    @FXML
    private ImageView crown1;
    @FXML
    private TextField player1;
    @FXML
    private TextField points1;
    @FXML
    private TextField goals1;

    @FXML
    private Circle pawn2;
    @FXML
    private ImageView crown2;
    @FXML
    private TextField player2;
    @FXML
    private TextField points2;
    @FXML
    private TextField goals2;

    @FXML
    private Circle pawn3;
    @FXML
    private ImageView crown3;
    @FXML
    private TextField player3;
    @FXML
    private TextField points3;
    @FXML
    private TextField goals3;


    @FXML
    private Circle pawn4;
    @FXML
    private ImageView crown4;
    @FXML
    private TextField player4;
    @FXML
    private TextField points4;
    @FXML
    private TextField goals4;

    private MainLayout mainLayout;
    private HashMap<Player, Integer> scoreboard;
    private HashMap<Player, Integer> goalsDone;
    private ArrayList<Player> winners;
    private HashMap<Player, Pawn> pawns;

    /**
     * @param mainLayout the main layout, used to return to main menu
     */
    public void setMainLayout(MainLayout mainLayout) {
        this.mainLayout = mainLayout;
    }

    /**
     * adds the winners to the screen
     * @param scoreboard final scoreboard
     * @param goalsDone number of goals done
     * @param winners list of winners
     */
    public void setWinners(HashMap<Player, Integer> scoreboard, HashMap<Player, Integer> goalsDone, ArrayList<Player> winners) {
        ArrayList<Player> players = new ArrayList<>(scoreboard.keySet());
        this.scoreboard = scoreboard;
        this.goalsDone = goalsDone;
        this.winners = winners;

        pawns = new HashMap<>();
        for (Player p : scoreboard.keySet()) {
            pawns.put(p, p.getPawn());
        }

        setPlayer(players, player1, pawn1, points1, goals1, crown1, 0);
        if (scoreboard.keySet().size() > 1) setPlayer(players, player2, pawn2, points2, goals2, crown2, 1);
        if (scoreboard.keySet().size() > 2) setPlayer(players, player3, pawn3, points3, goals3, crown3, 2);
        if (scoreboard.keySet().size() > 3) setPlayer(players, player4, pawn4, points4, goals4, crown4, 3);

        if (scoreboard.keySet().size() < 4) {
            pawn4.setVisible(false);
            crown4.setVisible(false);
            player4.setVisible(false);
            points4.setVisible(false);
            goals4.setVisible(false);
        }

        if (scoreboard.keySet().size() < 3) {
            pawn3.setVisible(false);
            crown3.setVisible(false);
            player3.setVisible(false);
            points3.setVisible(false);
            goals3.setVisible(false);
        }

        if (scoreboard.keySet().size() < 2) {
            pawn2.setVisible(false);
            crown2.setVisible(false);
            player2.setVisible(false);
            points2.setVisible(false);
            goals2.setVisible(false);
        }
    }

    public void setPlayersFromLobby(Lobby lobby) {
        pawns = new HashMap<>();
        for (Player p : lobby.getPlayers()) {
            pawns.put(p, p.getPawn());
        }

        setPlayer(lobby.getPlayers(), player1, pawn1, null, null, null, 0);
        if (lobby.getPlayers().size() > 1) setPlayer(lobby.getPlayers(), player2, pawn2, null, null, null, 1);
        if (lobby.getPlayers().size() > 2) setPlayer(lobby.getPlayers(), player3, pawn3, null, null, null, 2);
        if (lobby.getPlayers().size() > 3) setPlayer(lobby.getPlayers(), player4, pawn4, null, null, null, 3);

        crown2.setVisible(false);
        points2.setVisible(false);
        goals2.setVisible(false);

        crown3.setVisible(false);
        points3.setVisible(false);
        goals3.setVisible(false);

        crown4.setVisible(false);
        points4.setVisible(false);
        goals4.setVisible(false);

        if (lobby.getPlayers().size() < 4) {
            player4.setVisible(false);
            pawn4.setVisible(false);
        }

        if (lobby.getPlayers().size() < 3) {
            player3.setVisible(false);
            pawn3.setVisible(false);
        }

        if (lobby.getPlayers().size() < 2) {
            player2.setVisible(false);
            pawn2.setVisible(false);
        }
    }

    /**
     * sets the players adding a crown to the winners
     * @param players players sorted by points
     * @param player player's text field
     * @param pawn pawn shape
     * @param points points of the player
     * @param goals text field with goals done
     * @param crown image of the crown
     * @param index index of the player
     */
    public void setPlayer(ArrayList<Player> players, TextField player, Circle pawn, TextField points, TextField goals, ImageView crown, int index) {
        switch (pawns.get(players.get(index))) {
            case RED -> pawn.setFill(Color.RED);
            case YELLOW -> pawn.setFill(Color.GOLD);
            case GREEN -> pawn.setFill(Color.GREEN);
            case BLUE -> pawn.setFill(Color.BLUE);
        }
        player.setText(players.get(index).getNickname());
        if (points != null) points.setText(String.valueOf(scoreboard.get(players.get(index))));
        if (goals != null) goals.setText(String.valueOf(goalsDone.get(players.get(index))));
        if (crown != null && winners.contains(players.get(index))) crown.setImage(new Image(getClass().getResource("/images/crown.png").toExternalForm()));
    }

    /**
     * method to go back to main menu
     */
    public void returnToMainMenu() {
        mainLayout.setScene("Open");
    }
}
