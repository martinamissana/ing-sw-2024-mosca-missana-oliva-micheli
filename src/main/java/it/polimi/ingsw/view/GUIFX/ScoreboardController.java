package it.polimi.ingsw.view.GUIFX;

import it.polimi.ingsw.model.game.Lobby;
import it.polimi.ingsw.model.player.Pawn;
import it.polimi.ingsw.model.player.Player;

import it.polimi.ingsw.view.View;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.control.Button;

import javafx.scene.input.MouseEvent;
import java.util.HashMap;

public class ScoreboardController{
    private View view;
    private Lobby lobby;

    @FXML
    private TextField title;
    @FXML
    private TextField player1;
    @FXML
    private TextField player2;
    @FXML
    private TextField player3;
    @FXML
    private TextField player4;
    @FXML
    private TextField score1;
    @FXML
    private TextField score2;
    @FXML
    private TextField score3;
    @FXML
    private TextField score4;
    @FXML
    private Button pawn1;
    @FXML
    private Button pawn2;
    @FXML
    private Button pawn3;
    @FXML
    private Button pawn4;

    @FXML
    public void showField(MouseEvent mouseEvent){
        if(mouseEvent.getSource().equals(pawn1)){
            if(view.getNickname().equals(player1.toString())) return;
        }
        if(mouseEvent.getSource().equals(pawn2)){
            if(view.getNickname().equals(player2.toString())) return;
        }
        if(mouseEvent.getSource().equals(pawn3)){
            if(view.getNickname().equals(player3.toString())) return;
        }
        if(mouseEvent.getSource().equals(pawn4)){
            if(view.getNickname().equals(player4.toString())) return;
        }
    }

    public void setPlayers(Lobby lobby) {
        this.lobby = lobby;
        title.setText("PLAYERS");

        setPlayersAndPawns(0, player1, pawn1);      // There is always at least one player
        if (lobby.getPlayers().size() > 1) setPlayersAndPawns(1, player2, pawn2);
        if (lobby.getPlayers().size() > 2) setPlayersAndPawns(2, player3, pawn3);
        if (lobby.getPlayers().size() > 3) setPlayersAndPawns(3, player4, pawn4);

        if (lobby.getPlayers().size() < 4) {
            player4.setVisible(false);
            pawn4.setVisible(false);
            pawn3.setOnMouseClicked(mouseEvent -> {});
        }
        if (lobby.getPlayers().size() < 3) {
            player3.setVisible(false);
            pawn3.setVisible(false);
            pawn2.setOnMouseClicked(mouseEvent -> {});
        }
        if (lobby.getPlayers().size() < 2) {
            player2.setVisible(false);
            pawn2.setVisible(false);
            pawn1.setOnMouseClicked(mouseEvent -> {});
        }
        score1.setVisible(false);
        score2.setVisible(false);
        score3.setVisible(false);
        score4.setVisible(false);
    }

    public void setView (View view){
        this.view = view;
    }


    public void setScoreboard(Lobby lobby) {
        this.lobby=lobby;
        this.title.setText("SCOREBOARD");
        setPlayersAndPawns(0, player1, pawn1);
        score1.setText(view.getScoreboard().get(lobby.getPlayers().get(0)).toString());
        setPlayersAndPawns(1, player2, pawn2);
        score2.setText(view.getScoreboard().get(lobby.getPlayers().get(1)).toString());

        if (lobby.getPlayers().size() > 2) {
            setPlayersAndPawns(2, player3, pawn3);
            score3.setText(view.getScoreboard().get(lobby.getPlayers().get(2)).toString());
            if (lobby.getPlayers().size() == 4) {
                setPlayersAndPawns(3, player4, pawn4);
                score4.setText(view.getScoreboard().get(lobby.getPlayers().get(3)).toString());
            } else {
                player4.setVisible(false);
                score4.setVisible(false);
                pawn4.setVisible(false);
            }
        } else {
            player3.setVisible(false);
            score3.setVisible(false);
            pawn3.setVisible(false);
            player4.setVisible(false);
            score4.setVisible(false);
            pawn4.setVisible(false);
        }
    }

    public void setPlayersAndPawns(int index, TextField player, Button pawn) {
        player.setText(lobby.getPlayers().get(index).getNickname());
        setBgColor(pawn, lobby.getPlayers().get(index).getPawn());
        pawn.setOnAction(actionEvent -> {});
    }

    public void refresh() {
        HashMap<Player, Integer> scoreboard = view.getScoreboard();
        score1.setText(scoreboard.get(lobby.getPlayers().get(0)).toString());
        score2.setText(scoreboard.get(lobby.getPlayers().get(1)).toString());
        if (lobby.getNumOfPlayers() > 2) score3.setText(scoreboard.get(lobby.getPlayers().get(2)).toString());
        if (lobby.getNumOfPlayers() > 3) score4.setText(scoreboard.get(lobby.getPlayers().get(3)).toString());
    }

    private void setBgColor(Button button, Pawn pawn) {
        if (pawn == Pawn.YELLOW) button.setStyle("-fx-background-color: gold");
        else if (pawn != null) button.setStyle("-fx-background-color: " + pawn.toString().toLowerCase());
    }
}
