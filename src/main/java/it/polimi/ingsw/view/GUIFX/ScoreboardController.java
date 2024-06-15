package it.polimi.ingsw.view.GUIFX;

import it.polimi.ingsw.model.game.Lobby;
import it.polimi.ingsw.model.player.Pawn;
import it.polimi.ingsw.model.player.Player;
import it.polimi.ingsw.network.netMessage.NetMessage;
import it.polimi.ingsw.network.netMessage.s2c.TurnChangedMessage;
import it.polimi.ingsw.view.GUI.ViewSingleton;
import it.polimi.ingsw.view.ViewObserver;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.control.Button;

import javafx.scene.input.MouseEvent;
import java.io.IOException;
import java.util.HashMap;
import java.util.Objects;

public class ScoreboardController implements ViewObserver {
    private final ViewSingleton viewSing = ViewSingleton.getInstance();
    private final Lobby lobby = viewSing.getView().getLobbies().get(viewSing.getView().getID());


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
            if(viewSing.getView().getNickname().equals(player1.toString())) return;
        }
        if(mouseEvent.getSource().equals(pawn2)){
            if(viewSing.getView().getNickname().equals(player2.toString())) return;
        }
        if(mouseEvent.getSource().equals(pawn3)){
            if(viewSing.getView().getNickname().equals(player3.toString())) return;
        }
        if(mouseEvent.getSource().equals(pawn4)){
            if(viewSing.getView().getNickname().equals(player4.toString())) return;
        }
    }

    public void initialize() {
        viewSing.getView().addObserver(this);
        player1.setText(lobby.getPlayers().get(0).getNickname());
        player2.setText(lobby.getPlayers().get(1).getNickname());
        setBgColor(pawn1,lobby.getPlayers().get(0).getPawn());
        setBgColor(pawn2,lobby.getPlayers().get(1).getPawn());
        pawn1.setOnAction(actionEvent -> {});
        pawn2.setOnAction(actionEvent -> {});
        if(lobby.getPlayers().size() == 2) {
            player3.setVisible(false);
            player4.setVisible(false);
            score3.setVisible(false);
            score4.setVisible(false);
            pawn3.setVisible(false);
            pawn4.setVisible(false);
        } else if (lobby.getPlayers().size() == 3) {
            player3.setText(lobby.getPlayers().get(2).getNickname());
            setBgColor(pawn3,lobby.getPlayers().get(2).getPawn());
            pawn3.setOnAction(actionEvent -> {});
            player4.setVisible(false);
            score4.setVisible(false);
            pawn4.setVisible(false);
        }
        else{
            player3.setText(lobby.getPlayers().get(2).getNickname());
            setBgColor(pawn3,lobby.getPlayers().get(2).getPawn());
            pawn3.setOnAction(actionEvent -> {});
            player4.setText(lobby.getPlayers().get(3).getNickname());
            setBgColor(pawn4,lobby.getPlayers().get(3).getPawn());
            pawn4.setOnAction(actionEvent -> {});
        }
    }

    public void refresh() {
        HashMap<Player,Integer> scoreboard = viewSing.getView().getScoreboard();
        score1.setText(scoreboard.get(lobby.getPlayers().get(0)).toString());
        score2.setText(scoreboard.get(lobby.getPlayers().get(1)).toString());
        if(lobby.getNumOfPlayers()>2) score3.setText(scoreboard.get(lobby.getPlayers().get(2)).toString());
        if(lobby.getNumOfPlayers()>3) score4.setText(scoreboard.get(lobby.getPlayers().get(3)).toString());
    }

    private void setBgColor(Button button, Pawn pawn) {
        button.setStyle("-fx-background-color: " + pawn.toString().toLowerCase());
    }

    @Override
    public void update(NetMessage message) throws IOException {
        if (Objects.requireNonNull(message) instanceof TurnChangedMessage) {
            refresh();
        }
    }

}
