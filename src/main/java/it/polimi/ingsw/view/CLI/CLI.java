package it.polimi.ingsw.view.CLI;

import it.polimi.ingsw.controller.Controller;
import it.polimi.ingsw.controller.exceptions.GameAlreadyStartedException;
import it.polimi.ingsw.model.exceptions.*;
import it.polimi.ingsw.model.game.GameHandler;
import it.polimi.ingsw.model.game.Lobby;
import it.polimi.ingsw.model.player.Pawn;
import it.polimi.ingsw.model.player.Player;

import java.io.IOException;
import java.util.Scanner;

public class CLI {
    private static GameHandler gh;
    private Controller controller;
    private final Scanner input = new Scanner(System.in);
    public static final String warningColor = "\u001B[31m";
    public static final String reset = "\u001B[0m";
    public static final String cli = "\u001B[38;2;255;165;0m" + "\n[+] " + "\u001B[0m";
    public static final String user = "\u001B[38;2;255;165;0m" + "\n[-] " + "\u001B[0m";
    public static final String green = "\u001B[32m";

    public void setGameHandler(GameHandler gh) {
        CLI.gh = gh;
    }

    public void setController(Controller controller) {
        this.controller = controller;
    }

    public void printHello() {
        // prepare for the ASCII ART!!!!!!
        System.out.println(cli + "Welcome to \"Codex Naturalis\"");
    }

    public void askLogin() {
        String username;
        boolean availableUsername = false;
        Player you = null;

        do {
            System.out.print(cli + "Insert username to proceed:" + user);
            username = input.nextLine();

            try {
                you = new Player(username);
                gh.addUser(you);
                availableUsername = true;
            } catch (NicknameAlreadyTakenException e) {
                System.out.println(warningColor + "\n[ERROR]: Username already taken!\n" + reset);
            } catch (IOException e) {
                throw new RuntimeException(e);
            } catch (ConnectionException e) {
                throw new RuntimeException(e);
            }
        } while (!availableUsername);

        System.out.println(cli + "Hello " + you.getNickname() + "!");
    }

    public void chooseAction(Player you) {
        int choice;
        do {
            System.out.print(cli + "What would you like to do?" + cli +
                             "1. Create a lobby and wait for other players to join" + cli +
                             "2. Join an already open lobby" + cli +
                             "3. Change username" + user);
            choice = input.nextInt();

            switch(choice) {
                case 1 -> createLobby(you);
                case 2 -> {
                    if (gh.getNumOfLobbies() == 0) {
                        System.out.println(warningColor + "\n[ERROR]: There are no open lobbies!\n" + reset);
                    } else {
                        if (!chooseLobby(you)) choice = -1;
                    }
                }
                case 3 -> {
                    gh.removeUser(you);
                    askLogin();
                }
                default -> System.out.println(warningColor + "\n[ERROR]: Invalid choice!!\n" + reset);
            }
        } while (choice != 1 && choice != 2 && choice != 3 || (choice == 2 && gh.getNumOfLobbies() == 0));
    }

    public void printOpenLobbies() {
        System.out.print(cli + "Open lobbies:");

        for (Integer i : gh.getLobbies().keySet()) {
            try {
                if (gh.getLobby(i).getPlayers().size() != gh.getLobby(i).getNumOfPlayers()) {
                    System.out.print(cli + "-> Lobby " + green + "#" + i + reset + ": " + gh.getLobby(i).getPlayers().size() + "/" + gh.getLobby(i).getNumOfPlayers() + " - [");

                    for (Player p : gh.getLobby(i).getPlayers()) {
                        if (p.equals(gh.getLobby(i).getPlayers().getLast())) {
                            System.out.print(p.getNickname() + "]");
                        } else {
                            System.out.print(p.getNickname() + " - ");
                        }
                    }
                } else System.out.print(cli + "-> Lobby " + warningColor + "#" + i + reset + ": FULL");
            } catch (LobbyDoesNotExistsException ignored) {}
        }
        System.out.println();
    }

