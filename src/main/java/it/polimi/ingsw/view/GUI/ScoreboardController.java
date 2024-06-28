package it.polimi.ingsw.view.GUI;

import it.polimi.ingsw.model.game.GamePhase;
import it.polimi.ingsw.model.game.Lobby;
import it.polimi.ingsw.model.player.Pawn;
import it.polimi.ingsw.model.player.Player;

import it.polimi.ingsw.network.netMessage.NetMessage;
import it.polimi.ingsw.network.netMessage.s2c.GamePhaseChangedMessage;
import it.polimi.ingsw.network.netMessage.s2c.GameWinnersAnnouncedMessage;
import it.polimi.ingsw.network.netMessage.s2c.LobbyLeftMessage;
import it.polimi.ingsw.network.netMessage.s2c.ScoreIncrementedMessage;
import it.polimi.ingsw.view.View;
import it.polimi.ingsw.view.ViewObserver;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.TextField;
import javafx.scene.control.Button;

import javafx.scene.control.Tooltip;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.util.Duration;

import java.io.IOException;
import java.util.HashMap;

/**
 * Class ScoreboardController
 * used in lobby to visualize the players and in game to visualize the players and their points
 */
public class ScoreboardController implements ViewObserver {
    private View view;
    private Lobby lobby;
    private Pane field1;
    private Pane field2;
    private Pane field3;
    private Pane field4;
    private Pane otherField;

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
    private StackPane infoField;

    /**
     * method to visualize other players' fields
     * @param mouseEvent the player selected
     */
    @FXML
    public void showField(MouseEvent mouseEvent) {
        if (!(view.getGamePhase().equals(GamePhase.PLAYING_GAME))) return;
        if (mouseEvent.getSource().equals(pawn1)) {
            if (view.getNickname().equals(lobby.getPlayers().getFirst().getNickname())) return;
            otherField.getChildren().clear();
            otherField.getChildren().add(field1);
            otherField.setVisible(true);
        }
        if (mouseEvent.getSource().equals(pawn2)) {
            if (view.getNickname().equals(lobby.getPlayers().get(1).getNickname())) return;
            otherField.getChildren().clear();
            otherField.getChildren().add(field2);
            otherField.setVisible(true);
        }
        if (mouseEvent.getSource().equals(pawn3)) {
            if (view.getNickname().equals(lobby.getPlayers().get(2).getNickname())) return;
            otherField.getChildren().clear();
            otherField.getChildren().add(field3);
            otherField.setVisible(true);
        }
        if (mouseEvent.getSource().equals(pawn4)) {
            if (view.getNickname().equals(lobby.getPlayers().get(3).getNickname())) return;
            otherField.getChildren().clear();
            otherField.getChildren().add(field4);
            otherField.setVisible(true);
        }
    }

