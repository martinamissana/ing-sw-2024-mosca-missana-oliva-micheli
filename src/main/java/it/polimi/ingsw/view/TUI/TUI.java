package it.polimi.ingsw.view.TUI;

import it.polimi.ingsw.controller.exceptions.*;
import it.polimi.ingsw.model.card.*;
import it.polimi.ingsw.model.chat.Chat;
import it.polimi.ingsw.model.chat.Message;
import it.polimi.ingsw.model.commonItem.CornerStatus;
import it.polimi.ingsw.model.commonItem.ItemBox;
import it.polimi.ingsw.model.commonItem.Kingdom;
import it.polimi.ingsw.model.commonItem.Resource;
import it.polimi.ingsw.model.deck.DeckBuffer;
import it.polimi.ingsw.model.deck.DeckBufferType;
import it.polimi.ingsw.model.deck.DeckType;
import it.polimi.ingsw.model.deck.DeckTypeBox;
import it.polimi.ingsw.model.exceptions.*;
import it.polimi.ingsw.model.game.Action;
import it.polimi.ingsw.model.game.GamePhase;
import it.polimi.ingsw.model.game.Lobby;
import it.polimi.ingsw.model.goal.DiagonalGoal;
import it.polimi.ingsw.model.goal.Goal;
import it.polimi.ingsw.model.goal.L_ShapeGoal;
import it.polimi.ingsw.model.goal.ResourceGoal;
import it.polimi.ingsw.model.player.Coords;
import it.polimi.ingsw.model.player.Hand;
import it.polimi.ingsw.model.player.Pawn;
import it.polimi.ingsw.model.player.Player;
import it.polimi.ingsw.network.RMI.ClientRemoteInterface;
import it.polimi.ingsw.network.RMI.RemoteInterface;
import it.polimi.ingsw.network.netMessage.NetMessage;
import it.polimi.ingsw.network.netMessage.c2s.DisconnectMessage;
import it.polimi.ingsw.network.netMessage.c2s.LobbyJoinedMessage;
import it.polimi.ingsw.network.netMessage.s2c.*;
import it.polimi.ingsw.view.*;

import java.io.IOException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.Semaphore;

import static java.lang.System.exit;

public class TUI implements Runnable, ViewObserver {
    private View view;
    private ViewController check;
    private final Scanner scanner = new Scanner(System.in);
    private final Semaphore semaphore = new Semaphore(0);

    private TUIState state = TUIState.LOGIN;
    private static final String warningColor = "\u001B[31m";
    private static final String reset = "\u001B[0m";
    private static final String cli = "\u001B[38;2;255;165;0m" + "\n[+] " + "\u001B[0m";
    private static final String user = "\u001B[38;2;255;165;0m" + "\n[-] " + "\u001B[0m";
    private String in;
    private static final String green = "\u001B[32m";
    private static final String blue = "\u001B[34m";
    private static final String yellow = "\u001B[93m";
    private static final String red = "\u001B[31m";
    private static final String GoldColor = "\u001B[30;43m"; // Gold

    private InputState inputState;

    // Temporary choices for card placement
    private int cardChoice;
    private int xChoice;
    private int yChoice;


    public boolean isNumeric(String s) {
        return s.matches("-?\\d+");
    }

    @Override
    public void run() {

        chooseConnectionType();

        this.check = new ViewController(view);
        this.view.addObserver(this);

        new Thread(() -> {
            try {
                try {
                    System.out.println(cli + "insert nickname" );
                    view.login(scanner.nextLine());
                    view.getCurrentStatus();

                } catch (NicknameAlreadyTakenException | FullLobbyException | IOException |
                         ClassNotFoundException ex) {
                    view.removeObserver(this);
                    Thread.currentThread().interrupt();
                }
                semaphore.acquire();

                while (true) {
                    //System.out.println(cli +state);
                    String in = scanner.nextLine();
                    switch (state) {
                        case MENU -> {
                            chooseMenuAction(in);
                            printStatus();
                        }
                        case CREATE_LOBBY -> {
                            createLobby(in);
                        }
                        case CHOOSE_PAWN -> {
                            setPawnColor(in);
                        }
                        case JOIN_LOBBY -> {
                            chooseLobby(in);
                        }
                        case LOBBY -> {
                            chooseLobbyAction(in);
                        }
                        case GAME -> {
                            switch (view.getGamePhase()) {
                                case PLACING_STARTER_CARD -> {
                                    placeStarterCard(in);
                                }
                                case CHOOSING_SECRET_GOAL -> {
                                    chooseSecretGoal(in);
                                }
                                case PLAYING_GAME -> {

                                    if (view.isYourTurn()) {
                                        switch (inputState) {
                                            case PLAY_SELECT_CARD -> {
                                                playCard_selectCard(in);
                                            }
                                            case PLAY_SELECT_SIDE -> {
                                                playCard_selectSide(in);
                                            }
                                            case PLAY_SELECT_X -> {
                                                playCard_selectX(in);
                                            }
                                            case PLAY_SELECT_Y -> {
                                                playCard_selectY(in);
                                            }
                                            case DRAW -> {
                                                drawCard(in);
                                            }
                                        }
                                    } else {
                                        chooseLobbyAction(in);
                                        //not your turn
                                    }
                                }
                            }
                        }
                    }

                }
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }


        }).start();

    }

