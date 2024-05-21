package it.polimi.ingsw.view.CLI;

import it.polimi.ingsw.controller.exceptions.*;
import it.polimi.ingsw.model.chat.Chat;
import it.polimi.ingsw.model.chat.Message;
import it.polimi.ingsw.model.exceptions.*;
import it.polimi.ingsw.model.game.Action;
import it.polimi.ingsw.model.game.GamePhase;
import it.polimi.ingsw.model.game.Lobby;
import it.polimi.ingsw.model.player.Pawn;
import it.polimi.ingsw.model.player.Player;
import it.polimi.ingsw.network.RMI.ClientRemoteInterface;
import it.polimi.ingsw.network.RMI.RemoteInterface;
import it.polimi.ingsw.network.netMessage.HeartBeatMessage;
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
import java.util.InputMismatchException;
import java.util.Scanner;

import static java.lang.System.exit;

public class CLI implements Runnable, ViewObserver {
    private View view;
    private ViewController check;
    private CLIGame game;
    private final InputManager input = new InputManager();
    private static final String warningColor = "\u001B[31m";
    private static final String reset = "\u001B[0m";
    private static final String cli = "\u001B[38;2;255;165;0m" + "\n[+] " + "\u001B[0m";
    private static final String user = "\u001B[38;2;255;165;0m" + "\n[-] " + "\u001B[0m";
    private static final String green = "\u001B[32m";
    private static final String blue = "\u001B[34m";
    private static final String yellow = "\u001B[93m";
    private static final String red = "\u001B[31m";
    private static final String GoldColor = "\u001B[30;43m"; // Gold

    public CLI() {
    }

    private boolean isNumeric(String s){
        return s.matches("-?\\d+");
    }


