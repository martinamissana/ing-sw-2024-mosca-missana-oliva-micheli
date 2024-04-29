package it.polimi.ingsw.view.CLI;

import it.polimi.ingsw.controller.Controller;
import it.polimi.ingsw.model.exceptions.FullLobbyException;
import it.polimi.ingsw.model.exceptions.LobbyDoesNotExistsException;
import it.polimi.ingsw.model.exceptions.NicknameAlreadyTakenException;
import it.polimi.ingsw.model.game.GameHandler;
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
        System.out.println("\tWelcome to \"Codex Naturalis\"");
    }

    public Player askLogin() {
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
            }
        } while (!availableUsername);

        System.out.println(cli + "Hello " + you.getNickname() + "!");
        return you;
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

        } catch (FullLobbyException | LobbyDoesNotExistsException | NicknameAlreadyTakenException ignored) {}
    }

    public void waiting(Player you) {
        int choice;
        System.out.println(cli + "Waiting for players..." + cli + "1. Set pawn color" + cli + "2. Send message" + cli + "3. Quit lobby");
        choice = input.nextInt();

        switch (choice) {
            case 1 -> setPawnColor(you);
            case 2 -> sendMessage(you);
            case 3 -> quitLobby(you);
        }
    }

    public void setPawnColor(Player you) {

    }

    public void sendMessage(Player you) {

    }

    public void quitLobby(Player you) {

    }
}

