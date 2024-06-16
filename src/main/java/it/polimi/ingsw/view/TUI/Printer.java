package it.polimi.ingsw.view.TUI;

import it.polimi.ingsw.model.card.*;
import it.polimi.ingsw.model.commonItem.CornerStatus;
import it.polimi.ingsw.model.commonItem.ItemBox;
import it.polimi.ingsw.model.commonItem.Kingdom;
import it.polimi.ingsw.model.commonItem.Resource;
import it.polimi.ingsw.model.deck.Deck;
import it.polimi.ingsw.model.deck.DeckBuffer;
import it.polimi.ingsw.model.deck.DeckBufferType;
import it.polimi.ingsw.model.deck.DeckType;
import it.polimi.ingsw.model.exceptions.IllegalMoveException;
import it.polimi.ingsw.model.game.CardsPreset;
import it.polimi.ingsw.model.game.Lobby;
import it.polimi.ingsw.model.goal.DiagonalGoal;
import it.polimi.ingsw.model.goal.Goal;
import it.polimi.ingsw.model.goal.L_ShapeGoal;
import it.polimi.ingsw.model.goal.ResourceGoal;
import it.polimi.ingsw.model.player.*;
import it.polimi.ingsw.view.View;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class Printer {
    private final View view;
    private final String cli = Color.console + "\n[+] " + Color.reset;
    
    public Printer(View view) {
        this.view = view;
    }
    
    public void printLogo() {
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

    // ~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~
    // Main Menu printing methods:
    // ~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~

    public void printOpenLobbies() {
        System.out.print(cli + "Open lobbies:");
        HashMap<Integer, Lobby> lobbies = view.getLobbies();

        for (Integer i : lobbies.keySet()) {
            if (!lobbies.get(i).getPlayers().isEmpty()) {
                if (lobbies.get(i).getPlayers().size() != lobbies.get(i).getNumOfPlayers()) {
                    System.out.print(cli + "-> Lobby " + Color.green + "#" + i + Color.reset + ": " + lobbies.get(i).getPlayers().size() + "/" + lobbies.get(i).getNumOfPlayers() + " - [");

                    for (Player p : lobbies.get(i).getPlayers()) {
                        if (p.equals(lobbies.get(i).getPlayers().getLast())) {
                            System.out.print(p.getNickname() + "]");
                        } else {
                            System.out.print(p.getNickname() + " - ");
                        }
                    }
                } else if (!lobbies.get(i).getPlayers().isEmpty())
                    System.out.print(cli + "-> Lobby " + Color.warning + "#" + i + Color.reset + ": FULL");
            }
        }
        System.out.println();
    }

    public void printRules() {
        System.out.println("""
            
            \t ,-----. ,-----. ,------.  ,------.,--.   ,--.   ,--.  ,--.          ,--.                         ,--.,--.      \s
            \t'  .--./'  .-.  '|  .-.  \\ |  .---' \\  `.'  /    |  ,'.|  | ,--,--.,-'  '-.,--.,--.,--.--. ,--,--.|  |`--' ,---.\s
            \t|  |    |  | |  ||  |  \\  :|  `--,   .'    \\     |  |' '  |' ,-.  |'-.  .-'|  ||  ||  .--'' ,-.  ||  |,--.(  .-'\s
            \t'  '--'\\'  '-'  '|  '--'  /|  `---. /  .'.  \\    |  | `   |\\ '-'  |  |  |  '  ''  '|  |   \\ '-'  ||  ||  |.-'  `)
            \t `-----' `-----' `-------' `------''--'   '--'   `--'  `--' `--`--'  `--'   `----' `--'    `--`--'`--'`--'`----'\s
            
                                                             ONLINE GAME CREATED BY:
                                                                 Micheli Giorgio
                                                                 Missana Martina
                                                                  Mosca Silvia
                                                                  Oliva Antonio
            
            """);

            String text = """
            PLAYING THE GAME:
            A game consists of several rounds that players take one after
            another in a clockwise direction. In turn, the player must perform
            the following two actions in order:
            
            1. Play a card from their hand
            2. Draw a card
            
            
            PLAY CARD:
            The player chooses one of the 3 cards in their hand and places it
            in their play area according to the illustration and respecting these
            2 rules:
            
            - PLACEMENT RULE: The card must cover one or several visible corners
                of cards already present in their play area. It cannot cover more
                than one corner of the same card. Only the card already present in
                the play area may contain the necessary visible corners.
                
                Important: if the player does not like the front of the card,
                they can play it face-down.
            
            - GOLD CARDS RULE: To place the Gold cards, the player must possess
                the indicated resources visible in their play area. The resources
                must be visible before they place the card, but they may be covered
                afterwards.
                
            SCORING POINTS:
            If the card placed allows the player to score points, they must be
            immediately added to the Score track. The cards are detailed later.
            
            
            DRAW CARD:
            After placing a card in their play area, the player chooses a new
            card either from the four cards face-up in the center of the table
            (a new card is then revealed to replace the one just taken), or the
            first card from one of the two decks.
            
            
            END OF GAME:
            The end of the game is triggered when a player reaches 20 points
            (or more) or if the two decks are empty. Players finish the round
            and then each have a last turn before the game ends.
            
            Each player counts the points from the Objective cards (2 common
            objectives + the secret objective) and adds them to the points
            already scored on the Score track.
            
            Important: When counting the objective points, each Resource or
            Gold card in the play area may only be counted once
            
            The player with the most points wins the game. In case of a tie,
            the player with the most Objective card points wins. If there is
            still a tie, the players share the victory.
            """;

        for (int i = 0; i < text.length(); i++) {
            System.out.print(text.charAt(i));
            try {
                Thread.sleep(1);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }

    // ~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~
    // In lobby printing methods:
    // ~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~

    public void printRemainingPlayers() {
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

    public void printAvailablePawns() {
        ArrayList<Pawn> pawns = new ArrayList<>();
        for (Player p : view.getLobbies().get(view.getID()).getPlayers()) {
            if (p.getPawn() != null) pawns.add(p.getPawn());
        }

        System.out.print(cli + "Available pawns: ");
        for (Pawn p : Pawn.values()) {
            if (!pawns.contains(p)) {
                switch (p) {
                    case Pawn.BLUE -> System.out.print(Color.blue + "● " + Color.reset);
                    case Pawn.RED -> System.out.print(Color.red + "● " + Color.reset);
                    case Pawn.GREEN -> System.out.print(Color.green + "● " + Color.reset);
                    case Pawn.YELLOW -> System.out.print(Color.yellow + "● " + Color.reset);
                }
            }
        }
    }

    public void printPlayers(Integer ID) {
        Lobby lobby = view.getLobbies().get(ID);
        Pawn pawn;

        System.out.print(cli + "Players (lobby " + Color.green + "#" + ID + Color.reset + "): " + lobby.getPlayers().size() + "/" + lobby.getNumOfPlayers());
        for (Player p : lobby.getPlayers()) {
            if (p.equals(view.getPlayer())) pawn = view.getPawn();
            else pawn = p.getPawn();

            switch (pawn) {
                case Pawn.BLUE -> System.out.print(cli + Color.blue + "● ");
                case Pawn.RED -> System.out.print(cli + Color.red + "● ");
                case Pawn.GREEN -> System.out.print(cli + Color.green + "● ");
                case Pawn.YELLOW -> System.out.print(cli + Color.yellow + "● ");
                case null -> System.out.print(cli + "● ");
            }
            System.out.print(p.getNickname() + Color.reset);
        }
        System.out.println();
    }

    // ~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~
    // Game printing methods:
    // ~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~

    public void printGameArea() {
        System.out.print("\n" + cli + "Common goals:");
        printGoal(view.getCommonGoal1());
        printGoal(view.getCommonGoal2());
        System.out.println();
        printGoal(view.getSecretGoal());

        //printResources();

        Card next = view.getTopResourceCard();
        HashMap<DeckBufferType, DeckBuffer> cardSpaces = view.getDeckBuffers();

        // Printing resource deck + spaces
        if (next != null && next.getSide().equals(CardSide.FRONT)) next.flip();

        System.out.print(cli + "Resource Deck + card spaces:" + cli);
        if (next != null) System.out.print(printUpper(next) + "\t\t");
        else System.out.print("\t\t\t\t\t");
        if (cardSpaces.get(DeckBufferType.RES1).getCard() != null) System.out.print(printUpper(cardSpaces.get(DeckBufferType.RES1).getCard()) + "\t");
        else System.out.print("\t\t\t\t");
        if (cardSpaces.get(DeckBufferType.RES2).getCard() != null) System.out.print(printUpper(cardSpaces.get(DeckBufferType.RES2).getCard()) + cli);
        else System.out.print("\t\t\t\t");

        if (next != null) System.out.print(printMiddle(next) + "\t\t");
        else System.out.print("\t\t\t\t\t");
        if (cardSpaces.get(DeckBufferType.RES1).getCard() != null) System.out.print(printMiddle(cardSpaces.get(DeckBufferType.RES1).getCard()) + "\t");
        else System.out.print("\t\t\t\t");
        if (cardSpaces.get(DeckBufferType.RES2).getCard() != null) System.out.print(printMiddle(cardSpaces.get(DeckBufferType.RES2).getCard()) + cli);
        else System.out.print("\t\t\t\t");

        if (next != null) System.out.print(printLower(next) + "\t\t");
        else System.out.print("\t\t\t\t\t");
        if (cardSpaces.get(DeckBufferType.RES1).getCard() != null) System.out.print(printLower(cardSpaces.get(DeckBufferType.RES1).getCard()) + "\t");
        else System.out.print("\t\t\t\t");
        if (cardSpaces.get(DeckBufferType.RES2).getCard() != null) System.out.print(printLower(cardSpaces.get(DeckBufferType.RES2).getCard()) + cli);
        else System.out.print("\t\t\t\t");

        if (next != null) next.flip();

        // Printing golden deck + spaces
        next = view.getTopGoldenCard();
        if (next != null && next.getSide().equals(CardSide.FRONT)) next.flip();

        System.out.print(cli + "Golden Deck + card spaces:" + cli);
        if (next != null) System.out.print(printUpper(next) + "\t\t");
        else System.out.print("\t\t\t\t\t");
        if (cardSpaces.get(DeckBufferType.GOLD1).getCard() != null) System.out.print(printUpper(cardSpaces.get(DeckBufferType.GOLD1).getCard()) + "\t");
        else System.out.print("\t\t\t\t");
        if (cardSpaces.get(DeckBufferType.GOLD2).getCard() != null) System.out.print(printUpper(cardSpaces.get(DeckBufferType.GOLD2).getCard()) + cli);
        else System.out.print("\t\t\t\t");

        if (next != null) System.out.print(printMiddle(next) + "\t\t");
        else System.out.print("\t\t\t\t\t");
        if (cardSpaces.get(DeckBufferType.GOLD1).getCard() != null) System.out.print(printMiddle(cardSpaces.get(DeckBufferType.GOLD1).getCard()) + "\t");
        else System.out.print("\t\t\t\t");
        if (cardSpaces.get(DeckBufferType.GOLD2).getCard() != null) System.out.print(printMiddle(cardSpaces.get(DeckBufferType.GOLD2).getCard()) + cli);
        else System.out.print("\t\t\t\t");

        if (next != null) System.out.print(printLower(next) + "\t\t");
        else System.out.print("\t\t\t\t\t");
        if (cardSpaces.get(DeckBufferType.GOLD1).getCard() != null) System.out.print(printLower(cardSpaces.get(DeckBufferType.GOLD1).getCard()) + "\t");
        else System.out.print("\t\t\t\t");
        if (cardSpaces.get(DeckBufferType.GOLD2).getCard() != null) System.out.print(printLower(cardSpaces.get(DeckBufferType.GOLD2).getCard()) + cli);
        else System.out.print("\t\t\t\t");

        if (next != null) next.flip();
    }

    public void printField(String nickname) { // TODO: Add CornerToString()
        Field field = null;
        if (nickname.equals(view.getNickname())) field = view.getMyField();
        else {
            for (Player p : view.getLobbies().get(view.getID()).getPlayers()) {
                if (p.getNickname().equals(nickname)) field = view.getFields().get(p);
            }
        }
        if (field == null) return;

        // TODO: Remove when functioning:
//        Field field = new Field();
//        try {
//            Deck deck = new Deck(DeckType.RESOURCE);
//            Deck gd = new Deck(DeckType.GOLDEN);
//            StarterCard starterCard = CardsPreset.getStarterCards().get(CardsPreset.getStarterCards().size() - 2);
//            starterCard.flip();
//            field.addCard(starterCard);
//            field.addCard(deck.getCards().get(23), new Coords(0, 1));
//            field.addCard(deck.getCards().get(10), new Coords(0, 2));
//            field.addCard(deck.getCards().get(11), new Coords(1, 0));
//            field.addCard(deck.getCards().get(3), new Coords(2, 0));
//            field.addCard(deck.getCards().get(31), new Coords(0, -1));
//            field.addCard(deck.getCards().get(34), new Coords(1, -1));
//            field.addCard(deck.getCards().get(35), new Coords(2, -1));
//            field.addCard(deck.getCards().get(2), new Coords(-1, -1));
//            field.addCard(deck.getCards().get(6), new Coords(-2, -1));
//            field.addCard(deck.getCards().get(25), new Coords(3, -1));
//            ResourceCard resourceCard = deck.getCards().getFirst();
//            resourceCard.flip();
//            field.addCard(resourceCard, new Coords(-1, 0));

            // Case when it is not possible to play any card:
//            field.addCard(deck.getCards().get(31), new Coords(0, 1));
//            field.addCard(gd.getCards().get(38), new Coords(1, 1));
//            field.addCard(deck.getCards().get(22), new Coords(1, 0));
//            field.addCard(gd.getCards().get(36), new Coords(1, -1));
//            field.addCard(gd.getCards().get(28), new Coords(-1, 1));

//
//        } catch (IOException | IllegalMoveException e) {
//            throw new RuntimeException(e);
//        }


        int firstDiagonal = field.getMatrix().keySet().stream().mapToInt(c -> c.getX() + c.getY()).distinct().max().orElse(-80) + 1;
        int lastDiagonal = field.getMatrix().keySet().stream().mapToInt(c -> c.getX() + c.getY()).distinct().min().orElse(-80);
        int firstColumn = field.getMatrix().keySet().stream().mapToInt(c -> c.getX() - c.getY()).distinct().min().orElse(-80);
        int lastColumn = field.getMatrix().keySet().stream().mapToInt(c -> c.getX() - c.getY()).distinct().max().orElse(-80);

        if (firstDiagonal == -80 || firstColumn == -80 || lastDiagonal == -80 || lastColumn == -80) return;

        Coords firstCoordinate;
        if ((firstDiagonal + firstColumn) % 2 == 0) firstCoordinate = new Coords((int) Math.floor((firstDiagonal + firstColumn) / 2.0), (int) Math.floor((firstDiagonal - firstColumn) / 2.0));
        else firstCoordinate = new Coords((int) Math.floor((firstDiagonal + firstColumn) / 2.0), (int) Math.floor((firstDiagonal - firstColumn) / 2.0) + 1);
        firstColumn = firstCoordinate.getX() - firstCoordinate.getY();

        Coords lastCoordinate;
        if ((firstDiagonal + lastColumn) % 2 == 0) lastCoordinate = new Coords((int) Math.floor((firstDiagonal + lastColumn) / 2.0), (int) Math.floor((firstDiagonal - lastColumn) / 2.0));
        else lastCoordinate = new Coords((int) Math.floor((firstDiagonal + lastColumn) / 2.0) + 1, (int) Math.floor((firstDiagonal - lastColumn) / 2.0));
        lastColumn = lastCoordinate.getX() - lastCoordinate.getY();

        String cardColor;
        StringBuilder fieldBuilder = new StringBuilder();

        for (int diagonal = firstDiagonal; diagonal > lastDiagonal - 2; diagonal--) {
            for (Coords c = firstCoordinate; c.getX() != lastCoordinate.getX() + 1 && c.getY() != lastCoordinate.getY() - 1; c = new Coords(c.getX() + 1, c.getY() - 1)) {
                Card up_left = field.getMatrix().get(new Coords(c.getX(), c.getY() + 1));
                Card card = field.getMatrix().get(c);

                // Bottom part of first up-left card printed:
                if (c.getX() == firstCoordinate.getX() && firstCoordinate.getX() - firstCoordinate.getY() != firstColumn) {
                    if (up_left == null) fieldBuilder.append("        ");
                    else {
                        if (up_left instanceof CardBlock) cardColor = Color.black.label;
                        else cardColor = ItemsToColor(up_left.getKingdom());

                        if (up_left instanceof CardBlock) fieldBuilder.append(cardColor).append("   ").append(Color.reset);
                        else if (up_left.getCorner(CornerType.WEST) != null && !up_left.getCorner(CornerType.WEST).isCovered())
                            fieldBuilder.append(CornerToString(up_left, CornerType.WEST));

                        fieldBuilder.append(cardColor).append("     ").append(Color.reset);
                    }
                }

                // North corner printed:
                if (card == null) {
                    if (up_left == null) fieldBuilder.append("   ");
                    else if (up_left instanceof CardBlock) fieldBuilder.append(Color.black).append("   ").append(Color.reset);
                    else fieldBuilder.append(CornerToString(up_left, CornerType.SOUTH));

                } else if (card instanceof CardBlock) {
                    if (up_left == null || up_left instanceof CardBlock) fieldBuilder.append(Color.black).append("   ").append(Color.reset);
                    else if (up_left.getCorner(CornerType.SOUTH) == null) fieldBuilder.append(ItemsToColor(up_left.getKingdom())).append("   ").append(Color.reset);
                    else fieldBuilder.append(CornerToString(up_left, CornerType.SOUTH));

                } else {
                    if (up_left == null || up_left instanceof CardBlock || (up_left.getCorner(CornerType.SOUTH) != null && up_left.getCorner(CornerType.SOUTH).isCovered())) {
                        if (card.getCorner(CornerType.NORTH) != null) fieldBuilder.append(CornerToString(card, CornerType.NORTH));
                        else fieldBuilder.append(ItemsToColor(card.getKingdom())).append("   ").append(Color.reset);

                    } else if (up_left.getCorner(CornerType.SOUTH) != null && !up_left.getCorner(CornerType.SOUTH).isCovered()) {
                        fieldBuilder.append(CornerToString(up_left, CornerType.SOUTH));
                    } else fieldBuilder.append(ItemsToColor(up_left.getKingdom())).append("   ").append(Color.reset);
                }

                // Upper middle part printed:
                if (card != null && !(card instanceof CardBlock))
                    cardColor = ItemsToColor(card.getKingdom());
                else if (card != null) cardColor = Color.black.label;
                else cardColor = "";

                fieldBuilder.append(cardColor).append("     ").append(Color.reset);

                Card up_right = field.getMatrix().get(new Coords(c.getX() + 1, c.getY()));

                // East corner printed:
                if (card == null) {
                    if (up_right == null) fieldBuilder.append("   ");
                    else if (up_right instanceof CardBlock) fieldBuilder.append(Color.black).append("   ").append(Color.reset);
                    else fieldBuilder.append(CornerToString(up_right, CornerType.WEST));

                } else if (card instanceof CardBlock) {
                    if (up_right == null || up_right instanceof CardBlock) fieldBuilder.append(Color.black).append("   ").append(Color.reset);
                    else if (up_right.getCorner(CornerType.WEST) == null) fieldBuilder.append(ItemsToColor(up_right.getKingdom())).append("   ").append(Color.reset);
                    else fieldBuilder.append(CornerToString(up_right, CornerType.WEST));

                } else {
                    if (up_right == null || up_right instanceof CardBlock || (up_right.getCorner(CornerType.WEST) != null && up_right.getCorner(CornerType.WEST).isCovered())) {
                        if (card.getCorner(CornerType.EAST) != null) fieldBuilder.append(CornerToString(card, CornerType.EAST));
                        else fieldBuilder.append(ItemsToColor(card.getKingdom())).append("   ").append(Color.reset);

                    } else if (up_right.getCorner(CornerType.WEST) != null && !up_right.getCorner(CornerType.WEST).isCovered()) {
                        fieldBuilder.append(CornerToString(up_right, CornerType.WEST));
                    } else fieldBuilder.append(ItemsToColor(up_right.getKingdom())).append("   ").append(Color.reset);
                }

                // Lower part of up-right card printed:
                if (up_right != null && !(up_right instanceof CardBlock)) cardColor = ItemsToColor(up_right.getKingdom());
                else if (up_right != null) cardColor = Color.black.label;
                else cardColor = "";

                fieldBuilder.append(cardColor).append("     ").append(Color.reset);
            }
            fieldBuilder.append("\n");

            // Middle part:
            for (Coords c = firstCoordinate; c.getX() != lastCoordinate.getX() + 1 && c.getY() != lastCoordinate.getY() - 1; c = new Coords(c.getX() + 1, c.getY() - 1)) {
                Card card = field.getMatrix().get(c);

                if (c.getX() == firstCoordinate.getX() && firstCoordinate.getX() - firstCoordinate.getY() != firstColumn) fieldBuilder.append("        ");

                if (card instanceof CardBlock) cardColor = Color.black.label;
                else if (card != null) cardColor = ItemsToColor(card.getKingdom());
                else cardColor = "";

                if (card == null) {
                    fieldBuilder.append(cardColor).append("  ");
                    if (c.getX() >= 0 && c.getX() < 10) fieldBuilder.append("  ").append(c.getX());
                    else if (c.getX() > -10) fieldBuilder.append(" ").append(c.getX());
                    else fieldBuilder.append(c.getX());

                    fieldBuilder.append(" ");

                    if (c.getY() >= 0 && c.getY() < 10) fieldBuilder.append(c.getY()).append("  ");
                    else if (c.getY() > -10) fieldBuilder.append(c.getY()).append(" ");
                    else fieldBuilder.append(c.getY());

                    fieldBuilder.append("  ");
                } else if (!(card instanceof CardBlock) && card.getSide() == CardSide.BACK) {

                    StringBuilder permRes = new StringBuilder();
                    if (card instanceof StarterCard) {
                        for (ItemBox item : ((StarterCard) card).getPermanentRes().keySet()) {
                            switch (item) {
                                case Kingdom.ANIMAL -> permRes.append("A ".repeat(Math.max(0, ((StarterCard) card).getPermanentRes().get(item))));
                                case Kingdom.FUNGI -> permRes.append("F ".repeat(Math.max(0, ((StarterCard) card).getPermanentRes().get(item))));
                                case Kingdom.INSECT -> permRes.append("I ".repeat(Math.max(0, ((StarterCard) card).getPermanentRes().get(item))));
                                case Kingdom.PLANT -> permRes.append("P ".repeat(Math.max(0, ((StarterCard) card).getPermanentRes().get(item))));
                                default -> {}
                            }
                        }
                    } else switch (card.getKingdom()) {
                        case ANIMAL -> permRes.append("A ");
                        case FUNGI -> permRes.append("F ");
                        case INSECT -> permRes.append("I ");
                        case PLANT -> permRes.append("P ");
                    }

                    fieldBuilder.append(cardColor).append(switch(permRes.length()) {
                        case 2 -> "     " + permRes + "    ";
                        case 4 -> "    " + permRes + "   ";
                        case 6 -> "   " + permRes + "  ";
                        default -> throw new IllegalStateException("Unexpected value: " + permRes.length());
                    }).append(Color.reset);

                } else fieldBuilder.append(cardColor).append("           ");
                fieldBuilder.append(Color.reset).append("     ");
            }
            fieldBuilder.append("\n");


            if (firstCoordinate.getX() - firstCoordinate.getY() == firstColumn) {
                firstCoordinate = new Coords(firstCoordinate.getX(), firstCoordinate.getY() - 1);
                lastCoordinate = new Coords(lastCoordinate.getX(), lastCoordinate.getY() - 1);
            } else {
                firstCoordinate = new Coords(firstCoordinate.getX() - 1, firstCoordinate.getY());
                lastCoordinate = new Coords(lastCoordinate.getX() - 1, lastCoordinate.getY());
            }
        }
        // Print last lower string:


        if(view.getNickname().equals(nickname))System.out.println(cli + "Your field:\n" + fieldBuilder);
        else  System.out.println(cli + nickname +"'s field:\n" + fieldBuilder);
        //printResources();
    }

    public void printResources() {
        System.out.print(cli + "Your total resources:");
        for (ItemBox item : view.getMyField().getTotalResources().keySet()) {
            System.out.print(cli + " - " + item + ": " + view.getMyField().getTotalResources().get(item));
        } System.out.println();
    }

    public void printScoreboard() {
        HashMap<Player, Integer> scoreboard = view.getScoreboard();
        for (Player p : scoreboard.keySet()) {
            System.out.print(cli + "[" + p.getNickname() + " - " + scoreboard.get(p) + "]");
        } System.out.println();
    }

    public void printFinalScoreboard() {
        HashMap<Player, Integer> scoreboard = view.getScoreboard();
        HashMap<Player, Integer> goalsDone = view.getGoalsDone();

        ArrayList<Player> sortedPlayers = new ArrayList<>(scoreboard.keySet());
        sortedPlayers.sort((p1, p2) -> {
            int scoreComparison = scoreboard.get(p1).compareTo(scoreboard.get(p2));
            if (scoreComparison != 0) {
                return scoreComparison;
            } else {
                return goalsDone.get(p1).compareTo(goalsDone.get(p2));
            }
        });

        for (Player p : sortedPlayers) {
            System.out.print(cli + "[" + p.getNickname() + " - " + scoreboard.get(p) + " with " + goalsDone.get(p) + " goals done]");
        }
        System.out.println();
    }

    // Card + Hand printing methods:
    public String ItemsToColor(ItemBox item) {
        return switch(item) {
            case Kingdom.ANIMAL -> Color.animal.label;
            case Kingdom.FUNGI -> Color.fungi.label;
            case Kingdom.INSECT -> Color.insect.label;
            case Kingdom.PLANT -> Color.plant.label;
            case Resource.INKWELL, Resource.MANUSCRIPT, Resource.QUILL -> Color.gold.label;
            case CornerStatus.EMPTY -> Color.corner.label;
            case null -> Color.starter.label;
            default -> throw new IllegalStateException("Unexpected value: " + item);
        };
    }

    public String CornerToString(Card card, CornerType type) {
        ItemBox item = card.getItemFromCorner(type);

        if (item == null) return ItemsToColor(card.getKingdom()) + "   " + Color.reset.label;
        if (item.equals(Resource.INKWELL)) return Color.gold.label + " I " + Color.reset.label;
        if (item.equals(Resource.MANUSCRIPT)) return Color.gold.label + " M " + Color.reset.label;
        if (item.equals(Resource.QUILL)) return Color.gold.label + " Q " + Color.reset.label;
        if (item.equals(CornerStatus.EMPTY)) return Color.corner.label + "   " + Color.reset.label;

        if (card.getClass().equals(StarterCard.class)) return switch (item) {
            case Kingdom.FUNGI -> ItemsToColor(item) + " F ";
            case Kingdom.PLANT -> ItemsToColor(item) + " P ";
            case Kingdom.ANIMAL -> ItemsToColor(item) + " A ";
            case Kingdom.INSECT -> ItemsToColor(item) + " I ";
            default -> ItemsToColor(card.getKingdom()) + "   ";
        } + Color.reset.label;

        return switch (item) {
            case Kingdom.FUNGI -> Color.corner.label + " F ";
            case Kingdom.PLANT -> Color.corner.label + " P ";
            case Kingdom.ANIMAL -> Color.corner.label + " A ";
            case Kingdom.INSECT -> Color.corner.label + " I ";
            default -> ItemsToColor(card.getKingdom()) + "   ";
        } + Color.reset.label;
    }

    public String printUpper(Card card) {
        if (card.getClass().equals(CardBlock.class))
            return Color.black.label + "             " + Color.reset.label;

        String kingdom = ItemsToColor(card.getKingdom());
        String north = CornerToString(card, CornerType.NORTH);
        String east = CornerToString(card, CornerType.EAST);
        String points = kingdom + "   " + Color.reset.label;

        if (card.getSide().equals(CardSide.FRONT)) {
            int directPoints;
            if (card.getClass().equals(StarterCard.class)) {
                directPoints = 0;
            } else directPoints = ((ResourceCard) card).getPoints();

            // Setting string of points for cards:
            if (directPoints != 0) {
                if (card.getClass().equals(ResourceCard.class) || (card.getClass().equals(GoldenCard.class) && ((GoldenCard) card).getType() == GoldenCardType.DIRECT)) {
                    points = Color.gold.label + " " + directPoints + " " + Color.reset.label;
                }
                else {
                    // Generating corner covered points string:
                    if (((GoldenCard) card).getType() == GoldenCardType.CORNER)
                        points = Color.gold.label + directPoints + "xC" + Color.reset.label;

                        // Generating gold resources points string:
                    else if (((GoldenCard) card).getType() == GoldenCardType.RESOURCE) {
                        points = Color.gold.label + directPoints + "x" + switch (((GoldenCard) card).getPointResource()) {
                            case INKWELL -> "I";
                            case MANUSCRIPT -> "M";
                            case QUILL -> "Q";
                        } + Color.reset.label;
                    }
                }
            }
        }
        return north + kingdom + "  " + Color.reset.label + points + kingdom + "  " + Color.reset.label + east;
    }

    public String printMiddle(Card card) {
        if (card.getClass().equals(CardBlock.class))
            return Color.black.label + "             " + Color.reset.label;

        if (card.getClass().equals(StarterCard.class)) {
            if (card.getSide().equals(CardSide.FRONT)) return Color.starter.label + "             " + Color.reset.label;

            // Setting string of permanent resources
            StringBuilder permRes = new StringBuilder();
            for (Kingdom k : ((StarterCard) card).getPermanentRes().keySet()) {
                switch(k) {
                    case ANIMAL -> permRes.append("A ".repeat(Math.max(0, ((StarterCard) card).getPermanentRes().get(k))));
                    case FUNGI -> permRes.append("F ".repeat(Math.max(0, ((StarterCard) card).getPermanentRes().get(k))));
                    case INSECT -> permRes.append("I ".repeat(Math.max(0, ((StarterCard) card).getPermanentRes().get(k))));
                    case PLANT -> permRes.append("P ".repeat(Math.max(0, ((StarterCard) card).getPermanentRes().get(k))));
                }
            }

            return switch(permRes.length()) {
                case 2 -> Color.starter.label + "      " + permRes + "     " + Color.reset.label;
                case 4 -> Color.starter.label + "     " + permRes + "    " + Color.reset.label;
                case 6 -> Color.starter.label + "    " + permRes + "   " + Color.reset.label;
                default -> throw new IllegalStateException("Unexpected value: " + permRes.length());
            };
        }

        String kingdom;
        if (card.getSide().equals(CardSide.BACK)) {
            kingdom = switch (ItemsToColor(card.getKingdom())) {
                case "\u001B[30;46m" -> " A ";      // Color.animal.label
                case "\u001B[30;41m" -> " F ";      // Color.fungi.label
                case "\u001B[30;45m" -> " I ";      // Color.insect.label
                case "\u001B[30;42m" -> " P ";      // Color.plant.label
                default -> throw new IllegalStateException("Unexpected value: " + ItemsToColor(card.getKingdom()));
            };

            if (card.getClass().equals(GoldenCard.class))
                return ItemsToColor(card.getKingdom()) + "     " + Color.reset + Color.gold + kingdom + Color.reset + ItemsToColor(card.getKingdom()) + "     " + Color.reset;
        } else kingdom = "   ";

        return ItemsToColor(card.getKingdom()) + "     " + kingdom + "     " + Color.reset;
    }

    public String printLower(Card card) {
        if (card.getClass().equals(CardBlock.class))
            return Color.black.label + "             " + Color.reset.label;

        String kingdom = ItemsToColor(card.getKingdom());

        String south = CornerToString(card, CornerType.SOUTH);
        String west = CornerToString(card, CornerType.WEST);

        if (card.getClass().equals(GoldenCard.class) && card.getSide().equals(CardSide.FRONT)) {
            StringBuilder requirements = new StringBuilder();
            for (Kingdom k : ((GoldenCard) card).getRequirements().keySet()) {
                switch(k) {
                    case ANIMAL -> requirements.append("A".repeat(Math.max(0, ((GoldenCard) card).getRequirements().get(k))));
                    case FUNGI -> requirements.append("F".repeat(Math.max(0, ((GoldenCard) card).getRequirements().get(k))));
                    case INSECT -> requirements.append("I".repeat(Math.max(0, ((GoldenCard) card).getRequirements().get(k))));
                    case PLANT -> requirements.append("P".repeat(Math.max(0, ((GoldenCard) card).getRequirements().get(k))));
                }
            }

            return west + kingdom + switch (requirements.toString().length()) {
                case 1 -> "   " + Color.reset + Color.starter + requirements + Color.reset + kingdom + "   ";
                case 2 -> "  " + Color.reset + Color.starter + requirements + Color.reset + kingdom + "   ";
                case 3 -> "  " + Color.reset + Color.starter + requirements + Color.reset + kingdom + "  ";
                case 4 -> " " + Color.reset + Color.starter + requirements + Color.reset + kingdom + "  ";
                case 5 -> " " + Color.reset + Color.starter + requirements + Color.reset + kingdom + " ";
                default -> throw new IllegalStateException("Unexpected value: " + requirements.toString().length());
            } + Color.reset + south;
        }
        return west + kingdom + "       " + Color.reset + south;
    }

    public void printCard(Card card) {
        System.out.println(cli + printUpper(card) + cli + printMiddle(card) + cli + printLower(card) + "\n");
    }

    public void printHand() {
        Hand hand = view.getHand();

        System.out.print(cli);
        for(int i = 0; i < hand.getSize(); i++)
            System.out.print("\t\t" + printUpper(hand.getCard(i)));

        System.out.print(cli);
        for(int i = 0; i < hand.getSize(); i++)
            System.out.print("\t\t" + printMiddle(hand.getCard(i)));

        System.out.print(cli);
        for(int i = 0; i < hand.getSize(); i++)
            System.out.print("\t" + i + ":\t" + printLower(hand.getCard(i)));

        System.out.println();
    }

    // Goal printing methods:
    public void printGoal(Goal goal) {
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
}