    @Override
    public synchronized void update(NetMessage message) throws IOException {

        switch (message) {

            case LoginMessage m -> {
                semaphore.release();
            }
            case CurrentStatusMessage m -> {
                state = TUIState.MENU;
                semaphore.release();
            }

            case LobbyCreatedMessage m -> {
                if (state == TUIState.CREATE_LOBBY && m.getID().equals(view.getID())) {
                    state = TUIState.CHOOSE_PAWN;
                }
                semaphore.release();
            }
            case LobbyDeletedMessage m -> {
                return;
            }
            case PawnAssignedMessage m -> {
                if (state == TUIState.CHOOSE_PAWN && m.getPlayer().equals(view.getPlayer())) {
                    state = TUIState.LOBBY;
                    semaphore.release();
                } else if (!m.getPlayer().equals(view.getPlayer()))
                    return;
            }
            case LobbyJoinedMessage m -> {
                if (state == TUIState.CHOOSE_PAWN) return;
                if (m.getPlayer().getNickname().equals(view.getPlayer().getNickname())) {
                    state = TUIState.CHOOSE_PAWN;
                }
                semaphore.release();
            }
            case LobbyLeftMessage m -> {
                if (state == TUIState.CHOOSE_PAWN) return;
                if (m.getPlayer().getNickname().equals(view.getPlayer().getNickname())) {
                    state = TUIState.MENU;
                } else if (GamePhase.PLACING_STARTER_CARD.equals(view.getGamePhase()) || (GamePhase.CHOOSING_SECRET_GOAL.equals(view.getGamePhase()))) {
                    System.out.println(cli + "someone left the game");
                }
                semaphore.release();
            }
            case GameCreatedMessage m -> {
                state = TUIState.GAME;
                semaphore.release();
                inputState = InputState.PLAY_SELECT_CARD;
            }
            case GamePhaseChangedMessage m -> {
                if (state.equals(TUIState.GAME) && !view.getGamePhase().equals(GamePhase.PLACING_STARTER_CARD) && m.getID().equals(view.getID()))
                    semaphore.release();
            }
            case SecretGoalAssignedMessage m -> {
                System.out.println(cli + "wait for the other players to continue");
                semaphore.release();
            }
            case CardPlacedOnFieldMessage m -> {
                if (!view.isLastRound()) inputState = InputState.DRAW;
                else return; //if(!view.isLastRound())inputState = InputState.DRAW;
                semaphore.release();
            }
            case TurnChangedMessage m -> {
                if (view.isLastRound()) inputState = InputState.PLAY_SELECT_CARD;
                System.out.println(cli + "new turn");
                if(view.isLastRound())System.out.println(cli + "last round");
                semaphore.release();

            }
            case LoginFail_NicknameAlreadyTaken m -> {
                System.out.print(cli + "Nickname already taken. Exiting game");
                view.removeObserver(this);
                exit(0);
            }
            case GameWinnersAnnouncedMessage m -> {
                if (m.getID().equals(view.getID())) {
                    printScoreboard();

                    ArrayList<Player> winners = view.getWinners();
                    if (winners.size() == 1) {
                        if (m.getWinners().contains(view.getPlayer())) System.out.println(cli + "You win!!");
                        else
                            System.out.println(cli + "THE WINNER IS " + GoldColor + winners.getFirst().getNickname().toUpperCase() + reset + "!!");
                    } else {
                        System.out.print(cli + "The winners are ");
                        for (Player winner : winners) {
                            System.out.print(cli + GoldColor + winner.getNickname() + reset);
                        }
                    }
                }
            }
            case GameTerminatedMessage m -> {
                System.out.print(cli + "game terminated, returning to menu");
                state = TUIState.MENU;
                semaphore.release();
            }
            case FailMessage m -> {
                if(inputState==InputState.PLAY_SELECT_Y) inputState=InputState.PLAY_SELECT_CARD;
                if (m.getNickname().equals(view.getNickname())) System.out.println(m.getMessage());
                semaphore.release();
                return;
            }
            case DisconnectMessage m -> {
                System.out.print(cli + "The server crashed. Exiting game");
                view.removeObserver(this);
                exit(0);
            }
            default -> throw new IllegalStateException("Unexpected value: " + message);
        }
        //if (!message.getClass().equals(HeartBeatMessage.class)) System.out.println(message.toString());
        printStatus();
    }

    public void printStatus() {
        switch (state) {
            // print status: prints what the player is supposed to see
            case LOGIN -> {
            }
            case MENU -> {
                printOpenLobbies();
                System.out.print(cli + "1.create lobby" + cli +
                        "2.joinLobby" + cli +
                        "3.exit game\n");
            }
            case CREATE_LOBBY -> {
                System.out.print(cli + "insert how many players\n");
            }
            case CHOOSE_PAWN -> {
                printAvailablePawns();
                System.out.print(cli + "which pawn?\n");
            }
            case JOIN_LOBBY -> {
                printOpenLobbies();
                System.out.print(cli + "which lobby do you want to join?\n");
            }
            case LOBBY -> {
                printPlayers(view.getID());
                System.out.print(cli + "1.send message" +cli +
                        "2.leave lobby\n");
            }
            case GAME -> {
                switch (view.getGamePhase()) {
                    case PLACING_STARTER_CARD -> {
                        System.out.print(cli + "the number of player needed has been reached and the game has started: " +
                                cli + "now it's time to choose the strating card");
                        StarterCard card = (StarterCard) view.getHand().getCard(0);
                        System.out.print(cli + "FRONT SIDE:");
                        printCard(card);
                        card.flip();
                        System.out.print(cli + "BACK SIDE:");
                        printCard(card);
                        card.flip();
                    }
                    case CHOOSING_SECRET_GOAL -> {
                        System.out.print("\n" + cli + "Common goals:");
                        printGoal(view.getCommonGoal1());
                        printGoal(view.getCommonGoal2());

                        printHand();

                        ArrayList<Goal> goals = view.getSecretGoalChoices();
                        System.out.print(cli + "You can choose between these two goals:");
                        printGoal(goals.getFirst());
                        printGoal(goals.getLast());
                        System.out.println();
                    }
                    case PLAYING_GAME -> {
                        if (view.isYourTurn()) {
                            switch (inputState) {
                                case PLAY_SELECT_CARD -> {
                                    printResources();
                                    printField();
                                    printHand();
                                    System.out.print(cli + "Which card do you want to play? (0, 1, 2)\n");
                                }
                                case PLAY_SELECT_SIDE -> {
                                    System.out.print(cli + "Which side of card do you want to be visible? (front, back)\n");
                                }
                                case PLAY_SELECT_X -> {
                                    System.out.print(cli + "Select the X coordinate of the placement: ");
                                }
                                case PLAY_SELECT_Y -> {
                                    System.out.print(cli + "Select the Y coordinate of the placement: ");
                                }
                                case DRAW -> {
                                    printGameArea();
                                    System.out.println(cli + "From where do you want to draw?" + cli + "Resource Deck -> ResDeck" + cli + "Golden Deck -> GoldDeck" + cli + "Resource card spaces -> RES1 - RES2" + cli + "Golden card spaces -> GOLD1 - GOLD2" );
                                }
                            }
                        } else {
                            //not your turn
                            //print all the field and your hand and the decks
                            System.out.print(cli + "wait for your turn");
                            System.out.print(cli + "1.send message" +cli+
                                    "2.leave lobby");
                            printScoreboard();
                        }
                    }
                }
            }
            default -> {
            }
        }
    }

