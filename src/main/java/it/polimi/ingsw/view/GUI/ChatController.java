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
import it.polimi.ingsw.network.netMessage.s2c.LobbyLeftMessage;
import it.polimi.ingsw.view.ViewObserver;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;

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
    private Tab p1;
    @FXML
    private Tab p2;
    @FXML
    private Tab p3;
    @FXML
    private Button globalSendButton;
    @FXML
    private ListView<TextField> globalMessages;
    @FXML
    private TextField globalText;
    @FXML
    private ListView<TextField> p1Messages;
    @FXML
    private TextField p1Text;
    @FXML
    private ListView<TextField> p2Messages;
    @FXML
    private TextField p2Text;
    @FXML
    private ListView<TextField> p3Messages;
    @FXML
    private TextField p3Text;

    private final ViewSingleton viewSingleton = ViewSingleton.getInstance();
    private Lobby lobby;

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
                chats.getTabs().get(index).setText("p" + index);
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
        } catch (LobbyDoesNotExistsException | GameDoesNotExistException ignored) {}
        catch (PlayerChatMismatchException e) {
            System.out.println("Cannot send message");
        }
    }

    @Override
    public void update(NetMessage message) throws IOException {
        switch (message) {
            case LobbyJoinedMessage ignored -> setChats();
            case LobbyLeftMessage ignored -> setChats();
            case ChatMessageAddedMessage m -> Platform.runLater(() -> {
                TextField text = new TextField(m.getM().getSender().getNickname() + ": " + m.getM().getText());
                text.setEditable(false);

                if (m.getM().isGlobal()) globalMessages.getItems().add(text);
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
                                if (t.equals(p1)) p1Messages.getItems().add(text);
                                else if (t.equals(p2)) p2Messages.getItems().add(text);
                                else p3Messages.getItems().add(text);
                            }
                        }
                    }
                }
            });
            case GameCreatedMessage ignored -> viewSingleton.getView().removeObserver(this);
            default -> {}
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        viewSingleton.getView().addObserver(this);
        this.lobby = viewSingleton.getView().getLobbies().get(viewSingleton.getView().getID());
        setChats();
    }
}
