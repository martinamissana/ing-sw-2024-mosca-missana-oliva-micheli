package it.polimi.ingsw.view.GUI;

import it.polimi.ingsw.controller.exceptions.PlayerChatMismatchException;
import it.polimi.ingsw.controller.exceptions.UnexistentUserException;
import it.polimi.ingsw.model.chat.Message;
import it.polimi.ingsw.model.exceptions.GameDoesNotExistException;
import it.polimi.ingsw.model.exceptions.LobbyDoesNotExistsException;
import it.polimi.ingsw.model.game.Lobby;
import it.polimi.ingsw.model.player.Player;
import it.polimi.ingsw.network.netMessage.NetMessage;
import it.polimi.ingsw.network.netMessage.c2s.LobbyJoinedMessage;
import it.polimi.ingsw.network.netMessage.s2c.ChatMessageAddedMessage;
import it.polimi.ingsw.network.netMessage.s2c.GameCreatedMessage;
import it.polimi.ingsw.network.netMessage.s2c.GameWinnersAnnouncedMessage;
import it.polimi.ingsw.network.netMessage.s2c.LobbyLeftMessage;
import it.polimi.ingsw.view.ViewObserver;
import javafx.application.Platform;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.shape.Circle;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class ChatController implements ViewObserver, Initializable {
    @FXML
    private Button p1SendButton;
    @FXML
    private Button p2SendButton;
    @FXML
    private TabPane chats;
    @FXML
    private Tab global;
    @FXML
    private Tab p1;
    @FXML
    private Tab p2;
    @FXML
    private Tab p3;
    @FXML
    private Button globalSendButton;
    @FXML
    private Circle newGlobalMessage;
    @FXML
    private ListView<Label> globalMessages;
    @FXML
    private TextField globalText;
    @FXML
    private Circle newMessage1;
    @FXML
    private ListView<Label> p1Messages;
    @FXML
    private TextField p1Text;
    @FXML
    private Circle newMessage2;
    @FXML
    private ListView<Label> p2Messages;
    @FXML
    private TextField p2Text;
    @FXML
    private Circle newMessage3;
    @FXML
    private ListView<Label> p3Messages;
    @FXML
    private TextField p3Text;

    private final ViewSingleton viewSingleton = ViewSingleton.getInstance();
    private Lobby lobby;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        viewSingleton.getView().addObserver(this);
        this.lobby = viewSingleton.getView().getLobbies().get(viewSingleton.getView().getID());
        setChats();
    }

    public void setChats()  {
        Platform.runLater(() -> {
            int index = 1;
            for (Tab t : chats.getTabs()) t.setDisable(false);
            for (Player p : lobby.getPlayers()) {
                if (!p.equals(viewSingleton.getView().getPlayer())) {
                    chats.getTabs().get(index).setText(p.getNickname());
                    index++;
                }
            }
            while (index < chats.getTabs().size()) {
                chats.getTabs().get(index).setText("");
                if (index == 1) p1Messages.getItems().clear();
                else if (index == 2) p2Messages.getItems().clear();
                else if (index == 3) p3Messages.getItems().clear();
                chats.getTabs().get(index).setDisable(true);
                index++;
            }
        });
    }

    @FXML
    public void sendMessage(MouseEvent mouseEvent) {
        try {
            if (mouseEvent.getSource().equals(globalSendButton)) {
                if (globalText.getText().isEmpty()) return;
                viewSingleton.getView().sendMessage(new Message(globalText.getText(), viewSingleton.getView().getPlayer(), null, true));
                globalText.setText("");
            } else if (mouseEvent.getSource().equals(p1SendButton)) {
                if (p1Text.getText().isEmpty()) return;
                for (Player p : lobby.getPlayers()) {
                    if (p.getNickname().equals(p1.getText())) {
                        viewSingleton.getView().sendMessage(new Message(p1Text.getText(), viewSingleton.getView().getPlayer(), p, false));
                        p1Text.setText("");
                    }
                }
            } else if (mouseEvent.getSource().equals(p2SendButton)) {
                if (p2Text.getText().isEmpty()) return;
                for (Player p : lobby.getPlayers()) {
                    if (p.getNickname().equals(p2.getText())) {
                        viewSingleton.getView().sendMessage(new Message(p2Text.getText(), viewSingleton.getView().getPlayer(), p, false));
                        p2Text.setText("");
                    }
                }
            } else {
                if (p3Text.getText().isEmpty()) return;
                for (Player p : lobby.getPlayers()) {
                    if (p.getNickname().equals(p3.getText())) {
                        viewSingleton.getView().sendMessage(new Message(p3Text.getText(), viewSingleton.getView().getPlayer(), p, false));
                        p3Text.setText("");
                    }
                }
            }
        } catch (IOException | UnexistentUserException e) {
            throw new RuntimeException(e);
        } catch (LobbyDoesNotExistsException | GameDoesNotExistException | PlayerChatMismatchException ignored) {}
    }

    @Override
    public void update(NetMessage message) throws IOException {     // TODO: Default button + slider to 100% + Colors on tabs
        switch (message) {
            case LobbyJoinedMessage m -> {
                setChats();
                Platform.runLater(() -> globalMessages.getItems().add(new Label(m.getPlayer().getNickname() + " joined the lobby")));
            }
            case LobbyLeftMessage m -> {
                setChats();
                Platform.runLater(() -> globalMessages.getItems().add(new Label(m.getPlayer().getNickname() + " left the lobby")));
            }
            case ChatMessageAddedMessage m -> Platform.runLater(() -> {
                Label text = new Label(m.getM().getSender().getNickname() + ": " + m.getM().getText());

                if (m.getM().isGlobal()) {
                    globalMessages.getItems().add(text);
                    if (!global.isSelected()) newGlobalMessage.setVisible(true);
                }
                else {
                    if (viewSingleton.getView().getPlayer().equals(m.getM().getSender())) {
                        for (Tab t : chats.getTabs()) {
                            if (m.getM().getReceiver().getNickname().equals(t.getText())) {
                                if (t.equals(p1)) p1Messages.getItems().add(text);
                                else if (t.equals(p2)) p2Messages.getItems().add(text);
                                else p3Messages.getItems().add(text);
                            }
                        }
                    }
                    else if (viewSingleton.getView().getPlayer().equals(m.getM().getReceiver())) {
                        for (Tab t : chats.getTabs()) {
                            if (m.getM().getSender().getNickname().equals(t.getText())) {
                                if (t.equals(p1)) {
                                    p1Messages.getItems().add(text);
                                    if (!p1.isSelected()) newMessage1.setVisible(true);
                                }
                                else if (t.equals(p2)) {
                                    p2Messages.getItems().add(text);
                                    if (!p2.isSelected()) newMessage2.setVisible(true);
                                }
                                else {
                                    p3Messages.getItems().add(text);
                                    if (!p3.isSelected()) newMessage3.setVisible(true);
                                }
                            }
                        }
                    }
                }
            });
            case GameCreatedMessage ignored -> viewSingleton.getView().removeObserver(this);
            case GameWinnersAnnouncedMessage ignored -> viewSingleton.getView().removeObserver(this);
            default -> {}
        }
    }

    public void readMessage(Event event) {
        if (event.getSource().equals(global) && newGlobalMessage.isVisible()) newGlobalMessage.setVisible(false);
        if (event.getSource().equals(p1) && newMessage1.isVisible()) newMessage1.setVisible(false);
        if (event.getSource().equals(p2) && newMessage2.isVisible()) newMessage2.setVisible(false);
        if (event.getSource().equals(p3) && newMessage3.isVisible()) newMessage2.setVisible(false);
    }
}
