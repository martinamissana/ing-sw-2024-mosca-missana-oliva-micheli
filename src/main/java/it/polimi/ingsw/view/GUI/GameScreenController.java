package it.polimi.ingsw.view.GUI;

import it.polimi.ingsw.model.card.StarterCard;
import it.polimi.ingsw.model.game.Action;
import it.polimi.ingsw.model.game.GamePhase;
import it.polimi.ingsw.network.netMessage.NetMessage;
import it.polimi.ingsw.network.netMessage.s2c.*;
import it.polimi.ingsw.view.ViewObserver;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;


import java.io.IOException;

public class GameScreenController implements ViewObserver {

    private final ViewSingleton viewSingleton = ViewSingleton.getInstance();
    private HandController handController;

    @FXML
    private Pane chats;
    @FXML
    private Pane scoreboard;
    @FXML
    private Pane goals;
    @FXML
    private Pane decks;
    @FXML
    private Pane field;
    @FXML
    private Pane hand;
    @FXML
    private Pane chooseGoal;
    @FXML
    private TextField informations;
    @FXML
    private Pane otherField;

    public void initialize() {
        viewSingleton.getView().addObserver(this);
        Platform.runLater(() -> {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/Scoreboard.fxml"));
            Pane center;
            try {
                center = loader.load();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            scoreboard.getChildren().add(center);
            ScoreboardController controller = loader.getController();
            controller.setView(viewSingleton.getView());
            controller.setScoreboard(viewSingleton.getView().getLobbies().get(viewSingleton.getView().getID()));
            controller.setOtherField(otherField);


            FXMLLoader decksLoader = new FXMLLoader(getClass().getResource("/fxml/Decks.fxml"));
            Pane decksCenter;
            try {
                decksCenter = decksLoader.load();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            decks.getChildren().add(decksCenter);
            DecksController decksController = decksLoader.getController();
            decksController.setView(viewSingleton.getView());


            FXMLLoader goalsLoader = new FXMLLoader(getClass().getResource("/fxml/Goals.fxml"));
            Pane goalsCenter;
            try {
                goalsCenter = goalsLoader.load();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            goals.getChildren().add(goalsCenter);
            GoalsController goalsController = goalsLoader.getController();
            goalsController.setView(viewSingleton.getView());

            FXMLLoader handLoader = new FXMLLoader(getClass().getResource("/fxml/Hand.fxml"));
            Pane handCenter;
            try {
                handCenter = handLoader.load();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            hand.getChildren().add(handCenter);
            this.handController = handLoader.getController();
            handController.setView(viewSingleton.getView());

            FXMLLoader chooseStarterLoader = new FXMLLoader(getClass().getResource("/fxml/ChooseStarterCard.fxml"));
            Pane chooseStarterCenter;
            try {
                chooseStarterCenter = chooseStarterLoader.load();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            field.getChildren().add(chooseStarterCenter);
            ChooseStarterCardController chooseStarterController = chooseStarterLoader.getController();
            chooseStarterController.setView(viewSingleton.getView());


            FXMLLoader chatLoader = new FXMLLoader(getClass().getResource("/fxml/Chat.fxml"));
            Pane chatCenter;
            try {
                chatCenter = chatLoader.load();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            chats.getChildren().add(chatCenter);
            ChatController chatController = chatLoader.getController();
            chatController.setChats();
            chats.setVisible(false);
        });
    }

    @Override
    public void update(NetMessage message) throws IOException {
        switch (message){
            case CardPlacedOnFieldMessage m-> {
                if (m.getCard() instanceof StarterCard) {
                    if (m.getNickname().equals(viewSingleton.getView().getNickname())) {
                        Platform.runLater(() -> {
                            FXMLLoader fieldLoader = new FXMLLoader(getClass().getResource("/fxml/Field.fxml"));
                            Pane fieldCenter;
                            try {
                                fieldCenter = fieldLoader.load();
                            } catch (IOException e) {
                                throw new RuntimeException(e);
                            }
                            field.getChildren().removeAll();
                            field.getChildren().add(fieldCenter);
                            FieldController fieldController = fieldLoader.getController();
                            fieldController.setView(viewSingleton.getView(), viewSingleton.getViewController());
                            fieldController.setHand(handController);
                        });
                    }

                }
            }
            case GameActionSwitchedMessage m -> {
                if(viewSingleton.getView().isYourTurn()&&m.getAction().equals(Action.DRAW)){
                    informations.setText("Draw a card!");
                }
            }
            case GamePhaseChangedMessage m -> {
                if(m.getGamePhase().equals(GamePhase.CHOOSING_SECRET_GOAL)) {
                    Platform.runLater(() -> {
                        FXMLLoader secretGoalLoader = new FXMLLoader(getClass().getResource("/fxml/ChooseSecretGoal.fxml"));
                        Pane secretGoalCenter;
                        try {
                            secretGoalCenter = secretGoalLoader.load();
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                        chooseGoal.getChildren().add(secretGoalCenter);
                        SecretGoalController secretGoalController = secretGoalLoader.getController();
                        secretGoalController.setView(viewSingleton.getView());
                    });
                } else if (m.getGamePhase().equals(GamePhase.PLAYING_GAME)) {
                    if(viewSingleton.getView().isYourTurn()) informations.setText("It's your turn!");
                    else informations.setText("Wait for your turn...");
                }
            }
            case SecretGoalAssignedMessage ignored -> Platform.runLater(()-> chooseGoal.setVisible(false));
            case TurnChangedMessage m -> {
                if (!viewSingleton.getView().isYourTurn()) informations.setText("Wait for your turn...");
                else informations.setText("It's your turn!");
            }
            default -> {}
        }
    }

    public void toggleChats() {
        chats.setVisible(!chats.isVisible());

    }
}
