package it.polimi.ingsw.view.TUI;

import it.polimi.ingsw.controller.exceptions.*;
import it.polimi.ingsw.model.card.*;
import it.polimi.ingsw.model.chat.Chat;
import it.polimi.ingsw.model.chat.Message;
import it.polimi.ingsw.model.deck.DeckBufferType;
import it.polimi.ingsw.model.deck.DeckType;
import it.polimi.ingsw.model.deck.DeckTypeBox;
import it.polimi.ingsw.model.exceptions.*;
import it.polimi.ingsw.model.game.Action;
import it.polimi.ingsw.model.game.GamePhase;
import it.polimi.ingsw.model.goal.Goal;
import it.polimi.ingsw.model.player.Coords;
import it.polimi.ingsw.model.player.Pawn;
import it.polimi.ingsw.model.player.Player;
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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.Semaphore;

import static java.lang.System.exit;

public class TUI implements Runnable, ViewObserver {
    private View view;
    private ViewController check;
    private final Scanner scanner = new Scanner(System.in);
    private final Semaphore semaphore = new Semaphore(0);
    private Printer printer;

    private TUIState state;
    private static final String cli = Color.console + "\n[+] " + Color.reset;
    private static final String user = Color.console + "\n[-] " + Color.reset;
    private String in;

    private InputState inputState;
    private int cardChoice;


    public boolean isNumeric(String s) {
        return s.matches("-?\\d+");
    }

    @Override
    public void run() {
        // printer.printLogo();
        chooseConnectionType();

        this.check = new ViewController(view);
        this.printer = new Printer(view);
        this.view.addObserver(this);

        new Thread(() -> {
            try {
                System.out.print(cli + "Insert nickname: " + user);
                view.login(scanner.nextLine());
                view.getCurrentStatus();
                semaphore.acquire();

                while (true) {
                    //System.out.println(cli + state);
                    in = scanner.nextLine();
                    switch (state) {
                        case MENU -> {
                            chooseMenuAction(in);
                            printStatus();
                        }
                        case CREATE_LOBBY -> createLobby(in);
                        case CHOOSE_PAWN -> setPawnColor(in);
                        case JOIN_LOBBY -> chooseLobby(in);
                        case LOBBY -> chooseInLobbyAction(in);

                        case GAME -> {
                            switch (view.getGamePhase()) {
                                case PLACING_STARTER_CARD -> placeStarterCard(in);
                                case CHOOSING_SECRET_GOAL -> chooseSecretGoal(in);

                                case PLAYING_GAME -> {
                                    if (view.isYourTurn()) {
                                        if (inputState.equals(InputState.DRAW)) drawCard(in);
                                        else playCard(in);
                                    } else {
                                        chooseInGameAction(in);
                                        // not your turn
                                    }
                                }
                            }
                        }
                    }
                }
            } catch (InterruptedException | IOException | ClassNotFoundException e) {
                throw new RuntimeException(e);
            } catch (FullLobbyException | NicknameAlreadyTakenException ignored) {}
        }).start();

    }