    public void printActiveGames() {
        System.out.print(cli + "Games:");

        for (Integer i : gh.getActiveGames().keySet()) {
            try {
                System.out.print(cli + "-> Game " + green + "#" + i + reset + ": " + " - [");

                for (Player p : gh.getGame(i).getPlayers()) {
                    switch (p.getPawn()) {
                        case Pawn.BLUE -> System.out.print(blue + "● " + reset);
                        case Pawn.RED -> System.out.print(red + "● " + reset);
                        case Pawn.GREEN -> System.out.print(green + "● " + reset);
                        case Pawn.YELLOW -> System.out.print(yellow + "● " + reset);
                        default -> System.out.print("● " + reset);
                    }

                    if (p.equals(gh.getLobby(i).getPlayers().getLast())) {
                        System.out.print(p.getNickname() + "]");
                    } else {
                        System.out.print(p.getNickname() + " - ");
                    }
                }
            } catch (GameDoesNotExistException | LobbyDoesNotExistsException ignored) {}
        }
        System.out.println();
    }

    private boolean chooseLobby(Player you) {
        boolean chosen = false;
        int lobbyID = -1;

        do {
            try {
                System.out.print("\n" + cli + "Which lobby do you want to join? (-1 to return to selection)" + user + "#");
                lobbyID = input.nextInt();

                if (lobbyID == -1) return false;
                controller.joinLobby(you, lobbyID);
                chosen = true;

            } catch (LobbyDoesNotExistsException | FullLobbyException | NicknameAlreadyTakenException | IOException e) {
                if (e.getClass().getName().equals("it.polimi.ingsw.model.exceptions.LobbyDoesNotExistsException")) System.out.println(warningColor + "\n[ERROR]: Lobby with ID \"#"+ lobbyID + "\" does not exist!!\n" + reset);
                else if(e.getClass().getName().equals("it.polimi.ingsw.model.exceptions.FullLobbyException")) System.out.println(warningColor + "\n[ERROR]: Lobby full. Cannot join this lobby!!\n" + reset);
            }
        } while (!chosen);

        try {
            int remaining = gh.getLobby(lobbyID).getNumOfPlayers() - gh.getLobby(lobbyID).getPlayers().size();
            switch (remaining) {
                case 0 -> System.out.println(cli + "The lobby has the number of players required. Game is now starting...\n");
                case 1 -> System.out.println(cli + "Entered lobby #" + lobbyID + "!" + cli + "Waiting for last player to join...\n");
                default -> System.out.println(cli + "Entered lobby #" + lobbyID + "!" + cli + "Waiting for " + remaining + " players to join...\n");
            }
        }
        catch (LobbyDoesNotExistsException ignored) {}
        return true;
    }

    private void createLobby(Player you) {
        try {
            int numOfPlayers;
            do {
                System.out.print(cli + "Insert number of players: [2][3][4]" + user);
                numOfPlayers = input.nextInt();

                if (numOfPlayers < 2 || numOfPlayers > 4)
                    System.out.print(warningColor + "\n[ERROR]: Invalid number of players inserted!!\n" + reset);
            } while (numOfPlayers < 2 || numOfPlayers > 4);

            controller.createLobby(numOfPlayers, you);
            int lobbyID = gh.getNumOfLobbies() - 1;
            System.out.print(cli + "Lobby successfully created! ID: " + lobbyID);
            waiting(lobbyID, you);

        } catch (LobbyDoesNotExistsException ignored) {}
    }

    public void waiting(Integer lobbyID, Player you) throws LobbyDoesNotExistsException {
        int choice;
        System.out.println(cli + "Waiting for players..." + cli + "1. Set pawn color" + cli + "2. Send message" + cli + "3. Quit lobby");
        choice = input.nextInt();

        switch (choice) {
            case 1 -> setPawnColor(lobbyID, you);
            case 2 -> sendMessage(lobbyID, you);
            case 3 -> quitLobby(lobbyID, you);
        }
    }

