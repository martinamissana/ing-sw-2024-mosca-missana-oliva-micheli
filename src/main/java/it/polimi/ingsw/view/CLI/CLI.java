package it.polimi.ingsw.view.CLI;

import it.polimi.ingsw.controller.exceptions.GameAlreadyStartedException;
import it.polimi.ingsw.model.exceptions.*;
import it.polimi.ingsw.model.game.Lobby;
import it.polimi.ingsw.model.player.Pawn;
import it.polimi.ingsw.model.player.Player;
import it.polimi.ingsw.view.View;

import java.io.IOException;
import java.util.HashMap;
import java.util.Scanner;

public class CLI implements Runnable {
    private final View view;
    private final CLIGame gameAction;
    private final Scanner input = new Scanner(System.in);
    private static final String warningColor = "\u001B[31m";
    private static final String reset = "\u001B[0m";
    private static final String cli = "\u001B[38;2;255;165;0m" + "\n[+] " + "\u001B[0m";
    private static final String user = "\u001B[38;2;255;165;0m" + "\n[-] " + "\u001B[0m";
    private static final String green = "\u001B[32m";
    private static final String blue = "\u001B[34m";
    private static final String yellow = "\u001B[93m";
    private static final String red = "\u001B[31m";

    public CLI(View view) {
        this.view = view;
        this.gameAction = new CLIGame(view);
    }

    @Override
    public void run() {
        printHello();

        try {
            Thread.sleep(1500);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        System.out.println("\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n");

        String s;
        do {
            int ID = -1;
            int lobbyAction = chooseAction();
            switch (lobbyAction) {
                case 1 ->  ID = createLobby();
                case 2 -> {
                    printOpenLobbies();
                    ID = chooseLobby();
                }
            }
            if (ID != -1) {
                lobbyAction = waiting();
                switch(lobbyAction) {
                    case 1 -> setPawnColor(ID);
                    case 2 -> sendMessage();
                    case 3 -> quitLobby();
                }
            } else System.out.println(cli + "Returning to selection\n");
            s = input.nextLine();
        } while (!s.equals("quit"));
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
        System.out.println("   ..        .-++###########++-.            ...#..##..####.#..####.#...####-#+#+###+.+#--.      ");

        // System.out.println(cli + "Welcome to \"Codex Naturalis\"");
    }

    private int chooseAction() {
        int choice;
        do {
            System.out.print(cli + "What would you like to do?" + cli +
                             "1. Create a lobby and wait for other players to join" + cli +
                             "2. Join an already open lobby" + user);
            choice = input.nextInt();

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
                System.out.print(cli + "-> Lobby " + green + "#" + i + reset + ": " + lobbies.get(i).getPlayers().size() + "/" + lobbies.get(i).getNumOfPlayers());

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

    private int chooseLobby() {
        boolean chosen = false;
        int ID;
        HashMap<Integer, Lobby> lobbies = view.getLobbies();

        printOpenLobbies();
        do {
            System.out.print("\n" + cli + "Which lobby do you want to join? (-1 to return to selection)" + user + "#");
            ID = input.nextInt();
            if (ID == -1) return -1;

            try {
                view.joinLobby(ID);
                chosen = true;

            } catch (LobbyDoesNotExistsException e) {
                System.out.println(warningColor + "\n[ERROR]: Lobby with ID \"#" + ID + "\" does not exist!!\n" + reset);
            } catch (FullLobbyException e) {
                System.out.println(warningColor + "\n[ERROR]: Lobby full. Cannot join this lobby!!\n" + reset);
            } catch (NicknameAlreadyTakenException | IOException ignored) {}
        } while (!chosen);

        int remaining = lobbies.get(ID).getNumOfPlayers() - lobbies.get(ID).getPlayers().size();
        switch (remaining) {
            case 0 -> System.out.println(cli + "The lobby has the number of players required. Game is now starting...\n");
            case 1 -> System.out.println(cli + "Entered lobby #" + ID + "!" + cli + "Waiting for last player to join...\n");
            default -> System.out.println(cli + "Entered lobby #" + ID + "!" + cli + "Waiting for " + remaining + " players to join...\n");
        }

        return ID;
    }

    private int createLobby() {
        int ID = -1;
        try {
            int numOfPlayers;
            do {
                System.out.print(cli + "Insert number of players: [2][3][4]" + user);
                numOfPlayers = input.nextInt();

                if (numOfPlayers < 2 || numOfPlayers > 4)
                    System.out.print(warningColor + "\n[ERROR]: Invalid number of players inserted!!\n" + reset);
            } while (numOfPlayers < 2 || numOfPlayers > 4);

            view.createLobby(numOfPlayers);
            ID = view.getLobbies().size() - 1;      // TODO: Change how to take ID
            System.out.print(cli + "Lobby successfully created! ID: " + ID);

        } catch (LobbyDoesNotExistsException | IOException ignored) {}
        return ID;
    }

    private int waiting() {
        int choice;
        System.out.println(cli + "Waiting for players..." + cli + "1. Set pawn color" + cli + "2. Send message" + cli + "3. Quit lobby");
        choice = input.nextInt();

        return choice;
    }

    private void printAvailablePawns() {
        HashMap<Player, Pawn> pawns = view.getPawns();

        System.out.print(cli + "Available pawns: ");
        for (Pawn p : Pawn.values()) {
            if (!pawns.containsValue(p)) {
                switch (p) {
                    case Pawn.BLUE -> System.out.print(blue + "● " + reset);
                    case Pawn.RED -> System.out.print(red + "● " + reset);
                    case Pawn.GREEN -> System.out.print(green + "● " + reset);
                    case Pawn.YELLOW -> System.out.print(yellow + "● " + reset);
                }
            }
        }
        System.out.println();
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
                case null, default -> System.out.print(cli + "● " + reset);
            }
            System.out.print(p.getNickname());
        }
        System.out.println();
    }

    private void setPawnColor(Integer ID) {
        Pawn pawn;

        do {
            printAvailablePawns();
            System.out.print(cli + "Which pawn do you want to choose?");

            pawn = switch (input.nextLine().toUpperCase()) {
                case "RED", "R", "SILVIA" -> Pawn.RED;                      // Easter egg
                case "GREEN", "G", "SHREK", "ANTONIO" -> Pawn.GREEN;        // Easter egg
                case "BLUE", "B", "GIORGIO" -> Pawn.BLUE;                   // Easter egg
                case "YELLOW", "Y", "BANANA", "MARTINA" -> Pawn.YELLOW;     // Easter egg
                default -> null;
            };

            if(pawn != null) {
                try {
                     view.choosePawn(ID, pawn);
                } catch (PawnAlreadyTakenException e) {
                    System.out.print(cli + "Pawn already taken!\n");
                    pawn = null;
                } catch (Exception ignored) {}
            } else System.out.print(cli + "Color chosen does not exist!\n");
        } while(pawn == null);
    }

    private void sendMessage() {}

    private void quitLobby() {
        System.out.print("\n" + cli + "Trying leaving lobby");
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
        } System.out.println();

        try {
            view.leaveLobby();
            System.out.print(cli + "Successfully left the lobby\n");
        } catch (GameAlreadyStartedException e) {
            System.out.print(cli + "The game is already started! Cannot leave the lobby!");
        } catch (LobbyDoesNotExistsException ignored) {}
        catch (IOException e) {
            throw new RuntimeException();
        }
    }
}