    @Override
    public synchronized void update(NetMessage message) throws IOException {
        switch (message) {

            // Login messages ·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~
            case LoginMessage m -> {
                System.out.println(cli + "Successfully logged in. Hello " + m.getNickname() + "!!");
                semaphore.release();
            }

            case LoginFail_NicknameAlreadyTaken ignored -> {
                System.out.print(cli + "Nickname already taken. Exiting game");
                view.removeObserver(this);
                exit(0);
            }

            case DisconnectMessage ignored -> {
                System.out.print(cli + "The server crashed. Exiting game");
                view.removeObserver(this);
                exit(0);
            }

            case CurrentStatusMessage ignored -> {
                state = TUIState.MENU;
                semaphore.release();
                printStatus();
            }

            // Lobby messages ·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~
            case LobbyCreatedMessage m -> {
                if (state == TUIState.CREATE_LOBBY && m.getID().equals(view.getID())) {
                    System.out.println(cli + "Successfully created lobby. ID number: #" + m.getID());
                    state = TUIState.CHOOSE_PAWN;
                    printStatus();
                }
                semaphore.release();
            }

            case LobbyJoinedMessage m -> {
                if (state == TUIState.CHOOSE_PAWN) return;
                if (m.getPlayer().equals(view.getPlayer())) {
                    System.out.println(cli + "Joined lobby #" + m.getID());
                    state = TUIState.CHOOSE_PAWN;
                    printStatus();
                }
                semaphore.release();
            }

            case LobbyLeftMessage m -> {
                if (state == TUIState.CHOOSE_PAWN) return;
                if (m.getPlayer().equals(view.getPlayer())) {
                    System.out.println(cli + "Left lobby #" + m.getID());
                    state = TUIState.MENU;

                } else if (GamePhase.PLACING_STARTER_CARD.equals(view.getGamePhase()) ||
                        (GamePhase.CHOOSING_SECRET_GOAL.equals(view.getGamePhase()))) {

                    System.out.println(cli + m.getPlayer().getNickname() + " left the game");
                }
                printStatus();
                semaphore.release();
            }

            case LobbyDeletedMessage ignored -> {}

            case PawnAssignedMessage m -> {
                if (state == TUIState.CHOOSE_PAWN && m.getPlayer().equals(view.getPlayer())) {
                    state = TUIState.LOBBY;
                    semaphore.release();
                }
                printStatus();
            }

            // Game messages ·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·
            case GameCreatedMessage ignored -> {
                System.out.println(cli + "The game is starting...");
                state = TUIState.GAME;
                semaphore.release();
                inputState = InputState.PLAY_SELECT_SIDE;
                printStatus();
            }

            case GamePhaseChangedMessage m -> {
                if (state.equals(TUIState.GAME)  && m.getID().equals(view.getID()))
                    semaphore.release();
                printStatus();
            }

            case SecretGoalAssignedMessage ignored -> {
                System.out.println(cli + "Waiting for other players to continue");
                semaphore.release();
            }

            case CardPlacedOnFieldMessage m -> {
                System.out.println(cli + "Card placed successfully on coords (" + m.getCoords().getX() + ", " + m.getCoords().getY() + ")");
                if (!view.isLastRound()) inputState = InputState.DRAW;
                else return;
                printStatus();
                semaphore.release();
            }

            case TurnChangedMessage ignored -> {
                if (view.isLastRound()) inputState = InputState.PLAY_SELECT_SIDE;
                // System.out.println(cli + "new turn");
                if (view.isLastRound()) System.out.println(cli + "Last round started");
                semaphore.release();
                printStatus();
            }

            case GameWinnersAnnouncedMessage m -> {
                if (m.getID().equals(view.getID())) {
                    printer.printScoreboard();

                    ArrayList<Player> winners = view.getWinners();
                    if (winners.size() == 1) {
                        if (m.getWinners().contains(view.getPlayer())) System.out.println(cli + "You win!!");
                        else
                            System.out.println(cli + "THE WINNER IS " + Color.gold + winners.getFirst().getNickname().toUpperCase() + Color.reset + "!!");
                    } else {
                        System.out.print(cli + "The winners are ");
                        for (Player winner : winners) {
                            System.out.print(cli + Color.gold + winner.getNickname() + Color.reset);
                        }
                    }
                }
            }

            case GameTerminatedMessage ignored -> {
                System.out.print(cli + "Game terminated, returning to menu...");
                state = TUIState.MENU;
                semaphore.release();
                printStatus();
            }

            // Fail messages ·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·
            case FailMessage m -> {
                if (inputState==InputState.PLAY_SELECT_COORDS) inputState=InputState.PLAY_SELECT_SIDE;
                if (m.getNickname().equals(view.getNickname())) System.out.println(m.getMessage());
                semaphore.release();
            }

            default -> throw new IllegalStateException("Unexpected value: " + message);
        }
        //if (!message.getClass().equals(HeartBeatMessage.class)) System.out.println(message.toString());

    }