    private void chooseMenuAction(String in) throws InterruptedException {
        if (state != TUIState.MENU) return;
        if (!isNumeric(in)) {
            System.out.println(warningColor + "didn't insert numeric value!!" + reset);
            return;
        }

        int choice = Integer.parseInt(in);

        switch (choice) {
            case 1 -> state = TUIState.CREATE_LOBBY;
            case 2 -> {
                if (view.getLobbies().isEmpty()) {
                    System.out.println(warningColor + "\nThere are no open lobbies!\n" + reset);
                } else {
                    state = TUIState.JOIN_LOBBY;
                }
            }
            case 3 -> {
                System.out.print(cli + "Exiting game");
                view.removeObserver(this);
                exit(0);
            }
            default -> System.out.println(warningColor + "Invalid choice!!\n" + reset);
        }
    }

    private void createLobby(String in) throws InterruptedException {
        if (view.getID() != null) {
            System.out.println(warningColor + "\nCannot create a lobby while in another lobby!!" + reset);
            state = TUIState.MENU;
            return;
        }
        if (!isNumeric(in)) {
            System.out.println(warningColor + "didn't insert numeric value!!" + reset);
            return;
        }

        int numOfPlayers = Integer.parseInt(in);

        if (numOfPlayers < 2 || numOfPlayers > 4) {
            System.out.println(warningColor + "invalid number of player!" + reset);
            return;
        }

        try {
            check.checkCreateLobby(numOfPlayers);
            view.createLobby(numOfPlayers);
            semaphore.acquire();
        } catch (LobbyDoesNotExistsException | IOException | NicknameAlreadyTakenException | FullLobbyException |
                 CannotJoinMultipleLobbiesException ignored) {
        } catch (ClassNotFoundException | UnexistentUserException e) {
            view.removeObserver(this);
            Thread.currentThread().interrupt();
        }

    }


    private void setPawnColor(String color) throws InterruptedException {
        if (view.getPawn() != null) {
            System.out.println(cli + "Pawn already chosen!!");
            return;
        }

        Pawn pawn = switch (color.toUpperCase()) {
            case "RED", "R" -> Pawn.RED;
            case "GREEN", "G" -> Pawn.GREEN;
            case "BLUE", "B" -> Pawn.BLUE;
            case "YELLOW", "Y", "BANANA" -> Pawn.YELLOW;
            default -> null;
        };

        if (pawn != null) {
            try {
                view.choosePawn(pawn);
                semaphore.acquire();
            } catch (PawnAlreadyTakenException e) {
                System.out.print(cli + "Pawn already taken!\n");
            } catch (GameAlreadyStartedException | LobbyDoesNotExistsException |
                     GameDoesNotExistException ignored) {
            } catch (IOException | UnexistentUserException e) {
                view.removeObserver(this);
                Thread.currentThread().interrupt();
            }
        } else System.out.println(warningColor + "Color chosen does not exist!" + reset);

    }

    private void chooseLobby(String in) throws InterruptedException {

        if (view.getID() != null) {
            System.out.println(warningColor + "\nCannot create a lobby while in another lobby!!" + reset);
            return;
        }

        int id;


        if (!isNumeric(in)) {
            System.out.println(warningColor + "didn't insert numeric value!!" + reset);
            return;
        }
        id = Integer.parseInt(in);

        if (id == -1) {
            state = TUIState.MENU;
            printStatus();
            return;
        }

        try {
            check.checkJoinLobby(id);
            view.joinLobby(id);
            semaphore.acquire();
        } catch (NicknameAlreadyTakenException ignored) {
        } catch (CannotJoinMultipleLobbiesException e) {
            System.out.println(warningColor + "Cannot join multiple lobbies!!" + reset);
        } catch (FullLobbyException e) {
            System.out.println(warningColor + "Lobby #" + id + " is full. Cannot join!!" + reset);
        } catch (LobbyDoesNotExistsException e) {
            System.out.println(warningColor + "Lobby #" + id + " does not exist!!" + reset);
        } catch (IOException | ClassNotFoundException | UnexistentUserException e) {
            view.removeObserver(this);
            Thread.currentThread().interrupt();
        }

    }

    private void chooseLobbyAction(String in) throws InterruptedException {

        if (!isNumeric(in)) {
            System.out.println(warningColor + "didn't insert numeric value!!" + reset);
            return;
        }
        int choice = Integer.parseInt(in);
        switch (choice) {
            case 1 -> openChat();
            case 2 -> quitLobby();
        }

    }

