package it.polimi.ingsw.view.GUI;

import it.polimi.ingsw.model.game.Lobby;
import it.polimi.ingsw.model.player.Coords;
import it.polimi.ingsw.model.player.Player;
import it.polimi.ingsw.network.netMessage.NetMessage;
import it.polimi.ingsw.network.netMessage.s2c.CardPlacedOnFieldMessage;
import it.polimi.ingsw.network.netMessage.s2c.GameWinnersAnnouncedMessage;
import it.polimi.ingsw.network.netMessage.s2c.LobbyLeftMessage;
import it.polimi.ingsw.view.View;
import it.polimi.ingsw.view.ViewObserver;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;

import java.io.IOException;

/**
 * Class OtherPlayersController
 * allows the player to visualize other players' field
 */
public class OtherPlayersController implements ViewObserver {

    private String nickname;
    private View view;
    private Pane visible;

    @FXML
    private GridPane fieldGrid;
    private int starterX = 4;
    private int starterY = 4;

    /**
     * links player and field
     * @param nickname name of the player
     * @param lobby the lobby the player is in
     */
    public void setPlayer(String nickname, Lobby lobby) {
        this.nickname = nickname;
        for(Player p : lobby.getPlayers()) {
            if(nickname.equals(p.getNickname())) {
                fieldGrid.add(new CardBuilder(view.getFields().get(p).getMatrix().get(new Coords(0,0))).getCardImage(), starterX, starterY);
            }
        }
    }

    /**
     * @param view the view to observe
     */
    public void setView(View view) {
        this.view = view;
        this.view.addObserver(this);
    }

    /**
     * allows scoreboard buttons to set the pane as visible
     * @param visible the pane used to visualize the fields
     */
    public void setVisible(Pane visible) {
        this.visible = visible;
    }

    /**
     * Method used to add a row at the top in the grid pane when the field gets bigger
     */
    private void shiftRows() {
        fieldGrid.getChildren().forEach(node -> {
            Integer row = GridPane.getRowIndex(node);
            if (row != null) {
                GridPane.setRowIndex(node, row + 1);
            }
        });
    }

    /**
     * Method used to add a column to the left in the grid pane when the field gets bigger
     */
    private void shiftColumns() {
        fieldGrid.getChildren().forEach(node -> {
            Integer column = GridPane.getColumnIndex(node);
            if (column != null) {
                GridPane.setColumnIndex(node, column + 1);
            }
        });
    }

    @Override
    public void update(NetMessage message) throws IOException {
        switch (message){
            case CardPlacedOnFieldMessage m -> {
                if(m.getNickname().equals(nickname)){
                    Platform.runLater(() -> {
                        int x = starterX + m.getCoords().getX() - m.getCoords().getY();
                        int y = starterY - m.getCoords().getX() - m.getCoords().getY();

                        if (x == 0) {
                            shiftColumns();
                            fieldGrid.addColumn(0);
                            starterX++;
                            x = 1;
                        }

                        if (y == 0) {
                            shiftRows();
                            fieldGrid.addRow(0);
                            starterY++;
                            y = 1;
                        }

                        fieldGrid.add(new CardBuilder(m.getCard()).getCardImage(), x, y);
                    });
                }
            }
            case LobbyLeftMessage ignored -> view.removeObserver(this);
            case GameWinnersAnnouncedMessage ignored -> view.removeObserver(this);
            default -> {}
        }
    }

    /**
     * sets the pane as not visible
     */
    public void close() {
        visible.getChildren().clear();
        visible.setVisible(false);
    }
}
