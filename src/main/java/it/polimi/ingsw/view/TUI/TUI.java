package it.polimi.ingsw.view.TUI;

import it.polimi.ingsw.controller.exceptions.*;
import it.polimi.ingsw.model.card.*;
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
import java.util.*;
import java.util.concurrent.Semaphore;

import static java.lang.System.exit;

/**
 * Class TUI
 * handles all TUI states
 */
public class TUI implements Runnable, ViewObserver {
    private View view;
    private ViewController check;
    private Printer printer;
    private final Scanner scanner = new Scanner(System.in);
    private final Semaphore semaphore = new Semaphore(0);

    private TUIState state;
    private ActionState actionState;
    private ChatState chatState = null;

    private static final String cli = Color.console + "\n[+] " + Color.reset;
    private static final String user = Color.console + "\n[-] " + Color.reset;
    private String in;

    private CardSide side=CardSide.FRONT; //attribute used locally to determinate the side on which all the cards of the hand are on


    /**
     * Check if string is a numeric value
     * @param s         string to check
     * @return boolean  if string is a numeric value
     */
    public boolean isNumeric(String s) {
        return s.matches("-?\\d+");
    }

    @Override
    public void run() {
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
                    in = scanner.nextLine();
                    switch (state) {
                        case MENU -> {
                            chooseMenuAction(in);
                            printStatus();
                        }
                        case CREATE_LOBBY -> createLobby(in);
                        case CHOOSE_PAWN -> setPawnColor(in);
                        case JOIN_LOBBY -> chooseLobby(in);
                        case LOBBY -> {
                            if (chatState == null) chooseInLobbyAction(in);
                            else chat(in);
                        }

                        case GAME -> {
                            switch (view.getGamePhase()) {
                                case PLACING_STARTER_CARD -> placeStarterCard(in);
                                case CHOOSING_SECRET_GOAL -> chooseSecretGoal(in);

                                case PLAYING_GAME -> {
                                    if (view.isYourTurn()) {
                                        chatState = null;
                                        if (actionState.equals(ActionState.DRAW)) drawCard(in);
                                        else playCard(in);
                                    } else {
                                        if (chatState == null) chooseInGameAction(in);
                                        else chat(in);
                                    }
                                }
                            }
                        }
                    }
                }
            } catch (InterruptedException | IOException | ClassNotFoundException | FullLobbyException |
                     NicknameAlreadyTakenException ignored) {
            }
        }).start();

    }

    @Override
    public synchronized void update(NetMessage message) throws IOException {
        switch (message) {

            // Login messages ·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~
            case LoginSuccessMessage m -> {
                System.out.println(cli + "Successfully logged in. Hello " + m.getNickname() + "!!");
                semaphore.release();
            }

            case LoginFail_NicknameAlreadyTaken ignored -> {
                System.out.print(cli + "Nickname already taken. Exiting game");
                view.removeObserver(this);
                exit(0);
            }

            case DisconnectMessage ignored -> {
                System.out.print(cli + "Connection lost with the server. Exiting game");
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
                } else if (state == TUIState.JOIN_LOBBY) printStatus();
                semaphore.release();
            }

            case LobbyJoinedMessage m -> {
                if (m.getPlayer().equals(view.getPlayer())) {
                    System.out.println(cli + "Joined lobby #" + m.getID());
                    printer.printRemainingPlayers();
                    state = TUIState.CHOOSE_PAWN;
                    printStatus();
                } else if(view.getID()!=null && view.getID().equals(m.getID())){
                    System.out.println(cli + m.getPlayer().getNickname() + " joined the lobby!");
                    printer.printRemainingPlayers();
                    printStatus();
                }else if (state == TUIState.JOIN_LOBBY) printStatus();
                semaphore.release();
            }

            case LobbyLeftMessage m -> {
                if (m.getPlayer().equals(view.getPlayer())) {
                    System.out.println(cli + "Left lobby #" + m.getID());
                    state = TUIState.MENU;
                    printStatus();
                    semaphore.release();
                } else if (state.equals(TUIState.GAME) && m.getID().equals(view.getID()) && (
                        GamePhase.PLACING_STARTER_CARD.equals(view.getGamePhase()) ||
                                (GamePhase.CHOOSING_SECRET_GOAL.equals(view.getGamePhase())))) {

                    System.out.println(cli + m.getPlayer().getNickname() + " left the lobby");
                    state = TUIState.MENU;
                } else if (m.getID().equals(view.getID())) {
                    System.out.println(cli + m.getPlayer().getNickname() + " left the lobby");
                    chatState = null;
                    printStatus();
                } else if(state.equals(TUIState.JOIN_LOBBY)) printStatus();

            }

            case LobbyDeletedMessage ignored -> {}

            case PawnAssignedMessage m -> {
                if (state == TUIState.CHOOSE_PAWN && m.getPlayer().equals(view.getPlayer())) {
                    state = TUIState.LOBBY;
                    semaphore.release();
                }
                printStatus();
            }

            // Chat messages ·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·
            case ChatMessageAddedMessage m -> {
                if (m.getM().getSender().equals(view.getPlayer())) System.out.println(cli + "Message successfully sent");
                else {
                    if (m.getM().isGlobal()) System.out.println(cli + m.getM().getSender().getNickname() + " sent a message");
                    else System.out.println(cli + m.getM().getSender().getNickname() + " sent you a message");
                }
                printStatus();
            }

            // Game messages ·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·
            case GameCreatedMessage ignored -> {
                System.out.println(cli + "The game is starting...");
                state = TUIState.GAME;
                semaphore.release();
                actionState = ActionState.PLAY_SELECT_CARD;
                printStatus();
            }

            case GamePhaseChangedMessage m -> {
                if (state.equals(TUIState.GAME) && m.getID().equals(view.getID()))
                    semaphore.release();
                printStatus();
            }

            case SecretGoalAssignedMessage ignored -> {
                System.out.println(cli + "Goal chosen. Waiting for other players to continue...");
                semaphore.release();
            }

            case CardPlacedOnFieldMessage m -> {
                if(!m.getNickname().equals(view.getPlayer().getNickname())) return;
                if (m.getCard() instanceof StarterCard)
                    System.out.print(cli + "Card placed in position (0, 0), wait for other players to continue...");
                else if (m.getCard() instanceof ResourceCard) {
                    System.out.println(cli + "Card placed successfully on coords " + m.getCoords());
                    if (!view.isLastRound()) actionState = ActionState.DRAW;
                    else return;
                    printStatus();
                }
                semaphore.release();
            }

            case TurnChangedMessage ignored -> {
                if (view.isLastRound()) actionState = ActionState.PLAY_SELECT_CARD;
                if (view.isLastRound()) System.out.println(cli + "Last round started");
                semaphore.release();
                printStatus();
            }

            case GameWinnersAnnouncedMessage m -> {
                if (m.getID().equals(view.getID())) {
                    printer.printFinalScoreboard();

                    ArrayList<Player> winners = m.getWinners();
                    if (winners.size() == 1) {
                        if (winners.contains(view.getPlayer())) System.out.println(cli + "You win!!");
                        else
                            System.out.println(cli + "The winner is " + Color.gold + winners.getFirst().getNickname() + Color.reset + "!!");
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
                chatState = null;
                semaphore.release();
                printStatus();
            }

            // Fail messages ·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·
            case FailMessage m -> {
                if (actionState == ActionState.PLAY_SELECT_COORDS) actionState = ActionState.PLAY_SELECT_CARD;
                if (m.getNickname().equals(view.getNickname())) {
                    System.out.println(m.getMessage());
                    printStatus();
                }
                semaphore.release();
            }

            default -> {}
        }

    }

    /**
     * Prints what the main method is supposed to see
     */
    public void printStatus() {
        switch (state) {
            case MENU -> System.out.print(cli + "What would you like to do?"
                    + cli + "1. Create a new lobby"
                    + cli + "2. Join an open lobby"
                    + cli + "3. Show game rules"
                    + cli + "4. Exit game" + user);

            case CREATE_LOBBY -> System.out.print(cli + "How many players? (-1 to return to selection)" + user);
            case JOIN_LOBBY -> {
                printer.printOpenLobbies();
                System.out.print(cli + "Which lobby do you want to join? (-1 to return to selection)" + user);
            }

            case CHOOSE_PAWN -> {
                printer.printAvailablePawns();
                System.out.print(cli + "Choose your pawn from the list" + user);
            }

            case LOBBY -> {
                switch (chatState) {
                    case null -> {
                        printer.printPlayers(view.getID());
                        System.out.print(cli + "1. Open chats" + cli + "2. Leave lobby" + user);
                    }

                    case SELECT_CHAT -> {
                        // Taking all players currently in lobby (except the player itself)
                        ArrayList<Player> players = getOtherPlayers();

                        System.out.print(cli + "Which chat do you want to open? (-1 to return to selection)" + cli + "1. Global chat");
                        for (int i = 0; i < players.size(); i++) {
                            System.out.print(cli + (i + 2) + ". Private chat with " + players.get(i).getNickname());
                        }
                        System.out.print(user);
                    }

                    case SEND_MESSAGE -> {
                        ArrayList<Message> chat;
                        if (ChatState.SEND_MESSAGE.getNum() == 1) chat = view.getChat().getGlobalChat();
                        else {
                            // Taking all players currently in lobby (except the player itself)
                            ArrayList<Player> players = getOtherPlayers();
                            chat = view.getChat().getPrivateChat(players.get(ChatState.SEND_MESSAGE.getNum() - 2));
                        }

                        for (Message m : chat) System.out.print(Color.console + "\n[" + m.getSender().getNickname() + "]" + Color.reset + " : \"" + m.getText() + "\"");
                        System.out.print("\n" + cli + "Write your message (type \"quit chat\" to return to lobby): " + user);
                    }
                }
            }

            case GAME -> {
                switch (view.getGamePhase()) {
                    case PLACING_STARTER_CARD -> {
                        if (view.getHand().getSize() == 0)
                            return;
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
                            switch (actionState) {
                                case PLAY_SELECT_CARD -> {
                                    printer.printGoal(view.getCommonGoal1());
                                    printer.printGoal(view.getCommonGoal2());
                                    printer.printGoal(view.getSecretGoal());

                                    printer.printField(view.getNickname());
                                    printer.printHand();
                                    System.out.print(cli + "Which card do you want to play? (press f to flip all cards)" + user);
                                }

                                case PLAY_SELECT_COORDS ->
                                        System.out.print(cli + "Now insert the position in format: X Y" + user);

                                case DRAW -> {
                                    printer.printGameArea();
                                    System.out.print(cli + "From where do you want to draw?" + cli + "Resource Deck -> ResDeck" + cli + "Golden Deck -> GoldDeck" + cli + "Resource deck buffers -> RES1 - RES2" + cli + "Golden deck buffers -> GOLD1 - GOLD2" + user);
                                }
                            }
                        } else {
                            switch (chatState) {
                                case null -> {
                                    printer.printScoreboard();
                                    System.out.print(cli + "Waiting for your turn..."
                                            + cli + "1. View your field and goals"
                                            + cli + "2. View opponents' field"
                                            + cli + "3. Open chats"
                                            + cli + "4. Leave game" + user);
                                }

                                case SELECT_CHAT -> {
                                    // Taking all players currently in lobby (except the player itself)
                                    ArrayList<Player> players = getOtherPlayers();

                                    System.out.print(cli + "Which chat do you want to open? (-1 to return to selection)" + cli + "1. Global chat");
                                    for (int i = 0; i < players.size(); i++) {
                                        System.out.print(cli + (i + 2) + ". Private chat with " + players.get(i).getNickname());
                                    }
                                    System.out.print(user);
                                }

                                case SEND_MESSAGE -> {
                                    ArrayList<Message> chat;
                                    if (ChatState.SEND_MESSAGE.getNum() == 1) chat = view.getChat().getGlobalChat();
                                    else {
                                        // Taking all players currently in lobby (except the player itself)
                                        ArrayList<Player> players = getOtherPlayers();
                                        chat = view.getChat().getPrivateChat(players.get(ChatState.SEND_MESSAGE.getNum() - 2));
                                    }

                                    for (Message m : chat) System.out.print(Color.console + "\n[" + m.getSender().getNickname() + "]" + Color.reset + " : \"" + m.getText() + "\"");
                                    System.out.print("\n" + cli + "Write your message (type \"quit chat\" to return to lobby): " + user);
                                }
                            }
                        }
                    }
                }
            }
            default -> {}
        }
    }

    /**
     * Get players currently in same lobby as the one playing
     * @return list of players
     */
    private ArrayList<Player> getOtherPlayers() {
        ArrayList<Player> players = new ArrayList<>();
        for (int i = 0; i < view.getLobbies().get(view.getID()).getPlayers().size(); i++) {
            if (!view.getLobbies().get(view.getID()).getPlayers().get(i).equals(view.getPlayer()))
                players.add(view.getLobbies().get(view.getID()).getPlayers().get(i));
        }
        return players;
    }

    // ~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~
    // Login methods:
    // ~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~

    /**
     * Method to choose type of connection (TCP / RMI) + server IP to connect
     */
    private void chooseConnectionType() {
        String choice="";
        String port="";

        while (!choice.equalsIgnoreCase("TCP") && !choice.equalsIgnoreCase("RMI") && !isNumeric(port)) {
            System.out.print(cli + "Insert your connection type: [TCP|RMI]" + user);
            choice = scanner.nextLine();

            if (choice.equalsIgnoreCase("TCP")) {

                System.out.print(cli + "Insert the server IP" + user);
                String IP = scanner.nextLine();

                System.out.print(cli + "Insert the server port" + user);
                port = scanner.nextLine();
                if (port.isEmpty()) port = "4321";

                try {
                    this.view = new TCPView(IP, Integer.parseInt(port));
                } catch (IOException e) {
                    if (view != null) view.removeObserver(this);
                    Thread.currentThread().interrupt();
                } catch (NumberFormatException ignored) {
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
                    System.out.print(cli + "Insert the server IP" + user);
                    String IP = scanner.nextLine();

                    registry = LocateRegistry.getRegistry(IP, 0);

                    String remoteObjectName = "RMIServer";
                    RemoteInterface RMIServer;

                    RMIServer = (RemoteInterface) registry.lookup(remoteObjectName);
                    this.view = new RMIView(RMIServer);

                } catch (RemoteException | NotBoundException ex) {
                    view.removeObserver(this);
                    exit(1);
                }
            } else System.out.println(Color.warning + "Invalid choice!!\n" + Color.reset);
        }
    }

    // ~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~
    // Main Menu methods:
    // ~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~

    /**
     * Main menu actions
     * @param in                        input inserted by the user
     */
    private void chooseMenuAction(String in) {
        if (state != TUIState.MENU) return;
        if (!isNumeric(in)) {
            System.out.println(Color.warning + "Numeric value required!!" + Color.reset);
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
            case 3 -> printer.printRules();
            case 4 -> {
                System.out.print(cli + "Exiting game");
                view.removeObserver(this);
                try {
                    view.disconnect(view.getNickname());
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                exit(0);
            }
            default -> {
                System.out.println(Color.warning + "Invalid choice!!\n" + Color.reset);
                printStatus();
            }
        }
    }

    /**
     * Method to create a new lobby
     * @param in                        input inserted by the user (supposed to be numOfPlayers or -1)
     * @throws InterruptedException     throws this exception if thread is interrupted
     */
    private void createLobby(String in) throws InterruptedException {
        if (view.getID() != null) {
            System.out.println(Color.warning + "\nCannot create a lobby while in another lobby!!" + Color.reset);
            state = TUIState.MENU;
            return;
        }
        if (!isNumeric(in)) {
            System.out.println(Color.warning + "Numeric value required!!" + Color.reset);
            printStatus();
            return;
        }

        int numOfPlayers = Integer.parseInt(in);
        if (numOfPlayers == -1) {
            state = TUIState.MENU;
            printStatus();
            return;
        }
        if (numOfPlayers < 2 || numOfPlayers > 4) {
            System.out.println(Color.warning + "Invalid number of player inserted!" + Color.reset);
            printStatus();
            return;
        }

        try {
            check.checkCreateLobby(numOfPlayers);
            view.createLobby(numOfPlayers);
            semaphore.acquire();
        } catch (LobbyDoesNotExistException | NicknameAlreadyTakenException | FullLobbyException |
                 CannotJoinMultipleLobbiesException ignored) {
        } catch (ClassNotFoundException | UnexistentUserException | IOException e) {
            view.removeObserver(this);
            Thread.currentThread().interrupt();
        }

    }

    /**
     * Method to join a lobby already created
     * @param in                        input inserted by the user (supposed to be ID of the lobby or -1)
     * @throws InterruptedException     throws this exception if thread is interrupted
     */
    private void chooseLobby(String in) throws InterruptedException {
        if (view.getID() != null) {
            System.out.println(Color.warning + "\nCannot create a lobby while in another lobby!!" + Color.reset);
            return;
        }
        if (!isNumeric(in)) {
            System.out.println(Color.warning + "Numeric value required!!" + Color.reset);
            printStatus();
            return;
        }

        int ID = Integer.parseInt(in);
        if (ID == -1) {
            state = TUIState.MENU;
            printStatus();
            return;
        }

        try {
            check.checkJoinLobby(ID);
            view.joinLobby(ID);
            semaphore.acquire();

        } catch (NicknameAlreadyTakenException ignored) {
        } catch (CannotJoinMultipleLobbiesException e) {
            System.out.println(Color.warning + "Cannot join multiple lobbies!!" + Color.reset);
            printStatus();

        } catch (FullLobbyException e) {
            System.out.println(Color.warning + "Lobby #" + ID + " is full. Cannot join!!" + Color.reset);
            printStatus();

        } catch (LobbyDoesNotExistException e) {
            System.out.println(Color.warning + "Lobby #" + ID + " does not exist!!" + Color.reset);
            printStatus();

        } catch (IOException | ClassNotFoundException | UnexistentUserException e) {
            view.removeObserver(this);
            Thread.currentThread().interrupt();
        }
    }

    // ~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~
    // In Lobby methods:
    // ~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~

    /**
     * Method for pawn selection
     * @param color                     input inserted by the user (supposed to be color of pawn chosen)
     * @throws InterruptedException     throws this exception if thread is interrupted
     */
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

            } catch (GameAlreadyStartedException | LobbyDoesNotExistException | GameDoesNotExistException ignored) {
            } catch (PawnAlreadyTakenException e) {
                System.out.print(cli + "Pawn already taken!\n");
                printStatus();

            } catch (IOException | UnexistentUserException e) {
                view.removeObserver(this);
                Thread.currentThread().interrupt();
            }
        } else {
            System.out.println(Color.warning + "Color chosen does not exist!" + Color.reset);
            printStatus();
        }

    }

    /**
     * In lobby actions
     * @param in                        input inserted by the user
     */
    private void chooseInLobbyAction(String in) {
        if (!isNumeric(in)) {
            System.out.println(Color.warning + "Numeric value required!!" + Color.reset);
            return;
        }
        int choice = Integer.parseInt(in);
        switch (choice) {
            case 1 -> {
                chatState = ChatState.SELECT_CHAT;
                printStatus();
            }
            case 2 -> {
                try {
                    quitLobby();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
            default -> {
                System.out.println(Color.warning + "Invalid choice!!\n" + Color.reset);
                printStatus();
            }
        }
    }

    /**
     * Method for chat actions
     * @param in    input inserted by the user
     */
    private void chat(String in) {
        switch (chatState) {
            case null -> {}
            case SELECT_CHAT -> {
                int desiredChat;

                if (isNumeric(in)) desiredChat = Integer.parseInt(in);
                else {
                    System.out.println(Color.warning + "Numeric value required!!" + Color.reset);
                    printStatus();
                    return;
                }

                if(desiredChat==-1){
                    chatState = null;
                    printStatus();
                    return;
                }

                System.out.println(cli + "Opening chat...");
                if (desiredChat < 1 || desiredChat > getOtherPlayers().size() + 1) {
                    System.out.println(Color.warning + "Invalid choice!!\n" + Color.reset);
                    printStatus();
                    return;
                }

                chatState = ChatState.SEND_MESSAGE;
                ChatState.SEND_MESSAGE.setNum(desiredChat);
                printStatus();
            }

            case SEND_MESSAGE -> {
                if (in.equalsIgnoreCase("quit chat")) {
                    chatState = null;
                    printStatus();
                    return;
                }

                Message msg;
                int chat = ChatState.SEND_MESSAGE.getNum();

                try {
                    if (chat == 1) {
                        msg = new Message(in, view.getPlayer(), null, true);
                        view.sendMessage(msg);
                    } else {
                        ArrayList<Player> players = getOtherPlayers();
                        Player player = players.get(chat - 2);

                        msg = new Message(in, view.getPlayer(), player, false);
                        view.sendMessage(msg);
                    }
                } catch (LobbyDoesNotExistException | GameDoesNotExistException ignored) {
                } catch (IOException | UnexistentUserException | PlayerChatMismatchException e) {
                    view.removeObserver(this);
                    Thread.currentThread().interrupt();
                }
            }
        }
    }

    /**
     * Method for leaving lobby the player is currently in
     * @throws InterruptedException throws this exception if thread is interrupted
     */
    private void quitLobby() throws InterruptedException {
        try {
            check.checkLeaveLobby();
        } catch (NotConnectedToLobbyException e) {
            System.out.println(Color.warning + "Not connected to a lobby!!" + Color.reset);
            printStatus();
        }

        try {
            view.leaveLobby();
            semaphore.acquire();
        } catch (GameAlreadyStartedException e) {
            System.out.print(cli + "The game is already started! Cannot leave the lobby!");
            printStatus();
        } catch (LobbyDoesNotExistException | NicknameAlreadyTakenException | GameDoesNotExistException ignored) {
        } catch (IOException | FullLobbyException | ClassNotFoundException | UnexistentUserException e) {
            view.removeObserver(this);
            Thread.currentThread().interrupt();
        }
    }

    // ~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~
    // In Game methods:
    // ~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~

    /**
     * Method for choosing side of starter card and placing it on field
     * @param choice                    input inserted by the user (supposed to be card's side)
     * @throws InterruptedException     throws this exception if thread is interrupted
     */
    private void placeStarterCard(String choice) throws InterruptedException {
        if (view.getHand().getSize() == 0 || !view.getHand().getCard(0).getClass().equals(StarterCard.class)) return;
        StarterCard card = (StarterCard) view.getHand().getCard(0);

        CardSide side = switch (choice.toUpperCase()) {
            case "FRONT", "F" -> CardSide.FRONT;
            case "BACK", "B" -> CardSide.BACK;
            default -> null;
        };
        if (side == null) {
            System.out.println(Color.warning + "Invalid choice!!\n" + Color.reset);
            printStatus();
            return;
        }

        if (side.equals(CardSide.BACK)) {
            card.flip();
        }

        try {
            view.chooseCardSide(side);
            semaphore.acquire();
        } catch (IOException | GameDoesNotExistException | EmptyDeckException | HandIsFullException |
                 UnexistentUserException | WrongGamePhaseException ignored) {}
    }

    /**
     * Method for selecting secret goal
     * @param choice                    input inserted by the user (supposed to be ID of the chosen goal)
     * @throws InterruptedException     throws this exception if thread is interrupted
     */
    private void chooseSecretGoal(String choice) throws InterruptedException {
        if (view.getSecretGoal() != null) return;

        if (!isNumeric(choice)) {
            System.out.println(Color.warning + "Numeric value required!!" + Color.reset);
            printStatus();
            return;
        }
        int id = Integer.parseInt(choice);

        try {
            view.chooseSecretGoal(id);
        } catch (IOException | UnexistentUserException e) {
            throw new RuntimeException(e);
        } catch (WrongGamePhaseException | GameDoesNotExistException ignored) {
        } catch (IllegalGoalChosenException e) {
            System.out.print(Color.warning + "Invalid choice!!\n" + Color.reset);
            printStatus();
        }
    }

    /**
     * In game actions (when it's not player's turn)
     * @param in    input inserted by the user
     */
    private void chooseInGameAction(String in) {
        if (!isNumeric(in)) {
            System.out.println(Color.warning + "Numeric value required!!" + Color.reset);
            printStatus();
            return;
        }
        int action = Integer.parseInt(in);

        switch (action) {
            case 1 -> {
                printer.printGoal(view.getCommonGoal1());
                printer.printGoal(view.getCommonGoal2());
                printer.printGoal(view.getSecretGoal());
                printer.printField(view.getNickname());
                printStatus();
            }
            case 2 -> {
                for (Player p : getOtherPlayers()) {
                    if (!p.getNickname().equals(view.getNickname())) {
                        printer.printField(p.getNickname());
                    }
                }
                printStatus();
            }
            case 3 -> {
                chatState = ChatState.SELECT_CHAT;
                printStatus();
            }
            case 4 -> {
                try {
                    quitLobby();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
            default -> {
                System.out.println(Color.warning + "Invalid choice!!\n" + Color.reset);
                printStatus();
            }
        }
    }

    /**
     * Method for selecting and playing card from hand to field
     * @param in        input inserted by the user
     */
    private void playCard(String in) {

        switch (actionState) {
            case PLAY_SELECT_CARD -> {
                if (in.equalsIgnoreCase("f") || in.equalsIgnoreCase("flip")) {
                    for (int i = 0; i < view.getHand().getSize(); i++) {
                        view.getHand().getCard(i).flip();
                        if(CardSide.FRONT.equals(side)) side=CardSide.BACK;
                        else side=CardSide.FRONT;
                    }
                    printStatus();
                } else {

                    if (!isNumeric(in)) {
                        System.out.println(Color.warning + "Numeric value required!!" + Color.reset);
                        printStatus();
                        return;
                    }
                    int pos = Integer.parseInt(in);

                    if (pos < 0 || pos >= view.getHand().getSize()) {
                        System.out.println(Color.warning + "Invalid choice!!\n" + Color.reset);
                        printStatus();
                    } else {
                        actionState = ActionState.PLAY_SELECT_COORDS;
                        ActionState.PLAY_SELECT_COORDS.setNum(pos);
                        printStatus();
                    }
                }
            }

            case PLAY_SELECT_COORDS -> {
                String X;
                String Y;
                List<String> coords = Arrays.asList(in.split("\\s+"));
                if ((long) coords.size() != 2) {
                    System.out.println(Color.warning + "Wrong format inserted!!" + Color.reset);
                    printStatus();
                    return;
                }
                X = coords.getFirst();
                Y = coords.getLast();

                if (!isNumeric(X) || !isNumeric(Y)) {
                    System.out.println(Color.warning + "Numeric value required!!" + Color.reset);
                    printStatus();
                    return;
                }
                int x = Integer.parseInt(X);
                int y = Integer.parseInt(Y);
                int pos = ActionState.PLAY_SELECT_COORDS.getNum();

                Coords position = new Coords(x, y);

                try {
                    check.checkPlayCard(pos, position);

                    view.playCard(pos, position, side);
                    semaphore.acquire();

                    for (int i = 0; i < view.getHand().getSize(); i++) {
                        if (view.getHand().getCard(i).getSide().equals(CardSide.BACK)) view.getHand().getCard(i).flip();
                    }
                    side=CardSide.FRONT;

                } catch (IllegalActionException | NotYourTurnException | LobbyDoesNotExistException |
                         GameDoesNotExistException ignored) {
                } catch (IllegalMoveException e) {
                    if (e instanceof OccupiedCoordsException) {
                        System.out.format(cli + "Coords (%d, %d) already occupied:\n", x, y);
                        printer.printCard(view.getMyField().getMatrix().get(position));
                        actionState = ActionState.PLAY_SELECT_CARD;
                        printStatus();
                    }
                    if (e instanceof RequirementsNotSatisfiedException) {
                        System.out.println(Color.warning + "You don't have enough resources to play this card!!");
                        actionState = ActionState.PLAY_SELECT_CARD;
                        printStatus();
                    }
                    if (e instanceof UnreachablePositionException) {
                        System.out.println(Color.warning + "Invalid position!!");
                        actionState = ActionState.PLAY_SELECT_CARD;
                        printStatus();
                    }
                } catch (IOException | InterruptedException | UnexistentUserException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }

    /**
     * Method for drawing card
     * @param choice                    where the player wants to draw
     * @throws InterruptedException     throws this exception if thread is interrupted
     */
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
            printStatus();
            return;
        }

        try {
            view.drawCard(type);
            semaphore.acquire();
            actionState = ActionState.PLAY_SELECT_CARD;
        } catch (IllegalActionException | HandIsFullException | EmptyBufferException | NotYourTurnException |
                 LobbyDoesNotExistException | GameDoesNotExistException ignored) {
        } catch (EmptyDeckException e) {
            System.out.println(Color.warning + "Cannot draw from this deck. Reason: empty!!" + Color.reset);
            printStatus();
        } catch (IOException | UnexistentUserException e) {
            throw new RuntimeException(e);
        }
    }
}