    /**
     * sets the scoreboard for players' visualization in lobby
     * @param lobby the lobby
     */
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
            pawn3.setOnMouseClicked(mouseEvent -> {
            });
        }
        if (lobby.getPlayers().size() < 3) {
            player3.setVisible(false);
            pawn3.setVisible(false);
            pawn2.setOnMouseClicked(mouseEvent -> {
            });
        }
        if (lobby.getPlayers().size() < 2) {
            player2.setVisible(false);
            pawn2.setVisible(false);
            pawn1.setOnMouseClicked(mouseEvent -> {
            });
        }
        score1.setVisible(false);
        score2.setVisible(false);
        score3.setVisible(false);
        score4.setVisible(false);
    }

    /**
     * adds the class as a view observer
     * @param view the observable view
     */
    public void setView(View view) {
        this.view = view;
        view.addObserver(this);
    }


    /**
     * sets the scoreboard when the game starts
     * @param lobby lobby used to sets the players
     */
    public void setScoreboard(Lobby lobby) {
        this.lobby = lobby;
        this.title.setText("SCOREBOARD");

        infoField.setVisible(true);
        Tooltip tooltipFields = new Tooltip("Click a pawn to see other players' fields");
        Tooltip.install(infoField, tooltipFields);
        tooltipFields.setShowDelay(new Duration(0.2));

        setPlayersAndPawns(0, player1, pawn1);
        score1.setText(view.getScoreboard().get(lobby.getPlayers().getFirst()).toString());
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

    /**
     * @param index position of the player in the list
     * @param player the player's name text field
     * @param pawn the pawn button
     */
    public void setPlayersAndPawns(int index, TextField player, Button pawn) {
        player.setText(lobby.getPlayers().get(index).getNickname());
        setBgColor(pawn, lobby.getPlayers().get(index).getPawn());
        pawn.setOnAction(actionEvent -> {
        });
    }

    /**
     * updates the scores of the players
     */
    public void refresh() {
        HashMap<Player, Integer> scoreboard = view.getScoreboard();
        score1.setText(scoreboard.get(lobby.getPlayers().get(0)).toString());
        score2.setText(scoreboard.get(lobby.getPlayers().get(1)).toString());
        if (lobby.getNumOfPlayers() > 2) score3.setText(scoreboard.get(lobby.getPlayers().get(2)).toString());
        if (lobby.getNumOfPlayers() > 3) score4.setText(scoreboard.get(lobby.getPlayers().get(3)).toString());
    }

    /**
     * sets the color of the pawn button
     * @param button pawn button
     * @param pawn pawn
     */
    private void setBgColor(Button button, Pawn pawn) {
        if (pawn == Pawn.YELLOW) button.setStyle("-fx-background-color: gold");
        else if (pawn != null) button.setStyle("-fx-background-color: " + pawn.toString().toLowerCase());
    }

    /**
     * @param otherField field of the selected player
     */
    public void setOtherField(Pane otherField) {
        this.otherField = otherField;
        otherField.setVisible(false);
    }

    @Override
    public void update(NetMessage message) throws IOException {
        switch (message) {
            case ScoreIncrementedMessage ignored -> refresh();
            case GamePhaseChangedMessage ignored -> {
                if (view.getGamePhase().equals(GamePhase.PLAYING_GAME)) {
                    if (!lobby.getPlayers().get(0).getNickname().equals(view.getNickname())) {
                        Platform.runLater(() -> {
                            FXMLLoader field1Loader = new FXMLLoader(getClass().getResource("/fxml/OtherPlayersField.fxml"));
                            try {
                                field1 = field1Loader.load();
                            } catch (IOException e) {
                                throw new RuntimeException(e);
                            }
                            OtherPlayersController field1Controller = field1Loader.getController();
                            field1Controller.setView(view);
                            field1Controller.setPlayer(lobby.getPlayers().getFirst().getNickname(), lobby);
                            field1Controller.setVisible(otherField);
                        });
                    }
                    if (!lobby.getPlayers().get(1).getNickname().equals(view.getNickname())) {
                        Platform.runLater(() -> {
                            FXMLLoader field2Loader = new FXMLLoader(getClass().getResource("/fxml/OtherPlayersField.fxml"));
                            try {
                                field2 = field2Loader.load();
                            } catch (IOException e) {
                                throw new RuntimeException(e);
                            }
                            OtherPlayersController field2Controller = field2Loader.getController();
                            field2Controller.setView(view);
                            field2Controller.setPlayer(lobby.getPlayers().get(1).getNickname(), lobby);
                            field2Controller.setVisible(otherField);
                        });
                    }
                    if (lobby.getPlayers().size() > 2) {
                        if (!lobby.getPlayers().get(2).getNickname().equals(view.getNickname())) {
                            Platform.runLater(() -> {
                                FXMLLoader field3Loader = new FXMLLoader(getClass().getResource("/fxml/OtherPlayersField.fxml"));
                                try {
                                    field3 = field3Loader.load();
                                } catch (IOException e) {
                                    throw new RuntimeException(e);
                                }
                                OtherPlayersController field3Controller = field3Loader.getController();
                                field3Controller.setView(view);
                                field3Controller.setPlayer(lobby.getPlayers().get(2).getNickname(), lobby);
                                field3Controller.setVisible(otherField);
                            });
                        }
                    }
                    if (lobby.getPlayers().size() == 4) {
                        if (!lobby.getPlayers().get(3).getNickname().equals(view.getNickname())) {
                            Platform.runLater(() -> {
                                FXMLLoader field4Loader = new FXMLLoader(getClass().getResource("/fxml/OtherPlayersField.fxml"));
                                try {
                                    field4 = field4Loader.load();
                                } catch (IOException e) {
                                    throw new RuntimeException(e);
                                }
                                OtherPlayersController field4Controller = field4Loader.getController();
                                field4Controller.setView(view);
                                field4Controller.setPlayer(lobby.getPlayers().get(3).getNickname(), lobby);
                                field4Controller.setVisible(otherField);
                            });
                        }
                    }
                }
            }
            case LobbyLeftMessage ignored -> view. removeObserver(this);
            case GameWinnersAnnouncedMessage ignored -> view.removeObserver(this);
            default -> {}
        }
    }
}
