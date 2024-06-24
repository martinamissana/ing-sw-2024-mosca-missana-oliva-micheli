package it.polimi.ingsw.view.GUI;

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

    public void setMainLayout(MainLayout mainLayout) {
        this.mainLayout = mainLayout;
    }

    public void setWinners(HashMap<Player, Integer> scoreboard, HashMap<Player, Integer> goalsDone, ArrayList<Player> winners) {
        HashMap<Player, Pawn> pawns = new HashMap<>();
        for (Player p : scoreboard.keySet()) {
            pawns.put(p, p.getPawn());
        }

        ArrayList<Player> sortedPlayers = new ArrayList<>(scoreboard.keySet());
        sortedPlayers.sort((p1, p2) -> {
            int scoreComparison = scoreboard.get(p1).compareTo(scoreboard.get(p2));
            if (scoreComparison != 0) {
                return scoreComparison;
            } else {
                return goalsDone.get(p1).compareTo(goalsDone.get(p2));
            }
        });

        switch (pawns.get(sortedPlayers.getFirst())) {
            case RED -> pawn1.setFill(Color.RED);
            case YELLOW -> pawn1.setFill(Color.GOLD);
            case GREEN -> pawn1.setFill(Color.GREEN);
            case BLUE -> pawn1.setFill(Color.BLUE);
        }
        player1.setText(sortedPlayers.getFirst().getNickname());
        points1.setText(String.valueOf(scoreboard.get(sortedPlayers.getFirst())));
        goals1.setText(String.valueOf(goalsDone.get(sortedPlayers.getFirst())));
        if (winners.contains(sortedPlayers.getFirst())) crown1.setImage(new Image(getClass().getResource("/images/crown.png").toString()));

        if (scoreboard.keySet().size() > 1) {
            switch (pawns.get(sortedPlayers.get(1))) {
                case RED -> pawn2.setFill(Color.RED);
                case YELLOW -> pawn2.setFill(Color.GOLD);
                case GREEN -> pawn2.setFill(Color.GREEN);
                case BLUE -> pawn2.setFill(Color.BLUE);
            }
            player2.setText(sortedPlayers.get(1).getNickname());
            points2.setText(String.valueOf(scoreboard.get(sortedPlayers.get(1))));
            goals2.setText(String.valueOf(goalsDone.get(sortedPlayers.get(1))));
            if (winners.contains(sortedPlayers.get(1))) crown2.setImage(new Image(getClass().getResource("/images/crown.png").toString()));
        }

        if (scoreboard.keySet().size() > 2) {
            switch (pawns.get(sortedPlayers.get(2))) {
                case RED -> pawn3.setFill(Color.RED);
                case YELLOW -> pawn3.setFill(Color.GOLD);
                case GREEN -> pawn3.setFill(Color.GREEN);
                case BLUE -> pawn3.setFill(Color.BLUE);
            }
            player3.setText(sortedPlayers.get(2).getNickname());
            points3.setText(String.valueOf(scoreboard.get(sortedPlayers.get(2))));
            goals3.setText(String.valueOf(goalsDone.get(sortedPlayers.get(2))));
            if (winners.contains(sortedPlayers.get(2))) crown3.setImage(new Image(getClass().getResource("/images/crown.png").toString()));
        }

        if (scoreboard.keySet().size() > 3) {
            switch (pawns.get(sortedPlayers.get(3))) {
                case RED -> pawn4.setFill(Color.RED);
                case YELLOW -> pawn4.setFill(Color.GOLD);
                case GREEN -> pawn4.setFill(Color.GREEN);
                case BLUE -> pawn4.setFill(Color.BLUE);
            }
            player4.setText(sortedPlayers.get(3).getNickname());
            points4.setText(String.valueOf(scoreboard.get(sortedPlayers.get(3))));
            goals4.setText(String.valueOf(goalsDone.get(sortedPlayers.get(3))));
            if (winners.contains(sortedPlayers.get(3))) crown4.setImage(new Image(getClass().getResource("/images/crown.png").toString()));
        }

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

    public void returnToMainMenu() {
        mainLayout.setScene("Open");
    }
}