    private void openChat() throws InterruptedException {       // TODO: fix private chats
        int desiredChat;
        Chat chats = view.getChat();

        // Taking all players currently in lobby (except the player itself)
        ArrayList<Player> players = new ArrayList<>();
        for (int i = 0; i < view.getLobbies().get(view.getID()).getPlayers().size(); i++) {
            if (!view.getLobbies().get(view.getID()).getPlayers().get(i).equals(view.getPlayer()))
                players.add(view.getLobbies().get(view.getID()).getPlayers().get(i));
        }

        System.out.print(cli + "Which chat do you want to open?" + cli + "1. Global chat");
        for (int i = 0; i < players.size(); i++) {
            System.out.print(cli + (i + 2) + ". Private chat with " + players.get(i).getNickname());
        }

        // Selecting chat (if error return to waiting()):
        in = scanner.nextLine();

        if (isNumeric(in)) desiredChat = Integer.parseInt(scanner.nextLine());
        else {
            System.out.println(warningColor + "didn't insert numeric value!!" + reset);
            return;
        }

        System.out.println(cli + "Opening chat (write \"quit chat\" to return to selection)");
        if (desiredChat == 1) {
            for (Message m : chats.getGlobalChat()) {
                System.out.print("\u001B[38;2;255;165;0m\n[" + m.getSender().getNickname() + "]:" + reset + " \"" + m.getText() + "\"");
            }
            sendMessage(null);
        } else if (desiredChat > 1 && desiredChat < view.getLobbies().get(view.getID()).getPlayers().size() + 2) {
            for (Message m : chats.getPrivateChat(players.get(desiredChat - 2))) {
                System.out.print("\n\u001B[38;2;255;165;0m[" + m.getSender().getNickname() + "]:" + reset + " \"" + m.getText() + "\"");
            }
            sendMessage(players.get(desiredChat - 2));
        } else System.out.println(warningColor + "Invalid choice!!\n" + reset);
    }

    private void sendMessage(Player player) throws InterruptedException {
        String text = scanner.nextLine();
        if (text.equalsIgnoreCase("quit chat")) return;
        Message msg;

        try {
            if (player == null) {
                msg = new Message(text, view.getPlayer(), null, true);
                view.sendMessage(msg);
            } else {
                msg = new Message(text, view.getPlayer(), player, false);
                view.sendMessage(msg);
            }
        } catch (LobbyDoesNotExistsException | GameDoesNotExistException ignored) {
        } catch (IOException | UnexistentUserException | PlayerChatMismatchException e) {
            view.removeObserver(this);
            Thread.currentThread().interrupt();
        }
    }

    private void quitLobby() throws InterruptedException {

        try {
            check.checkLeaveLobby();
        } catch (NotConnectedToLobbyException e) {
            System.out.println(warningColor + "Non connected to a lobby!!" + reset);
        }

        try {
            view.leaveLobby();
            semaphore.acquire();

        } catch (GameAlreadyStartedException e) {
            System.out.print(cli + "The game is already started! Cannot leave the lobby!");
        } catch (LobbyDoesNotExistsException | NicknameAlreadyTakenException | GameDoesNotExistException ignored) {
        } catch (IOException | FullLobbyException | ClassNotFoundException | UnexistentUserException e) {
            view.removeObserver(this);
            Thread.currentThread().interrupt();
        }
    }

    protected void placeStarterCard(String choice) throws InterruptedException {
        if (view.getHand().getSize() == 0 || !view.getHand().getCard(0).getClass().equals(StarterCard.class)) return;
        StarterCard card = (StarterCard) view.getHand().getCard(0);


        CardSide side = switch (choice.toUpperCase()) {
            case "FRONT", "F" -> side = CardSide.FRONT;
            case "BACK", "B" -> side = CardSide.BACK;
            default -> side = null;
        };
        if (side == null) {
            System.out.println(warningColor + "Invalid choice!!\n" + reset);
            return;
        }

        if (side.equals(CardSide.BACK)) {
            card.flip();
        }

        try {
            view.chooseCardSide(side);
            System.out.print(cli + "card placed, wait for the others");
            semaphore.acquire();
        } catch (IOException | GameDoesNotExistException | EmptyDeckException | HandIsFullException |
                 UnexistentUserException | WrongGamePhaseException e) {
            System.out.print(e.getClass().getName());
        }

    }

    protected void chooseSecretGoal(String choice) throws InterruptedException {
        if (view.getSecretGoal() != null) return;

        if (!isNumeric(choice)) {
            System.out.println(warningColor + "didn't insert numeric value!!" + reset);
        }
        int id = Integer.parseInt(choice);

        try {
            view.chooseSecretGoal(id);
            System.out.print(cli + "goal chosen");
        } catch (IOException | UnexistentUserException e) {
            throw new RuntimeException(e);
        } catch (WrongGamePhaseException | GameDoesNotExistException ignored) {
        } catch (IllegalGoalChosenException e) {
            System.out.print(warningColor + "Invalid choice!!\n" + reset);
        }
    }

    private void playCard_selectCard(String in) {
        if (!isNumeric(in)) {
            System.out.println(warningColor + "didn't insert numeric value!!" + reset);
            return;
        }
        cardChoice = Integer.parseInt(in);

        if (cardChoice < 0 || cardChoice >= view.getHand().getSize()) {
            System.out.println(warningColor + "Invalid choice!!\n" + reset);
        } else {
            inputState = InputState.PLAY_SELECT_SIDE;
            printStatus();
        }
    }