    public void printStatus() {
        switch (state) {
            // print status: prints what the player is supposed to see
            case MENU -> System.out.print(cli + "1. Create a new lobby"
                                        + cli + "2. Join an open lobby"
                                        + cli + "3. Exit game" + user);

            case CREATE_LOBBY -> System.out.print(cli + "How many players?" + user);
            case JOIN_LOBBY -> {
                printer.printOpenLobbies();
                System.out.print(cli + "Which lobby do you want to join?" + user);
            }

            case CHOOSE_PAWN -> {
                printer.printAvailablePawns();
                System.out.print(cli + "Choose your pawn from the list" + user);
            }

            case LOBBY -> {
                printer.printPlayers(view.getID());
                System.out.print(cli + "1. Send message" + cli + "2. Leave lobby" + user);
            }

            case GAME -> {
                switch (view.getGamePhase()) {
                    case PLACING_STARTER_CARD -> {
                        if (view.getHand().getSize() == 0 && !(view.getHand().getCard(0) instanceof StarterCard)) return;
                        System.out.print(cli + "Choose the starter card's side you prefer (front / back):");
                        StarterCard card = (StarterCard) view.getHand().getCard(0);
                        System.out.print(cli + "FRONT SIDE:");
                        printer.printCard(card);
                        card.flip();
                        System.out.print(cli + "BACK SIDE:");
                        printer.printCard(card);
                        card.flip();
                        System.out.print(user);
                    }

                    case CHOOSING_SECRET_GOAL -> {
                        System.out.print("\n" + cli + "Your hand:");
                        printer.printHand();

                        System.out.print(cli + "Common goals:");
                        printer.printGoal(view.getCommonGoal1());
                        printer.printGoal(view.getCommonGoal2());

                        ArrayList<Goal> goals = view.getSecretGoalChoices();
                        System.out.print("\n" + cli + "You can choose between these two goals:");
                        printer.printGoal(goals.getFirst());
                        printer.printGoal(goals.getLast());
                        System.out.print(user);
                    }

                    case PLAYING_GAME -> {
                        if (view.isYourTurn()) {
                            switch (inputState) {
                                case PLAY_SELECT_SIDE -> {
                                    printer.printHand();
                                    System.out.print(cli + "Do you want to flip your hand? (y / n)" + user);
                                }

                                case PLAY_SELECT_CARD -> {
                                    printer.printResources();
                                    printer.printFieldTemp();
                                    printer.printHand();
                                    System.out.print(cli + "Which card do you want to play?" + user);
                                }

                                case PLAY_SELECT_COORDS -> System.out.print(cli +  "Now insert the position in format: X Y" + user);

                                case DRAW -> {
                                    printer.printGameArea();
                                    System.out.print(cli + "From where do you want to draw?" + cli + "Resource Deck -> ResDeck" + cli + "Golden Deck -> GoldDeck" + cli + "Resource card spaces -> RES1 - RES2" + cli + "Golden card spaces -> GOLD1 - GOLD2" + user);
                                }
                            }
                        } else {
                            //not your turn
                            //print all the field and your hand and the decks
                            System.out.print(cli + "wait for your turn"
                                            + cli + "1.send message"
                                            + cli + "2.leave lobby");
                            printer.printScoreboard();
                        }
                    }
                }
            }
            default -> {}
        }
    }

    // ~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~
    // Login methods:
    // ~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~

    private void chooseConnectionType() {
        System.out.print(cli + "Insert your connection type: [TCP|RMI]" + user);
        String choice = scanner.nextLine();

        if (choice.equalsIgnoreCase("TCP")) {

            System.out.print(cli + "Insert the server IP" + user);
            String IP = scanner.nextLine();
            //IP="127.0.0.1";

            try {
                this.view = new TCPView(IP, 4321);
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

            } catch (RemoteException | NotBoundException ex) {
                view.removeObserver(this);
                exit(1);
            }
        } else exit(1);
    }
    
    // ~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~
    // Main Menu methods:
    // ~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~

    private void chooseMenuAction(String in) throws InterruptedException {
        if (state != TUIState.MENU) return;
        if (!isNumeric(in)) {
            System.out.println(Color.warning + "didn't insert numeric value!!" + Color.reset);
            return;
        }

        int choice = Integer.parseInt(in);

        switch (choice) {
            case 1 -> state = TUIState.CREATE_LOBBY;
            case 2 -> {
                if (view.getLobbies().isEmpty()) {
                    System.out.println(Color.warning + "\nThere are no open lobbies!\n" + Color.reset);
                } else {
                    state = TUIState.JOIN_LOBBY;
                }
            }
            case 3 -> {
                System.out.print(cli + "Exiting game");
                view.removeObserver(this);
                exit(0);
            }
            default -> System.out.println(Color.warning + "Invalid choice!!\n" + Color.reset);
        }
    }

