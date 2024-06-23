package it.polimi.ingsw.view.GUI;

import it.polimi.ingsw.model.game.Lobby;
import it.polimi.ingsw.model.player.Coords;
import it.polimi.ingsw.model.player.Player;
import it.polimi.ingsw.network.netMessage.NetMessage;
import it.polimi.ingsw.network.netMessage.s2c.CardPlacedOnFieldMessage;
import it.polimi.ingsw.view.View;
import it.polimi.ingsw.view.ViewObserver;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;

import java.io.IOException;

public class OtherPlayersController implements ViewObserver {

    private String nickname;
    private View view;
    private Pane visible;
    private Lobby lobby;

    @FXML
    private GridPane fieldGrid;
    @FXML
    private Button close;

    public void setPlayer(String nickname, Lobby lobby) {
        this.nickname = nickname;
        for(Player p : lobby.getPlayers()){
            if(nickname.equals(p.getNickname())){
                fieldGrid.add(new CardBuilder(view.getFields().get(p).getMatrix().get(new Coords(0,0))).getCardImage(), 4, 4);
            }
        }

    }

    public void setView(View view) {
        this.view = view;
        view.addObserver(this);
    }

    public void setVisible(Pane visible) {
        this.visible = visible;
    }

    @Override
    public void update(NetMessage message) throws IOException {
        switch (message){
            case CardPlacedOnFieldMessage m -> {
              if(m.getNickname().equals(nickname)){
                  int x = 4 + m.getCoords().getX() - m.getCoords().getY();
                  int y = 4 - m.getCoords().getX() - m.getCoords().getY();
                  Platform.runLater(() -> {
                      fieldGrid.add(new CardBuilder(m.getCard()).getCardImage(), x, y);
                  });
              }
            }
            default -> {}
        }
    }

    public void close(MouseEvent mouseEvent) {
        visible.getChildren().clear();
        visible.setVisible(false);
    }
}