    private void playCard_selectSide(String in) {
        String choice = in.toUpperCase();

        if (!List.of("B", "BACK", "F", "FRONT").contains(choice)) {
            System.out.print(warningColor + "Invalid choice!!\n" + reset);
            return;
        }

        boolean flip = switch (choice) {
            case "B", "BACK" -> true;
            default -> false;
        };

        if (flip) {
            for (int i = 0; i < view.getHand().getSize(); i++) {
                view.getHand().getCard(i).flip();
            }
            printHand();
        }
        inputState = InputState.PLAY_SELECT_X;
        printStatus();
    }

    private void playCard_selectX(String in) {
        if (!isNumeric(in)) {
            System.out.println(warningColor + "didn't insert numeric value!!" + reset);
            return;
        }
        xChoice = Integer.parseInt(in);
        inputState = InputState.PLAY_SELECT_Y;
        printStatus();
    }

    private void playCard_selectY(String in) {
        if (!isNumeric(in)) {
            System.out.println(warningColor + "didn't insert numeric value!!" + reset);
            return;
        }
        yChoice = Integer.parseInt(in);
        printStatus();

        Coords coords = new Coords(xChoice, yChoice);

        try {
            check.checkPlayCard(cardChoice, coords);
        } catch (NotYourTurnException | IllegalActionException e) {
            System.out.println(e.getClass().getName());
            return;
        } catch (IllegalCoordsException e) {
            if (e.getClass().equals(OccupiedCoordsException.class)) {
                System.out.format(cli + "Coords (%d, %d) already occupied", xChoice, yChoice);
            } else if (e.getClass().equals(UnreachablePositionException.class))
                System.out.println(warningColor + "Invalid spot!!");
            inputState = InputState.PLAY_SELECT_CARD;
            printStatus();
            return;
        } catch (RequirementsNotSatisfiedException e) {
            System.out.println(warningColor + "You don't have enough resources to play this card!!");
            inputState = InputState.PLAY_SELECT_CARD;
            printStatus();
            return;
        }

        try {
            view.playCard(cardChoice, coords);
            semaphore.acquire();
            for (int i = 0; i < view.getHand().getSize(); i++) {
                if (view.getHand().getCard(i).getSide().equals(CardSide.BACK)) view.getHand().getCard(i).flip();
            }
        } catch (IllegalActionException | NotYourTurnException | LobbyDoesNotExistsException |
                 GameDoesNotExistException e) {
            System.out.println(e.getClass().getName());
        } catch (OccupiedCoordsException e) {
            System.out.println(warningColor + "Spot already taken by:" + reset);
            inputState = InputState.PLAY_SELECT_CARD;
        } catch (IllegalMoveException e) {
            System.out.println(warningColor + "Illegal placement. Cannot play card in this spot!!" + reset);
            inputState = InputState.PLAY_SELECT_CARD;
        } catch (UnexistentUserException | IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }

    }


    protected void drawCard(String choice) throws InterruptedException {
        if (!view.isYourTurn()) {
            System.out.println(warningColor + "Cannot draw card because it's not your turn!!" + reset);
            return;
        }
        if (!view.getAction().equals(Action.DRAW)) {
            System.out.println(warningColor + "Cannot draw card because you have to play one first!!" + reset);
            return;
        }

        DeckTypeBox type = switch (choice.toUpperCase()) {
            case "RD", "RESDECK", "RESOURCE DECK" -> DeckType.RESOURCE;
            case "GD", "GOLDDECK", "GOLDEN DECK" -> DeckType.GOLDEN;
            case "R1", "RES1" -> DeckBufferType.RES1;
            case "R2", "RES2" -> DeckBufferType.RES2;
            case "G1", "GOLD1" -> DeckBufferType.GOLD1;
            case "G2", "GOLD2" -> DeckBufferType.GOLD2;
            default -> null;
        };

        if (type == null) {
            System.out.print(warningColor + "Invalid choice!!\n" + reset);
            return;
        }

        try {
            view.drawCard(type);
            semaphore.acquire();
            inputState = InputState.PLAY_SELECT_CARD;
        } catch (IllegalActionException | HandIsFullException | EmptyBufferException | NotYourTurnException |
                 LobbyDoesNotExistsException | GameDoesNotExistException ignored) {
        } catch (EmptyDeckException e) {
            System.out.println(warningColor + "Cannot draw from this deck. Reason: empty!!" + reset);
        } catch (IOException | UnexistentUserException e) {
            throw new RuntimeException(e);
        }
    }


    private void chooseConnectionType() {
        System.out.println(cli + "Insert your connection type: [TCP|RMI]" );
        String choice = scanner.nextLine();

        if (choice.equalsIgnoreCase("TCP")) {
            try {
                this.view = new TCPView("127.0.0.1", 4321);
            } catch (IOException ex) {
                if (view != null) view.removeObserver(this);
                Thread.currentThread().interrupt();
            }
            new Thread(() -> {
                try {
                    ((TCPView) view).startClient();
                } catch (IOException | ClassNotFoundException | NullPointerException e) {
                    if (view != null) view.removeObserver(this);
                    Thread.currentThread().interrupt();
                }
            }).start();

        } else if (choice.equalsIgnoreCase("RMI")) {
            Registry registry;
            try {
                registry = LocateRegistry.getRegistry();

                String remoteObjectName = "RMIServer";
                RemoteInterface RMIServer;

                RMIServer = (RemoteInterface) registry.lookup(remoteObjectName);
                this.view = new RMIView(RMIServer);

                RMIServer.connect((ClientRemoteInterface) UnicastRemoteObject.exportObject((ClientRemoteInterface) this.view, 0));

            } catch (RemoteException | NotBoundException ex) {
                view.removeObserver(this);
                exit(1);
            }
        } else exit(1);
    }


