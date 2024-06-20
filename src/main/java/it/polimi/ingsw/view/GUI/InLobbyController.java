package it.polimi.ingsw.view.GUI;

import it.polimi.ingsw.controller.exceptions.GameAlreadyStartedException;
import it.polimi.ingsw.controller.exceptions.NotConnectedToLobbyException;
import it.polimi.ingsw.controller.exceptions.UnexistentUserException;
import it.polimi.ingsw.model.exceptions.*;
import it.polimi.ingsw.model.game.Lobby;
import it.polimi.ingsw.model.player.Pawn;
import it.polimi.ingsw.model.player.Player;
import it.polimi.ingsw.network.netMessage.NetMessage;
import it.polimi.ingsw.network.netMessage.c2s.LobbyJoinedMessage;
import it.polimi.ingsw.network.netMessage.s2c.LobbyLeftMessage;
import it.polimi.ingsw.network.netMessage.s2c.PawnAssignedMessage;
import it.polimi.ingsw.view.ViewObserver;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.concurrent.Semaphore;

public class InLobbyController implements ViewObserver, Initializable {
    @FXML
    Button quitButton;
    @FXML
    Pane players;
    @FXML
    Button redPawn;
    @FXML
    Button yellowPawn;
    @FXML
    Button greenPawn;
    @FXML
    Button bluePawn;

    private final ViewSingleton viewSingleton = ViewSingleton.getInstance();
    private Lobby lobby;
    private final Semaphore sem = new Semaphore(0);

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        viewSingleton.getView().addObserver(this);
        this.lobby = viewSingleton.getView().getLobbies().get(viewSingleton.getView().getID());
        updatePlayers();

        for (Player p : lobby.getPlayers()) {
            switch (p.getPawn()) {
                case RED -> redPawn.setVisible(false);
                case YELLOW -> yellowPawn.setVisible(false);
                case GREEN -> greenPawn.setVisible(false);
                case BLUE -> bluePawn.setVisible(false);
                case null -> {}
            }
        }
    }

    public void leaveLobby(MouseEvent mouseEvent) {
        try {
            viewSingleton.getViewController().checkLeaveLobby();
            viewSingleton.getView().leaveLobby();
            sem.acquire();

            viewSingleton.getView().removeObserver(this);

        } catch (NotConnectedToLobbyException e) {
            System.out.println("Not in lobby");
        } catch (GameAlreadyStartedException e) {
            System.out.println("Game started");
        } catch (FullLobbyException | LobbyDoesNotExistsException | NicknameAlreadyTakenException |
                 GameDoesNotExistException ignored) {}
        catch (IOException | ClassNotFoundException | UnexistentUserException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public void updatePlayers() {
        Platform.runLater(() -> {
            try {
                players.getChildren().clear();
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/Scoreboard.fxml"));
                players.getChildren().add(loader.load());
                ScoreboardController controller = loader.getController();
                controller.setPlayers(lobby);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }

    public void setPawn(MouseEvent mouseEvent) {
        try {
            if (mouseEvent.getSource().equals(redPawn)) {
                viewSingleton.getView().choosePawn(Pawn.RED);
                redPawn.setVisible(false);

                yellowPawn.setOnMouseClicked(event -> {});
                greenPawn.setOnMouseClicked(event -> {});
                bluePawn.setOnMouseClicked(event -> {});
            }
            else if (mouseEvent.getSource().equals(yellowPawn)) {
                viewSingleton.getView().choosePawn(Pawn.YELLOW);
                yellowPawn.setVisible(false);

                redPawn.setOnMouseClicked(event -> {});
                greenPawn.setOnMouseClicked(event -> {});
                bluePawn.setOnMouseClicked(event -> {});
            }
            else if (mouseEvent.getSource().equals(greenPawn)) {
                viewSingleton.getView().choosePawn(Pawn.GREEN);
                greenPawn.setVisible(false);

                redPawn.setOnMouseClicked(event -> {});
                yellowPawn.setOnMouseClicked(event -> {});
                bluePawn.setOnMouseClicked(event -> {});
            }
            else if (mouseEvent.getSource().equals(bluePawn)) {
                viewSingleton.getView().choosePawn(Pawn.BLUE);
                bluePawn.setVisible(false);

                redPawn.setOnMouseClicked(event -> {});
                yellowPawn.setOnMouseClicked(event -> {});
                greenPawn.setOnMouseClicked(event -> {});
            }

        } catch (PawnAlreadyTakenException e) {
            System.out.println("Pawn taken");
        } catch (IOException | UnexistentUserException e) {
            throw new RuntimeException(e);
        } catch (LobbyDoesNotExistsException | GameAlreadyStartedException | GameDoesNotExistException ignored) {}
    }

    @Override
    public void update(NetMessage message) throws IOException {
        switch (message) {
            case LobbyLeftMessage m -> {
                if (m.getPlayer().equals(viewSingleton.getView().getPlayer())) sem.release();
                else updatePlayers();
            }
            case LobbyJoinedMessage ignored -> updatePlayers();
            case PawnAssignedMessage m -> {
                updatePlayers();
                switch (m.getColor()) {
                    case RED -> redPawn.setVisible(false);
                    case GREEN -> greenPawn.setVisible(false);
                    case YELLOW -> yellowPawn.setVisible(false);
                    case BLUE -> bluePawn.setVisible(false);
                }
                // checkStartGame();        // GameScreen not yet implemented
            }
            default -> {}
        }
    }

    private void checkStartGame() {
        if (lobby.getPlayers().size() != lobby.getNumOfPlayers()) return;

        for (Player p : lobby.getPlayers()) {
            if (p.equals(viewSingleton.getView().getPlayer()) && viewSingleton.getView().getPawn() == null) return;
            else if (p.getPawn() == null) return;
        }

        viewSingleton.getView().removeObserver(this);
    }
}