    public static final String blue = "\u001B[34m";
    public static final String yellow = "\u001B[93m";
    public static final String red = "\u001B[31m";

    public void printAvailablePawns(int lobbyID) throws LobbyDoesNotExistsException {
        Lobby lobby = gh.getLobby(lobbyID);

        System.out.print(cli + "Available pawns: ");
        for (Pawn p : lobby.getPawnBuffer().getPawnList()) {
            switch (p) {
                case Pawn.BLUE -> System.out.print(blue + "● " + reset);
                case Pawn.RED -> System.out.print(red + "● " + reset);
                case Pawn.GREEN -> System.out.print(green + "● " + reset);
                case Pawn.YELLOW -> System.out.print(yellow + "● " + reset);
            }
        }
        System.out.println();
    }

    public void printPlayers(int lobbyID) throws LobbyDoesNotExistsException {
        Lobby lobby = gh.getLobby(lobbyID);

        System.out.print(cli + "Players (lobby " + green + "#" + lobbyID + reset + "): " + lobby.getPlayers().size() + "/" + lobby.getNumOfPlayers());
        for (Player p : lobby.getPlayers()) {
            switch (p.getPawn()) {
                case Pawn.BLUE -> System.out.print(cli + blue + "● " + reset);
                case Pawn.RED -> System.out.print(cli + red + "● " + reset);
                case Pawn.GREEN -> System.out.print(cli + green + "● " + reset);
                case Pawn.YELLOW -> System.out.print(cli + yellow + "● " + reset);
                case null, default -> System.out.print(cli + "● " + reset);
            }
            System.out.print(p.getNickname());
        }
        System.out.println();
    }

    public void setPawnColor(Integer lobbyID, Player you) throws LobbyDoesNotExistsException {
        Pawn pawn;

        do {
            printAvailablePawns(lobbyID);
            System.out.print(cli + "Which pawn do you want to choose? (");
            for (int i = 0; i < gh.getLobby(lobbyID).getPawnBuffer().getPawnList().size(); i++) {
                if (i != gh.getLobby(lobbyID).getPawnBuffer().getPawnList().size() - 1) System.out.print(gh.getLobby(lobbyID).getPawnBuffer().getPawnList().get(i) + " - ");
                else System.out.print(gh.getLobby(lobbyID).getPawnBuffer().getPawnList().get(i) + ")" + user);
            }

            pawn = switch (input.nextLine().toUpperCase()) {
                case "RED", "R" -> Pawn.RED;
                case "GREEN", "G", "SHREK", "ANTONIO" -> Pawn.GREEN;    // Easter egg
                case "BLUE", "B", "GIORGIO" -> Pawn.BLUE;               // Easter egg
                case "YELLOW", "Y", "BANANA" -> Pawn.YELLOW;            // Easter egg
                default -> null;
            };

            if(pawn != null) {
                try {
                    controller.choosePawn(lobbyID, you, pawn);
                } catch (PawnAlreadyTakenException e) {
                    System.out.print(cli + "Pawn already taken!\n");
                    pawn = null;
                }
            } else System.out.print(cli + "Color chosen does not exist!\n");
        } while(pawn == null);
    }

    public void sendMessage(int lobbyID, Player you) {

    }

    public void quitLobby(int lobbyID, Player you) throws LobbyDoesNotExistsException {
        System.out.print("\n" + cli + "Trying leaving lobby " + green + "#" + lobbyID + reset + "...");
        try {
            controller.leaveLobby(you, lobbyID);
        } catch (GameAlreadyStartedException ignored) {
            System.out.print(cli + "The game is already started! Cannot leave the lobby!");
            return;
        }

        System.out.print(cli + "Successfully leaved the lobby\n");
    }
}