    private void createLobby(String in) throws InterruptedException {
        if (view.getID() != null) {
            System.out.println(Color.warning + "\nCannot create a lobby while in another lobby!!" + Color.reset);
            state = TUIState.MENU;
            return;
        }
        if (!isNumeric(in)) {
            System.out.println(Color.warning + "didn't insert numeric value!!" + Color.reset);
            return;
        }

        int numOfPlayers = Integer.parseInt(in);

        if (numOfPlayers < 2 || numOfPlayers > 4) {
            System.out.println(Color.warning + "invalid number of player!" + Color.reset);
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

    private void chooseLobby(String in) throws InterruptedException {

        if (view.getID() != null) {
            System.out.println(Color.warning + "\nCannot create a lobby while in another lobby!!" + Color.reset);
            return;
        }

        int id;


        if (!isNumeric(in)) {
            System.out.println(Color.warning + "didn't insert numeric value!!" + Color.reset);
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
            System.out.println(Color.warning + "Cannot join multiple lobbies!!" + Color.reset);
        } catch (FullLobbyException e) {
            System.out.println(Color.warning + "Lobby #" + id + " is full. Cannot join!!" + Color.reset);
        } catch (LobbyDoesNotExistsException e) {
            System.out.println(Color.warning + "Lobby #" + id + " does not exist!!" + Color.reset);
        } catch (IOException | ClassNotFoundException | UnexistentUserException e) {
            view.removeObserver(this);
            Thread.currentThread().interrupt();
        }
    }

    // ~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~
    // In Lobby methods:
    // ~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~
    
    private void setPawnColor(String color) throws InterruptedException {
        if (view.getPawn() != null) {
            System.out.println(view.getPawn().toString());
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
                printStatus();
            } catch (GameAlreadyStartedException | LobbyDoesNotExistsException |
                     GameDoesNotExistException ignored) {
            } catch (IOException | UnexistentUserException e) {
                view.removeObserver(this);
                Thread.currentThread().interrupt();
            }
        } else System.out.println(Color.warning + "Color chosen does not exist!" + Color.reset);

    }

    private void chooseInLobbyAction(String in) throws InterruptedException {
        if (!isNumeric(in)) {
            System.out.println(Color.warning + "didn't insert numeric value!!" + Color.reset);
            return;
        }
        int choice = Integer.parseInt(in);
        switch (choice) {
            case 1 -> openChat();
            case 2 -> quitLobby();
        }

    }

    private void openChat() {       // TODO: fix chats
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
            System.out.println(Color.warning + "didn't insert numeric value!!" + Color.reset);
            return;
        }

        System.out.println(cli + "Opening chat (write \"quit chat\" to return to selection)");
        if (desiredChat == 1) {
            for (Message m : chats.getGlobalChat()) {
                System.out.print(Color.console + "\n[" + m.getSender().getNickname() + "]:" + Color.reset + " \"" + m.getText() + "\"");
            }
            sendMessage(null);
        } else if (desiredChat > 1 && desiredChat < view.getLobbies().get(view.getID()).getPlayers().size() + 2) {
            for (Message m : chats.getPrivateChat(players.get(desiredChat - 2))) {
                System.out.print("\n\u001B[38;2;255;165;0m[" + m.getSender().getNickname() + "]:" + Color.reset + " \"" + m.getText() + "\"");
            }
            sendMessage(players.get(desiredChat - 2));
        } else System.out.println(Color.warning + "Invalid choice!!\n" + Color.reset);
    }

