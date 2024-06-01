package it.polimi.ingsw.view.GUI.Lobby;

import it.polimi.ingsw.controller.exceptions.GameAlreadyStartedException;
import it.polimi.ingsw.controller.exceptions.NotConnectedToLobbyException;
import it.polimi.ingsw.controller.exceptions.UnexistentUserException;
import it.polimi.ingsw.model.exceptions.GameDoesNotExistException;
import it.polimi.ingsw.model.exceptions.LobbyDoesNotExistsException;
import it.polimi.ingsw.model.exceptions.PawnAlreadyTakenException;
import it.polimi.ingsw.model.game.Lobby;
import it.polimi.ingsw.model.player.Pawn;
import it.polimi.ingsw.network.netMessage.NetMessage;
import it.polimi.ingsw.network.netMessage.s2c.PawnAssignedMessage;
import it.polimi.ingsw.view.GUI.ViewSingleton;
import it.polimi.ingsw.view.View;
import it.polimi.ingsw.view.ViewController;
import it.polimi.ingsw.view.ViewObserver;
import javafx.application.Platform;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.util.Builder;

import java.io.IOException;

public class PawnChooserWidget implements Builder<Region>, ViewObserver {

    private final View view = ViewSingleton.getInstance().getView();
    private final ViewController viewCon = ViewSingleton.getInstance().getViewController();
    private Lobby lobby;

    private final TilePane pawnsPane = new TilePane();
    private final SimpleBooleanProperty pawnChosen = new SimpleBooleanProperty(false);

    @Override
    public Region build() {
        view.addObserver(this);

        pawnsPane.setBackground(new Background(new BackgroundFill(Color.valueOf("#FFFFFFBF"), new CornerRadii(12.5), null)));
        pawnsPane.setPadding(new Insets(5, 5, 5, 5));
        pawnsPane.setAlignment(Pos.CENTER);
        pawnsPane.setMaxSize(Region.USE_PREF_SIZE, Region.USE_PREF_SIZE);

        refresh();
        return pawnsPane;
    }

    public void refresh() {
        lobby = view.getLobbies().get(view.getID());
        if (lobby == null) return;
        pawnsPane.getChildren().clear();
        for (Pawn pawn : lobby.getPawnBuffer().getPawnList())
            pawnsPane.getChildren().add(createPawn(pawn));
    }

    private Button createPawn(Pawn pawnColor) {
        Button pawn = new Button();
        pawn.setId(pawnColor.toString());
        pawn.disableProperty().bindBidirectional(pawnChosen);

//        Color color = null;
//        switch (pawnColor) {
//            case BLUE: color = new Color(0, 0, 1, 0); break;
//            case RED: color = new Color(1, 0, 0, 0); break;
//            case GREEN: color = new Color(0, 1, 0, 0); break;
//            case YELLOW: color = new Color(1, 1, 0, 0); break;
//            default: {}
//        }

        switch (pawnColor) {
            case BLUE: pawn.setText("b"); break;
            case RED: pawn.setText("r"); break;
            case GREEN: pawn.setText("g"); break;
            case YELLOW: pawn.setText("y"); break;
        }
        //pawn.setBackground(new Background(new BackgroundFill(color, null, null)));
        pawn.setStyle("-fx-max-height: 30; -fx-min-height: 30; -fx-max-width: 30; -fx-min-width: 30; -fx-background-radius: 15;");
        pawn.setFocusTraversable(true);
        pawn.setOnAction(actionEvent -> {
            try {
                viewCon.checkChoosePawn(pawnColor);
                view.choosePawn(pawnColor);
            } catch (NotConnectedToLobbyException | PawnAlreadyTakenException | GameAlreadyStartedException |
                     LobbyDoesNotExistsException | IOException | GameDoesNotExistException | UnexistentUserException e) {
                throw new RuntimeException(e);
            }
            pawnChosen.set(true);
        });
        return pawn;
    }

    @Override
    public void update(NetMessage message) throws IOException {
        switch (message) {
            case PawnAssignedMessage m -> Platform.runLater(() -> {
                if (m.getID().equals(lobby.getID()))
                    pawnsPane.getChildren().removeIf(p -> p.getId().equals(m.getColor().toString()));
            });
            default -> {}
        }
    }
}