    @Override
    public void run() {
        int lobbyAction = 0;
        printHello();

        chooseConnectionType();

        this.game = new CLIGame(view, input);
        this.check = new ViewController(view);
        this.view.addObserver(this);

        try {
            view.login(input.askInput(cli + "Insert username:" + user));
            Thread.sleep(500);
        } catch (NicknameAlreadyTakenException | FullLobbyException | IOException | ClassNotFoundException | InterruptedException ex) {
            view.removeObserver(this);
            Thread.currentThread().interrupt();
        }

        try {
            view.getCurrentStatus();
        } catch (IOException | FullLobbyException | NicknameAlreadyTakenException | ClassNotFoundException e) {
            view.removeObserver(this);
            Thread.currentThread().interrupt();
        }

        do {
            if (!check.userConnectedToLobby()) {
                lobbyAction = chooseAction();
                switch (lobbyAction) {
                    case 1 -> createLobby();
                    case 2 -> chooseLobby();
                }
                lobbyAction = -1;

                try {
                    view.getCurrentStatus();
                } catch (IOException | FullLobbyException | NicknameAlreadyTakenException | ClassNotFoundException e) {
                    throw new RuntimeException(e);
                }

            } else {
                while (view.getPawn() == null) setPawnColor();

                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }

                while (lobbyAction != 2 && view.getLobbies().get(view.getID()).getPlayers().size() < view.getLobbies().get(view.getID()).getNumOfPlayers() && view.getLobbies().get(view.getID()).getPawnBuffer().getPawnList().size() > Pawn.values().length - view.getLobbies().get(view.getID()).getNumOfPlayers()) {
                    lobbyAction = waiting();
                    switch (lobbyAction) {
                        case 1 -> openChat();
                        case 2 -> quitLobby();
                    }
                }

                try {
                    view.getCurrentStatus();
                } catch (IOException | ClassNotFoundException | NicknameAlreadyTakenException | FullLobbyException e) {
                    view.removeObserver(this);
                    Thread.currentThread().interrupt();
                }

                if (lobbyAction != 2) {
                    // Game CLI
                    GamePhase phase = view.getGamePhase();
                    if (phase != null) {

                        do {
                            switch (phase) {
                                case PLACING_STARTER_CARD -> game.placeStarterCard();
                                case CHOOSING_SECRET_GOAL -> game.chooseSecretGoal();
                                case PLAYING_GAME -> {
                                    if (view.isYourTurn()) {
                                        if (view.getAction().equals(Action.PLAY)) game.playCard();
                                        else if (view.getAction().equals(Action.DRAW)) game.drawCard();
                                    }
                                }
                                case GAME_FINISHED -> {}
                                case null -> quitLobby();
                            }
                            try {
                                Thread.sleep(500);
                            } catch (InterruptedException e) {
                                throw new RuntimeException(e);
                            }

                            phase = view.getGamePhase();
                        } while (phase != GamePhase.GAME_FINISHED);
                    }
                }
            }
        } while(true);
    }

    private void chooseConnectionType() {
        input.setPipedInput(true);              // TODO: Remove when TUI is functioning
        String choice = input.askInput(cli + "Insert your connection type: [TCP|RMI]" + user);
        if (choice.equals("console")) choice = "TCP";

        if (choice.equalsIgnoreCase("TCP")) {
            System.out.println(cli + "TCP selected..");
            try {
                this.view = new TCPView("127.0.0.1", 4321);
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
            new Thread(() -> {
                try {
                    ((TCPView)view).startClient();
                } catch (IOException | ClassNotFoundException e) {
                    throw new RuntimeException(e);
                }
            }).start();

        } else if (choice.equalsIgnoreCase("RMI")) {
            Registry registry;
            try {
                registry = LocateRegistry.getRegistry();
            } catch (RemoteException ex) {
                throw new RuntimeException(ex);
            }
            String remoteObjectName = "RMIServer";
            RemoteInterface RMIServer;
            try {
                RMIServer = (RemoteInterface) registry.lookup(remoteObjectName);
            } catch (RemoteException | NotBoundException ex) {
                throw new RuntimeException(ex);
            }
            try {
                this.view = new RMIView(RMIServer);
            } catch (RemoteException | NotBoundException ex) {
                throw new RuntimeException(ex);
            }
            try {
                RMIServer.connect((ClientRemoteInterface) UnicastRemoteObject.exportObject((ClientRemoteInterface)this.view, 0));
            } catch (NotBoundException | RemoteException ex) {
                throw new RuntimeException(ex);
            }

        } else exit(1);
    }

    private int chooseAction() {
        int choice;
        do {
            String in = input.askInput(cli + "What would you like to do?" + cli +
                    "1. Create a lobby and wait for other players to join" + cli +
                    "2. Join an already open lobby" + cli +
                    "3. Quit" + user);
            if ("123".contains(in)) choice = Integer.parseInt(in);
            else choice = -1;

            if (choice == 3) {
                System.out.print(cli + "Exiting game");
                printDots();
                view.removeObserver(this);
                exit(0);
            }
            if (choice == 2) {
                if (view.getLobbies().isEmpty()) {
                    System.out.println(warningColor + "\n[ERROR]: There are no open lobbies!\n" + reset);
                    choice = -1;
                }
            } else if (choice != 1) {
                System.out.println(warningColor + "\n[ERROR]: Invalid choice!!\n" + reset);
                choice = -1;
            }
        } while (choice == -1);
        return choice;
    }

    private void printOpenLobbies() {
        System.out.print(cli + "Open lobbies:");
        HashMap<Integer, Lobby> lobbies = view.getLobbies();

        for (Integer i : lobbies.keySet()) {
            if (lobbies.get(i).getPlayers().size() != lobbies.get(i).getNumOfPlayers()) {
                System.out.print(cli + "-> Lobby " + green + "#" + i + reset + ": " + lobbies.get(i).getPlayers().size() + "/" + lobbies.get(i).getNumOfPlayers() + " - [");

                for (Player p : lobbies.get(i).getPlayers()) {
                    if (p.equals(lobbies.get(i).getPlayers().getLast())) {
                        System.out.print(p.getNickname() + "]");
                    } else {
                        System.out.print(p.getNickname() + " - ");
                    }
                }
            } else System.out.print(cli + "-> Lobby " + warningColor + "#" + i + reset + ": FULL");
        }
        System.out.println();
    }

    private void chooseLobby() {
        if (view.getID() != null) {
            System.out.println(warningColor + "\n[ERROR]: Cannot create a lobby while in another lobby!!" + reset);
            return;
        }

        boolean chosen = false;
        int ID;

        printOpenLobbies();
        do {
            try {
                ID = Integer.parseInt(input.askInput(cli + "Which lobby do you want to join? (-1 to return to selection)" + user + "#"));
            } catch (InputMismatchException e) {
                ID = -1;
            }

            if (ID == -1) return;
            try {
                check.checkJoinLobby(ID);
                view.joinLobby(ID);
                chosen = true;

                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }

            } catch (NicknameAlreadyTakenException ignored) {}
            catch (CannotJoinMultipleLobbiesException e) {
                System.out.println(warningColor + "[ERROR]: Cannot join multiple lobbies!!" + reset);
            } catch (FullLobbyException e) {
                System.out.println(warningColor + "[ERROR]: Lobby #" + ID + " is full. Cannot join!!" + reset);
            } catch (LobbyDoesNotExistsException e) {
                System.out.println(warningColor + "[ERROR]: Lobby #" + ID + " does not exist!!" + reset);
            } catch (IOException | ClassNotFoundException | UnexistentUserException e) {
                throw new RuntimeException(e);
            }
        } while (!chosen);

    }

    private void createLobby() {
        if (view.getID() != null) {
            System.out.println(warningColor + "\n[ERROR]: Cannot create a lobby while in another lobby!!" + reset);
            return;
        }

        try {
            int numOfPlayers;
            do {
                numOfPlayers = Integer.parseInt(input.askInput(cli + "Insert number of players: [2][3][4] (-1 to return to selection)" + user));
                if (numOfPlayers == -1) {
                    System.out.println(cli + "Lobby not created, returning to selection...");
                    return;
                }

                check.checkCreateLobby(numOfPlayers);
                System.out.print(cli + "Trying to create lobby");
                printDots();

                view.createLobby(numOfPlayers);
                Thread.sleep(500);
            } while (numOfPlayers < 2 || numOfPlayers > 4);

        } catch (LobbyDoesNotExistsException | IOException | NicknameAlreadyTakenException | FullLobbyException |
                 CannotJoinMultipleLobbiesException ignored) {}
        catch (ClassNotFoundException | UnexistentUserException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    private int waiting() {
        int choice;
        do {
            choice = Integer.parseInt(input.askInput(cli + "Waiting for players..." + cli + "1. Open chat" + cli + "2. Quit lobby" + user, "3"));

            if (choice != 1 && choice != 2 && choice != 3)
                System.out.println(warningColor + "\n[ERROR]: Invalid choice\n" + reset);
        } while (choice != 1 && choice != 2 && choice != 3);

        return choice;
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

    private void printPlayers(Integer ID) {
        Lobby lobby = view.getLobbies().get(ID);

        System.out.print(cli + "Players (lobby " + green + "#" + ID + reset + "): " + lobby.getPlayers().size() + "/" + lobby.getNumOfPlayers());
        for (Player p : lobby.getPlayers()) {
            switch (p.getPawn()) {
                case Pawn.BLUE -> System.out.print(cli + blue + "● " + reset);
                case Pawn.RED -> System.out.print(cli + red + "● " + reset);
                case Pawn.GREEN -> System.out.print(cli + green + "● " + reset);
                case Pawn.YELLOW -> System.out.print(cli + yellow + "● " + reset);
                case null -> System.out.print(cli + "● " + reset);
            }
            System.out.print(p.getNickname());
        }
        System.out.println();
    }

    private void setPawnColor() {
        if (view.getPawn() != null) {
            System.out.println(cli + "Pawn already chosen!!");
            return;
        }

        Pawn pawn;
        do {
            printAvailablePawns();
            String color = input.askInput(cli + "Which pawn do you want to choose?" + user);

            pawn = switch (color.toUpperCase()) {
                case "RED", "R" -> Pawn.RED;
                case "GREEN", "G" -> Pawn.GREEN;
                case "BLUE", "B" -> Pawn.BLUE;
                case "YELLOW", "Y", "BANANA" -> Pawn.YELLOW;
                default -> null;
            };

            if (pawn != null) {
                try {
                    view.choosePawn(pawn);
                } catch (PawnAlreadyTakenException e) {
                    System.out.print(cli + "Pawn already taken!\n");
                    pawn = null;
                } catch (Exception ignored) {}
            } else System.out.print(cli + "Color chosen does not exist!\n");

            try {
                view.getCurrentStatus();
            } catch (IOException | ClassNotFoundException | NicknameAlreadyTakenException | FullLobbyException e) {
                view.removeObserver(this);
                Thread.currentThread().interrupt();
            }
        } while (pawn == null);

        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    private void openChat() {       // TODO: fix private chats
        Chat chat = view.getChat();
        ArrayList<Player> players = new ArrayList<>();
        for (int i = 0; i < view.getLobbies().get(view.getID()).getPlayers().size(); i++) {
            if (!view.getLobbies().get(view.getID()).getPlayers().get(i).equals(view.getPlayer())) players.add(view.getLobbies().get(view.getID()).getPlayers().get(i));
        }

        System.out.print(cli + "Which chat do you want to open?" + cli + "1. Global chat");
        for (int i = 0; i < players.size(); i++) {
            System.out.print(cli + (i + 2) + ". Private chat with " + players.get(i).getNickname());
        }
        int choice = Integer.parseInt(input.askInput(user));

        System.out.println(cli + "Opening chat (write \"quit chat\" to return to selection)");
        if (choice == 1) {
                for (Message m : chat.getGlobalChat()) {
                    System.out.print("\u001B[38;2;255;165;0m\n[" + m.getSender().getNickname() + "]:\u001B[0m" + " \"" + m.getText() + "\"");
                }
                sendMessage(null);
        }
        else if (choice > 1 && choice < view.getLobbies().get(view.getID()).getPlayers().size() + 2) {
            for (Message m : chat.getPrivateChat(players.get(choice - 2))) {
                System.out.print("\u001B[38;2;255;165;0m" + "\n[" + m.getSender().getNickname() + "] " + "\u001B[0m" + ": \"" + m.getText() + "\"");
            }
            sendMessage(players.get(choice - 2));
        } else System.out.println(warningColor + "[ERROR]: Invalid choice!!" + reset);
    }

    private void sendMessage(Player player) {
        String text = input.askInput("\u001B[38;2;255;165;0m\n[" + view.getNickname() + "]: \u001B[0m ");

        if (text.equalsIgnoreCase("quit chat")) return;
        Message msg;

        if (player == null) {
            msg = new Message(text, view.getPlayer(), null, true);
            try {
                view.sendMessage(msg);
            } catch (IOException | UnexistentUserException | PlayerChatMismatchException e) {
                throw new RuntimeException(e);
            } catch (LobbyDoesNotExistsException | GameDoesNotExistException ignored) {}
        }
        else {
            msg = new Message(text, view.getPlayer(), player, false);
            try {
                view.sendMessage(msg);
            } catch (IOException | UnexistentUserException | PlayerChatMismatchException e) {
                throw new RuntimeException(e);
            } catch (LobbyDoesNotExistsException | GameDoesNotExistException ignored) {}
        }

        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    private void quitLobby() {
        try {
            check.checkLeaveLobby();
        } catch (NotConnectedToLobbyException e) {
            System.out.println(warningColor + "[ERROR]: Non connected to a lobby!!" + reset);
        }

        System.out.print(cli + "Trying leaving lobby");
        printDots();

        try {
            view.leaveLobby();
        } catch (GameAlreadyStartedException e) {
            System.out.print(cli + "The game is already started! Cannot leave the lobby!");
        } catch (LobbyDoesNotExistsException | NicknameAlreadyTakenException | GameDoesNotExistException ignored) {}
        catch (IOException | FullLobbyException | ClassNotFoundException | UnexistentUserException e) {
            throw new RuntimeException(e);
        }

        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void update(NetMessage message) {
        switch (message) {
            case LoginMessage m -> {
                if (m.getNickname().equals(view.getNickname())) {
                    System.out.print(cli + "Successfully logged in.. Hello " + m.getNickname() + "!");
                }

                try {
                    view.getCurrentStatus();
                } catch (IOException | ClassNotFoundException | NicknameAlreadyTakenException | FullLobbyException e) {
                    view.removeObserver(this);
                    Thread.currentThread().interrupt();
                }
            }

            case HeartBeatMessage m -> {}

            case DisconnectMessage m -> {
                System.out.println(cli + "Server crashed");
                view.removeObserver(this);
                exit(1);
            }

            case LobbyCreatedMessage m -> {
                if (m.getCreator().equals(view.getPlayer())) {
                    System.out.print(cli + "Successfully created lobby. ID: #" + m.getID());
                    // printRemainingPlayers();
                }
            }

            case LoginFail_NicknameAlreadyTaken m -> {
                System.out.print(cli + "Cannot login. Quitting");
                view.removeObserver(this);
                exit(1);
            }

            case LobbyJoinedMessage m -> {
                if (m.getPlayer().getNickname().equals(view.getNickname())) {
                    System.out.print(cli + "Entered lobby #" + m.getID());
                    printRemainingPlayers();

                } else if (m.getID().equals(view.getID())) {
                    System.out.print(cli + m.getPlayer().getNickname() + " joined the lobby!");
                    printRemainingPlayers();
                }
            }

            case FailMessage m -> {
                if (m.getMessage().equals("Already in lobby"))
                    System.out.println(warningColor + "\n[ERROR]: Already in a lobby, cannot join another!!\n" + reset);
                if (m.getMessage().equals("ID not found"))
                    System.out.println(warningColor + "\n[ERROR]: Inserted ID does not exist!!\n" + reset);
                if (m.getMessage().equals("Full lobby"))
                    System.out.println(warningColor + "\n[ERROR]: Lobby full. Cannot join this lobby!!\n" + reset);
            }

            case LobbyLeftMessage m -> {
                if (m.getPlayer().getNickname().equals(view.getNickname())) {
                    System.out.print(cli + "Successfully left the lobby\n");
                } else if (m.getID().equals(view.getID())) {
                    System.out.print(cli + m.getPlayer().getNickname() + " left the lobby\n");
                }
            }

            case PawnAssignedMessage m -> {
                if (m.getPlayer().equals(view.getPlayer())) {
                    System.out.print(cli + "Selected color " + m.getColor().name() + "\n");
                }
            }

            case GameCreatedMessage m -> {
                System.out.println(cli + "Game is starting... Press Enter to continue");
                printPlayers(view.getID());
            }

            case ChatMessageAddedMessage m -> {
                if (m.getM().isGlobal()) {
                    if (m.getM().getSender().equals(view.getPlayer())) {
                        System.out.print(cli + "Message sent: \"" + m.getM().getText() + "\" to everyone\n");
                    }
                    else if (m.getLobbyID().equals(view.getID())) {
                        System.out.print(cli + "[" + m.getM().getSender().getNickname() + "]: \"" + m.getM().getText() + "\"\n");
                    }
                } else {
                    if (m.getM().getSender().equals(view.getPlayer())) {
                        System.out.print(cli + "Message sent: \"" + m.getM().getText() + "\" to " + m.getM().getReceiver().getNickname() + "\n");
                    }
                    else if (m.getLobbyID().equals(view.getID())) {
                        System.out.print(cli + "[PRIVATE | " + m.getM().getSender().getNickname() + "]: \"" + m.getM().getText() + "\"\n");
                    }
                }
            }

            case LobbyDeletedMessage m -> {}
            case CurrentStatusMessage m -> {System.out.println("Updating...");}
            case CardAddedToHandMessage m -> {}
            case CardRemovedFromHandMessage m -> {}
            case SecretGoalsListAssignedMessage m -> {}
            case SecretGoalAssignedMessage m -> {}
            case GameActionSwitchedMessage m -> {}
            case CardDrawnFromSourceMessage m -> {}

            case CardPlacedOnFieldMessage m -> {
                if (m.getNickname().equals(view.getPlayer().getNickname())) {
                    System.out.format(cli + "Card added (side: %s) to field in position: (%d, %d)", m.getCard().getSide(), m.getCoords().getX(), m.getCoords().getY());
                    System.out.println(cli + "Your score: " + view.getScoreboard().get(view.getPlayer()));
                } else if (m.getID().equals(view.getID())) {
                    System.out.print(cli + m.getNickname() + " played a card");
                }
            }

            case ScoreIncrementedMessage m -> {
            }

            case GamePhaseChangedMessage m -> {
                if (m.getID().equals(view.getID())) {
                    System.out.println(cli + "New game phase: " + m.getGamePhase());
                }
            }

            case LastRoundStartedMessage m -> {
                if (m.getID().equals(view.getID())) {
                    game.printScoreboard();
                    System.out.println(cli + "Last round is started");
                }
            }

            case TurnChangedMessage m -> {
                if (m.getNickname().equals(view.getNickname())) System.out.println(cli + "It's your turn");
                else if (m.getID().equals(view.getID())) {
                    if (m.getNickname().toUpperCase().charAt(m.getNickname().length() - 1) == 'S')
                        System.out.println(cli + "It's " + m.getNickname() + "' turn");
                    else System.out.println(cli + "It's " + m.getNickname() + "'s turn");
                }
            }

            case GameWinnersAnnouncedMessage m -> {
                if (m.getID().equals(view.getID())) {
                    game.printScoreboard();

                    ArrayList<Player> winners = view.getWinners();
                    if (winners.size() == 1) {
                        if (m.getWinners().contains(view.getPlayer())) System.out.println(cli + "You win!!");
                        else System.out.println(cli + "THE WINNER IS " + GoldColor + winners.getFirst().getNickname().toUpperCase() + reset + "!!");
                    }
                    else {
                        System.out.print(cli + "The winners are ");
                        for (Player winner : winners) {
                            System.out.print(cli + GoldColor + winner.getNickname() + reset);
                        }
                    }
                }
            }

            case GameTerminatedMessage m -> {
                if (m.getID().equals(view.getID())) {
                    System.out.print(cli + "Game is closing");
                    printDots();
                }
            }

            default -> throw new IllegalStateException("Unexpected value: " + message);
        }
    }

    private void printRemainingPlayers() {
        HashMap<Integer, Lobby> lobbies = view.getLobbies();
        int ID = view.getID();

        int remaining = lobbies.get(ID).getNumOfPlayers() - lobbies.get(ID).getPlayers().size();
        switch (remaining) {
            case 0 -> {
                for (Player p : view.getLobbies().get(ID).getPlayers()) {
                    if (p.getPawn() == null)
                        System.out.println(cli + "The lobby has the number of players required. Game will start when all pawns are selected");
                    return;
                }
                System.out.println(cli + "The lobby has the number of players required. Game is now starting...");
            }
            case 1 ->
                    System.out.println(cli + "Waiting for last player to join...");
            default ->
                    System.out.println(cli + "Waiting for " + remaining + " players to join...");
        }
    }

    private void printHello() {
        // TODO: I need to stretch it out
        System.out.println("                          .##.          .-.  ..#####-.    .-+..+..    .+.                       ");
        System.out.println("                  ...   .#####-.      .-#...-....-#####+. .... .#+.  .-#-                       ");
        System.out.println("             .-+##-...+#########++---+##.. ..     ..-#####+-.  -##-...##+.-.                    ");
        System.out.println("          .-####.. .+#################+.          ..  ..     ..+##+..+###-#+.   .-#.            ");
        System.out.println("       ..+####-  .++..-+############+.         ..++.         .-###-..+#####+.  ..+#+.+-         ");
        System.out.println("     ..#####+. .-##.    .++#####++..           .##-.           .+- ..#######+-...-+.-++.        ");
        System.out.println("   ..######+. .+###-.       ..+.               -####+...        ..------------------------..    ");
        System.out.println("  .-######+.  -#####-        .+.               .########.                                       ");
        System.out.println(" .+#######-. .#+#####+.      .+.      ..-.     ..+########-.          .+.      ...     ..       ");
        System.out.println(" -########.  .#########.     .+.    .+####+..    ..+########-.     ..+###-.  ..+##-.  .##+++    ");
        System.out.println(".########+.  .#+########-.   .+. .-+#+######+.   .+#+.-####+#-.  .-#######-..+#++###-.####+.    ");
        System.out.println("-########+   .+##########+.  .+..+###..-#####+..+###+...+####+..-##+.-####+-...+#++###+++..     ");
        System.out.println("+########+.   -###########+. .+.+####.  .#####.-#+##+.  .####+.-###+  +###-    .##+###. .       ");
        System.out.println("+#+######+.    .###########-..+.+####.  .+####.-#+##+.  .###++.+###+  -##+.   ..+#+###.-+       ");
        System.out.println("+#+#######.     .###########..+.+####.  .+####.-#+##+.  .####+.+###+  -#+.   .####+#####.       ");
        System.out.println("+#########-.     .+########+..+.+####.  .+####.-#+##+.  .####+.+###+.-#..   .++-+#+###..        ");
        System.out.println("-##+######+.      ..#######...+.+####-  .+####.-#+##+.  .####+.+#####..     .. .+#+###.         ");
        System.out.println(".##########-.       .+####+. .+.-#####-..+###+.-#+###-. .####-.+####+.         .+#+###-  ...    ");
        System.out.println(".+##+#######..       .+###.. .+..+#####+.+#+-  .+#++##+..###.. -######...-..-###++#+###+---     ");
        System.out.println(" .+##+#######-.....   .##.   .+. .+#####+-.     .-##+####+..   ..#######-.-####+..-#+###+.      ");
        System.out.println("  .#############++#+-.+#-.   .+.   .-##..         .-###..        .+###-. .+..+#.  ..+##..       ");
        System.out.println("  ..###+########+..-###.     .+.    ...-+. ..       ....          ....   ..  ..     ...         ");
        System.out.println("   ..###+#########+....      .+.    .-#+.. .++###--##+..                                        ");
        System.out.println("    .--###+##########++-......++..-+##-.   ....##..#-....--#-.            .-#.#+.####...        ");
        System.out.println("   .+. .-####+######################-.       ..###.#..###.###.#.#..#.#.###.-#---##--..+++.      ");
        System.out.println("   .-.    .+####++###############+..         ..#.###..#-#.+#..#.#..##..#-#.-#+#.--##..--..      ");
        System.out.println("   ..        .-++###########++-.            ...#..##..####.#..####.#...####-#+#+###+.+#--.      \n");

        try {
            Thread.sleep(1500);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        System.out.println("\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n");
    }

    private void printDots() {
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        for (int i = 0; i < 3; i++) {
            System.out.print(".");
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }
}