    private void printOpenLobbies() {
        HashMap<Integer, Lobby> lobbies = view.getLobbies();
        if (lobbies.isEmpty()) return;
        System.out.print(cli + "Open lobbies:");
        for (Integer i : lobbies.keySet()) {
            if (lobbies.get(i).getPlayers().size() != lobbies.get(i).getNumOfPlayers() && !lobbies.get(i).getPlayers().isEmpty()) {
                System.out.print(cli + "-> Lobby " + green + "#" + i + reset + ": " + lobbies.get(i).getPlayers().size() + "/" + lobbies.get(i).getNumOfPlayers() + " - [");

                for (Player p : lobbies.get(i).getPlayers()) {
                    if (p.equals(lobbies.get(i).getPlayers().getLast())) {
                        System.out.print(p.getNickname() + "]");
                    } else {
                        System.out.print(p.getNickname() + " - ");
                    }
                }
            } else if (!lobbies.get(i).getPlayers().isEmpty())
                System.out.print(cli + "-> Lobby " + warningColor + "#" + i + reset + ": FULL");
        }

    }

    private void printPlayers(Integer ID) {     // TODO: Resolve problem with prints
        Lobby lobby = view.getLobbies().get(ID);

        System.out.print(cli + "Players (lobby " + green + "#" + ID + reset + "): " + lobby.getPlayers().size() + "/" + lobby.getNumOfPlayers());
        for (Player p : lobby.getPlayers()) {
            switch (p.getPawn()) {
                case Pawn.BLUE -> System.out.print(cli + blue + "● ");
                case Pawn.RED -> System.out.print(cli + red + "● ");
                case Pawn.GREEN -> System.out.print(cli + green + "● ");
                case Pawn.YELLOW -> System.out.print(cli + yellow + "● ");
                case null -> System.out.print(cli + "● ");
            }
            System.out.print(p.getNickname() + reset);
        }

    }

    private void printAvailablePawns() {
        ArrayList<Pawn> pawns = new ArrayList<>();
        for (Player p : view.getLobbies().get(view.getID()).getPlayers()) {
            if (p.getPawn() != null) pawns.add(p.getPawn());
        }

        System.out.print(cli + "Available pawns: ");
        for (Pawn p : Pawn.values()) {
            if (!pawns.contains(p)) {
                switch (p) {
                    case Pawn.BLUE -> System.out.print(blue + "● " + reset);
                    case Pawn.RED -> System.out.print(red + "● " + reset);
                    case Pawn.GREEN -> System.out.print(green + "● " + reset);
                    case Pawn.YELLOW -> System.out.print(yellow + "● " + reset);
                }
            }
        }
    }


    private static final String FungiColor = "\u001B[30;41m"; // Red
    private static final String PlantColor = "\u001B[30;42m"; // Green
    private static final String InsectColor = "\u001B[30;45m"; // Purple
    private static final String AnimalColor = "\u001B[30;46m"; // Cyan
    private static final String StarterColor = "\u001B[30;48;2;245;200;157m"; // Skin
    private static final String cornerColor = "\u001B[30;48;2;200;150;100m"; // Darker skin

    private static final String CardBlockColor = "\u001B[40m";   // Black


    private String ItemsToColor(ItemBox item) {
        return switch (item) {
            case Kingdom.ANIMAL -> AnimalColor;
            case Kingdom.FUNGI -> FungiColor;
            case Kingdom.INSECT -> InsectColor;
            case Kingdom.PLANT -> PlantColor;
            case Resource.INKWELL, Resource.MANUSCRIPT, Resource.QUILL -> GoldColor;
            case CornerStatus.EMPTY -> cornerColor;
            case null -> StarterColor;
            default -> throw new IllegalStateException("Unexpected value: " + item);
        };
    }

    public String ItemToString(Card card, CornerType type) {
        ItemBox item = card.getCorner(type);

        if (item == null) return ItemsToColor(card.getKingdom()) + "   " + reset;
        if (item.equals(Resource.INKWELL)) return GoldColor + " I " + reset;
        if (item.equals(Resource.MANUSCRIPT)) return GoldColor + " M " + reset;
        if (item.equals(Resource.QUILL)) return GoldColor + " Q " + reset;
        if (item.equals(CornerStatus.EMPTY)) return cornerColor + "   " + reset;

        if (card.getClass().equals(StarterCard.class)) return switch (item) {
            case Kingdom.FUNGI -> ItemsToColor(item) + " F ";
            case Kingdom.PLANT -> ItemsToColor(item) + " P ";
            case Kingdom.ANIMAL -> ItemsToColor(item) + " A ";
            case Kingdom.INSECT -> ItemsToColor(item) + " I ";
            default -> ItemsToColor(card.getKingdom()) + "   ";
        } + reset;

        return switch (item) {
            case Kingdom.FUNGI -> cornerColor + " F ";
            case Kingdom.PLANT -> cornerColor + " P ";
            case Kingdom.ANIMAL -> cornerColor + " A ";
            case Kingdom.INSECT -> cornerColor + " I ";
            default -> ItemsToColor(card.getKingdom()) + "   ";
        } + reset;
    }

    private void printGoal(Goal goal) {
        if (goal.getClass().equals(L_ShapeGoal.class)) {
            System.out.print(cli + "L-Shape Goal " + goal.getGoalID() + ": " + ((L_ShapeGoal) goal).getType() + " with two " + ((L_ShapeGoal) goal).getMainColor() + " and " + ((L_ShapeGoal) goal).getSecondaryColor() + ". Points = " + goal.getPoints());
        } else if (goal.getClass().equals(DiagonalGoal.class)) {
            System.out.print(cli + "Diagonal Goal " + goal.getGoalID() + ": " + ((DiagonalGoal) goal).getType() + " of " + ((DiagonalGoal) goal).getColor() + ". Points = " + goal.getPoints());
        } else {
            System.out.print(cli + "Resource Goal " + goal.getGoalID() + ": " + goal.getPoints() + " points if you have:");
            for (ItemBox item : ((ResourceGoal) goal).getResourceList()) {
                System.out.print(" " + item);
            }
        }
    }