    private void sendMessage(Player player) {
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
            System.out.println(Color.warning + "Non connected to a lobby!!" + Color.reset);
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

    // ~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~
    // In Game methods:
    // ~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~

    protected void placeStarterCard(String choice) throws InterruptedException {
        if (view.getHand().getSize() == 0 || !view.getHand().getCard(0).getClass().equals(StarterCard.class)) return;
        StarterCard card = (StarterCard) view.getHand().getCard(0);

        CardSide side = switch (choice.toUpperCase()) {
            case "FRONT", "F" -> CardSide.FRONT;
            case "BACK", "B" -> CardSide.BACK;
            default -> null;
        };
        if (side == null) {
            System.out.println(Color.warning + "Invalid choice!!\n" + Color.reset);
            return;
        }

        if (side.equals(CardSide.BACK)) {
            card.flip();
        }

        try {
            view.chooseCardSide(side);
            System.out.print(cli + "Card placed in position (0, 0), wait for other players to continue...");
            semaphore.acquire();
        } catch (IOException | GameDoesNotExistException | EmptyDeckException | HandIsFullException |
                 UnexistentUserException | WrongGamePhaseException e) {
            System.out.print(e.getClass().getName());
        }

    }

    protected void chooseSecretGoal(String choice) throws InterruptedException {
        if (view.getSecretGoal() != null) return;

        if (!isNumeric(choice)) {
            System.out.println(Color.warning + "didn't insert numeric value!!" + Color.reset);
        }
        int id = Integer.parseInt(choice);

        try {
            view.chooseSecretGoal(id);
            System.out.print(cli + "goal chosen");
        } catch (IOException | UnexistentUserException e) {
            throw new RuntimeException(e);
        } catch (WrongGamePhaseException | GameDoesNotExistException ignored) {
        } catch (IllegalGoalChosenException e) {
            System.out.print(Color.warning + "Invalid choice!!\n" + Color.reset);
            printStatus();
        }
    }

    private void chooseInGameAction(String in) {
        printer.printScoreboard();
        printer.printFieldTemp();

        if (!isNumeric(in)) {
            System.out.println(Color.warning + "Didn't insert numeric value!!" + Color.reset);
//            return;
        }
//        int action = Integer.parseInt(in);

//        switch (action) {
//            case 1 -> print field of another player;
//            case 2 -> send message;
//            case 3 -> leave game;
//        }
    }

    private void playCard(String in) {
        switch (inputState) {
            case PLAY_SELECT_SIDE -> {
                switch (in.toUpperCase()) {
                    case "Y", "YES" -> {
                        for (int i = 0; i < view.getHand().getSize(); i++) {
                            view.getHand().getCard(i).flip();
                        }
                        printStatus();
                    }

                    case "N", "NO" -> {
                        inputState = InputState.PLAY_SELECT_CARD;
                        printStatus();
                    }

                    default -> System.out.println(Color.warning + "[ERROR]: Invalid choice!!" + Color.reset);
                }
            }

            case PLAY_SELECT_CARD -> {
                if (!isNumeric(in)) {
                    System.out.println(Color.warning + "Didn't insert numeric value!!" + Color.reset);
                    return;
                }
                cardChoice = Integer.parseInt(in);

                if (cardChoice < 0 || cardChoice >= view.getHand().getSize()) {
                    System.out.println(Color.warning + "Invalid choice!!\n" + Color.reset);
                } else {
                    inputState = InputState.PLAY_SELECT_COORDS;
                    printStatus();
                }
            }

            case PLAY_SELECT_COORDS -> {
                String X;
                String Y;
                List<String> coords = Arrays.asList(in.split("\\s+"));
                if ((long) coords.size() != 2) {
                    System.out.println(Color.warning + "[ERROR]: Inserted wrong format!!" + Color.reset);
                    return;
                }
                X = coords.getFirst();
                Y = coords.getLast();

                if (!isNumeric(X) || !isNumeric(Y)) {
                    System.out.println(Color.warning + "[ERROR]: didn't insert numeric value!!" + Color.reset);
                    return;
                }
                int x = Integer.parseInt(X);
                int y = Integer.parseInt(Y);

                Coords position = new Coords(x, y);

                try {
                    check.checkPlayCard(cardChoice, position);

                    view.playCard(cardChoice, position);
                    semaphore.acquire();
                    for (int i = 0; i < view.getHand().getSize(); i++) {
                        if (view.getHand().getCard(i).getSide().equals(CardSide.BACK)) view.getHand().getCard(i).flip();
                    }

                } catch (IllegalActionException | NotYourTurnException | LobbyDoesNotExistsException |
                         GameDoesNotExistException ignored) {
                } catch (IllegalMoveException e) {
                    if (e instanceof OccupiedCoordsException) {
                        System.out.format(cli + "Coords (%d, %d) already occupied:\n", x, y);
                        printer.printCard(view.getMyField().getMatrix().get(position));
                    }
                    if (e instanceof RequirementsNotSatisfiedException) {
                        System.out.println(Color.warning + "[ERROR]: You don't have enough resources to play this card!!");
                        inputState = InputState.PLAY_SELECT_SIDE;
                    }
                    if (e instanceof UnreachablePositionException) {
                        System.out.println(Color.warning + "[ERROR]: Invalid spot!!");
                    }
                } catch (IOException | InterruptedException | UnexistentUserException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }


    protected void drawCard(String choice) throws InterruptedException {
        if (!view.isYourTurn()) {
            System.out.println(Color.warning + "Cannot draw card because it's not your turn!!" + Color.reset);
            return;
        }
        if (!view.getAction().equals(Action.DRAW)) {
            System.out.println(Color.warning + "Cannot draw card because you have to play one first!!" + Color.reset);
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
            System.out.print(Color.warning + "Invalid choice!!\n" + Color.reset);
            return;
        }

        try {
            view.drawCard(type);
            semaphore.acquire();
            inputState = InputState.PLAY_SELECT_SIDE;
        } catch (IllegalActionException | HandIsFullException | EmptyBufferException | NotYourTurnException |
                 LobbyDoesNotExistsException | GameDoesNotExistException ignored) {
        } catch (EmptyDeckException e) {
            System.out.println(Color.warning + "Cannot draw from this deck. Reason: empty!!" + Color.reset);
        } catch (IOException | UnexistentUserException e) {
            throw new RuntimeException(e);
        }
    }
}