    private String printUpper(Card card) {
        if (card.getClass().equals(CardBlock.class)) return CardBlockColor + "             " + reset;

        String kingdom = ItemsToColor(card.getKingdom());
        String north = ItemToString(card, CornerType.NORTH);
        String east = ItemToString(card, CornerType.EAST);
        String points = kingdom + "       " + reset;

        if (card.getSide().equals(CardSide.FRONT)) {
            int directPoints;
            if (card.getClass().equals(StarterCard.class)) {
                directPoints = 0;
            } else directPoints = ((ResourceCard) card).getPoints();

            // Setting string of points for cards:
            if (directPoints != 0) {
                if (card.getClass().equals(GoldenCard.class)) {
                    if (((GoldenCard) card).getType() == GoldenCardType.CORNER)
                        points = GoldColor + directPoints + "xC" + reset;
                    else if (((GoldenCard) card).getType() == GoldenCardType.RESOURCE) {
                        points = GoldColor + directPoints + "x" + switch (((GoldenCard) card).getPointResource()) {
                            case INKWELL -> "I";
                            case MANUSCRIPT -> "M";
                            case QUILL -> "Q";
                        };
                    } else points = GoldColor + " " + directPoints + " " + reset;
                } else points = GoldColor + " " + directPoints + " " + reset;
                return north + kingdom + "  " + reset + points + kingdom + "  " + reset + east;
            }
        }

        return north + points + east;
    }

    private String printMiddle(Card card) {
        if (card.getClass().equals(CardBlock.class)) return CardBlockColor + "             " + reset;

        if (card.getClass().equals(StarterCard.class)) {
            if (card.getSide().equals(CardSide.FRONT)) return StarterColor + "             " + reset;

            // Setting string of permanent resources
            StringBuilder permRes = new StringBuilder();
            for (Kingdom k : ((StarterCard) card).getPermanentRes().keySet()) {
                switch (k) {
                    case ANIMAL ->
                            permRes.append("A ".repeat(Math.max(0, ((StarterCard) card).getPermanentRes().get(k))));
                    case FUNGI ->
                            permRes.append("F ".repeat(Math.max(0, ((StarterCard) card).getPermanentRes().get(k))));
                    case INSECT ->
                            permRes.append("I ".repeat(Math.max(0, ((StarterCard) card).getPermanentRes().get(k))));
                    case PLANT ->
                            permRes.append("P ".repeat(Math.max(0, ((StarterCard) card).getPermanentRes().get(k))));
                }
            }

            return switch (permRes.length()) {
                case 2 -> StarterColor + "      " + permRes + "     " + reset;
                case 4 -> StarterColor + "     " + permRes + "    " + reset;
                case 6 -> StarterColor + "    " + permRes + "   " + reset;
                default -> throw new IllegalStateException("Unexpected value: " + permRes.length());
            };
        }

        String kingdom;
        if (card.getSide().equals(CardSide.BACK)) {
            kingdom = switch (ItemsToColor(card.getKingdom())) {
                case AnimalColor -> " A ";
                case FungiColor -> " F ";
                case InsectColor -> " I ";
                case PlantColor -> " P ";
                default -> throw new IllegalStateException("Unexpected value: " + ItemsToColor(card.getKingdom()));
            };

            if (card.getClass().equals(GoldenCard.class))
                return ItemsToColor(card.getKingdom()) + "     " + reset + GoldColor + kingdom + reset + ItemsToColor(card.getKingdom()) + "     " + reset;
        } else kingdom = "   ";

        return ItemsToColor(card.getKingdom()) + "     " + kingdom + "     " + reset;
    }

    private String printLower(Card card) {
        if (card.getClass().equals(CardBlock.class)) return CardBlockColor + "             " + reset;

        String kingdom = ItemsToColor(card.getKingdom());

        String south = ItemToString(card, CornerType.SOUTH);
        String west = ItemToString(card, CornerType.WEST);

        if (card.getClass().equals(GoldenCard.class) && card.getSide().equals(CardSide.FRONT)) {
            StringBuilder requirements = new StringBuilder();
            for (Kingdom k : ((GoldenCard) card).getRequirements().keySet()) {
                switch (k) {
                    case ANIMAL ->
                            requirements.append("A".repeat(Math.max(0, ((GoldenCard) card).getRequirements().get(k))));
                    case FUNGI ->
                            requirements.append("F".repeat(Math.max(0, ((GoldenCard) card).getRequirements().get(k))));
                    case INSECT ->
                            requirements.append("I".repeat(Math.max(0, ((GoldenCard) card).getRequirements().get(k))));
                    case PLANT ->
                            requirements.append("P".repeat(Math.max(0, ((GoldenCard) card).getRequirements().get(k))));
                }
            }

            return west + kingdom + switch (requirements.toString().length()) {
                case 1 -> "   " + reset + StarterColor + requirements + reset + kingdom + "   ";
                case 2 -> "  " + reset + StarterColor + requirements + reset + kingdom + "   ";
                case 3 -> "  " + reset + StarterColor + requirements + reset + kingdom + "  ";
                case 4 -> " " + reset + StarterColor + requirements + reset + kingdom + "  ";
                case 5 -> " " + reset + StarterColor + requirements + reset + kingdom + " ";
                default -> throw new IllegalStateException("Unexpected value: " + requirements.toString().length());
            } + reset + south;
        }
        return west + kingdom + "       " + reset + south;
    }

    public void printCard(Card card) {
        System.out.println(cli + printUpper(card) + cli + printMiddle(card) + cli + printLower(card) + "\n");
    }

    protected synchronized void printHand() {
        Hand hand = view.getHand();

        System.out.print(cli);
        for (int i = 0; i < hand.getSize(); i++)
            System.out.print("\t\t" + printUpper(hand.getCard(i)));

        System.out.print(cli);
        for (int i = 0; i < hand.getSize(); i++)
            System.out.print("\t\t" + printMiddle(hand.getCard(i)));

        System.out.print(cli);
        for (int i = 0; i < hand.getSize(); i++)
            System.out.print("\t" + i + ":\t" + printLower(hand.getCard(i)));


    }

    private void printGameArea() {
        Card next = view.getTopResourceCard();
        HashMap<DeckBufferType, DeckBuffer> cardSpaces = view.getDeckBuffers();

        // Printing resource deck + spaces
        if (next != null) next.flip();

        System.out.print(cli + "Resource Deck + card spaces:" + cli);
        if (next != null) System.out.print(printUpper(next) + "\t\t");
        else System.out.print("\t\t\t\t\t");
        if (cardSpaces.get(DeckBufferType.RES1).getCard() != null)
            System.out.print(printUpper(cardSpaces.get(DeckBufferType.RES1).getCard()) + "\t");
        else System.out.print("\t\t\t\t");
        if (cardSpaces.get(DeckBufferType.RES2).getCard() != null)
            System.out.print(printUpper(cardSpaces.get(DeckBufferType.RES2).getCard()) + cli);
        else System.out.print("\t\t\t\t");

        if (next != null) System.out.print(printMiddle(next) + "\t\t");
        else System.out.print("\t\t\t\t\t");
        if (cardSpaces.get(DeckBufferType.RES1).getCard() != null)
            System.out.print(printMiddle(cardSpaces.get(DeckBufferType.RES1).getCard()) + "\t");
        else System.out.print("\t\t\t\t");
        if (cardSpaces.get(DeckBufferType.RES2).getCard() != null)
            System.out.print(printMiddle(cardSpaces.get(DeckBufferType.RES2).getCard()) + cli);
        else System.out.print("\t\t\t\t");

        if (next != null) System.out.print(printLower(next) + "\t\t");
        else System.out.print("\t\t\t\t\t");
        if (cardSpaces.get(DeckBufferType.RES1).getCard() != null)
            System.out.print(printLower(cardSpaces.get(DeckBufferType.RES1).getCard()) + "\t");
        else System.out.print("\t\t\t\t");
        if (cardSpaces.get(DeckBufferType.RES2).getCard() != null)
            System.out.print(printLower(cardSpaces.get(DeckBufferType.RES2).getCard()) + cli);
        else System.out.print("\t\t\t\t");

        if (next != null) next.flip();

        // Printing golden deck + spaces
        next = view.getTopGoldenCard();
        if (next != null) next.flip();

        System.out.print(cli + "Golden Deck + card spaces:" + cli);
        if (next != null) System.out.print(printUpper(next) + "\t\t");
        else System.out.print("\t\t\t\t\t");
        if (cardSpaces.get(DeckBufferType.GOLD1).getCard() != null)
            System.out.print(printUpper(cardSpaces.get(DeckBufferType.GOLD1).getCard()) + "\t");
        else System.out.print("\t\t\t\t");
        if (cardSpaces.get(DeckBufferType.GOLD2).getCard() != null)
            System.out.print(printUpper(cardSpaces.get(DeckBufferType.GOLD2).getCard()) + cli);
        else System.out.print("\t\t\t\t");

        if (next != null) System.out.print(printMiddle(next) + "\t\t");
        else System.out.print("\t\t\t\t\t");
        if (cardSpaces.get(DeckBufferType.GOLD1).getCard() != null)
            System.out.print(printMiddle(cardSpaces.get(DeckBufferType.GOLD1).getCard()) + "\t");
        else System.out.print("\t\t\t\t");
        if (cardSpaces.get(DeckBufferType.GOLD2).getCard() != null)
            System.out.print(printMiddle(cardSpaces.get(DeckBufferType.GOLD2).getCard()) + cli);
        else System.out.print("\t\t\t\t");

        if (next != null) System.out.print(printLower(next) + "\t\t");
        else System.out.print("\t\t\t\t\t");
        if (cardSpaces.get(DeckBufferType.GOLD1).getCard() != null)
            System.out.print(printLower(cardSpaces.get(DeckBufferType.GOLD1).getCard()) + "\t");
        else System.out.print("\t\t\t\t");
        if (cardSpaces.get(DeckBufferType.GOLD2).getCard() != null)
            System.out.print(printLower(cardSpaces.get(DeckBufferType.GOLD2).getCard()) + cli);
        else System.out.print("\t\t\t\t");

        if (next != null) next.flip();
        System.out.print("\n" + cli + "Common goals:");
        printGoal(view.getCommonGoal1());
        printGoal(view.getCommonGoal2());

        printGoal(view.getSecretGoal());

    }

    private void printField() {         // TODO: Do a better printField
        System.out.print(cli + "Occupied spots:");
        for (Coords coords : view.getMyField().getMatrix().keySet()) {
            System.out.format(cli + "(%d, %d)\n", coords.getX(), coords.getY());
            printCard(view.getMyField().getMatrix().get(coords));
        }

    }

    private void printResources() {
        System.out.print(cli + "Your total resources:");
        for (ItemBox item : view.getMyField().getTotalResources().keySet()) {
            System.out.print(cli + " - " + item + ": " + view.getMyField().getTotalResources().get(item));
        }

    }

    protected void printScoreboard() {
        HashMap<Player, Integer> scoreboard = view.getScoreboard();
        for (Player p : scoreboard.keySet()) {
            System.out.print(cli + "[" + p.getNickname() + " - " + scoreboard.get(p) + "]");
        }
    }

}