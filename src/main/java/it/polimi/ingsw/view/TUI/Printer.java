package it.polimi.ingsw.view.TUI;

import it.polimi.ingsw.model.card.*;
import it.polimi.ingsw.model.commonItem.CornerStatus;
import it.polimi.ingsw.model.commonItem.ItemBox;
import it.polimi.ingsw.model.commonItem.Kingdom;
import it.polimi.ingsw.model.commonItem.Resource;
import it.polimi.ingsw.model.deck.DeckBuffer;
import it.polimi.ingsw.model.deck.DeckBufferType;
import it.polimi.ingsw.model.game.Lobby;
import it.polimi.ingsw.model.goal.DiagonalGoal;
import it.polimi.ingsw.model.goal.Goal;
import it.polimi.ingsw.model.goal.L_ShapeGoal;
import it.polimi.ingsw.model.goal.ResourceGoal;
import it.polimi.ingsw.model.player.*;
import it.polimi.ingsw.view.View;

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

    public void printDots() {
        try {
            Thread.sleep(500);
            for (int i = 0; i < 3; i++) {
                System.out.print(".");
                Thread.sleep(500);
            }
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    // ~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~
    // Main Menu + Lobby printing methods:
    // ~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~·~

    public void printOpenLobbies() {
        System.out.print(cli + "Open lobbies:");
        HashMap<Integer, Lobby> lobbies = view.getLobbies();

        for (Integer i : lobbies.keySet()) {
            if (lobbies.get(i).getPlayers().size() != lobbies.get(i).getNumOfPlayers()) {
                System.out.print(cli + "-> Lobby " + Color.green + "#" + i + Color.reset + ": " + lobbies.get(i).getPlayers().size() + "/" + lobbies.get(i).getNumOfPlayers() + " - [");

                for (Player p : lobbies.get(i).getPlayers()) {
                    if (p.equals(lobbies.get(i).getPlayers().getLast())) {
                        System.out.print(p.getNickname() + "]");
                    } else {
                        System.out.print(p.getNickname() + " - ");
                    }
                }
            } else System.out.print(cli + "-> Lobby " + Color.warning + "#" + i + Color.reset + ": FULL");
        }
        System.out.println();
    }

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
        Card next = view.getTopResourceCard();
        HashMap<DeckBufferType, DeckBuffer> cardSpaces = view.getDeckBuffers();

        // Printing resource deck + spaces
        if (next != null) next.flip();

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
        if (next != null) next.flip();

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
        System.out.print("\n" + cli + "Common goals:");
        printGoal(view.getCommonGoal1());
        printGoal(view.getCommonGoal2());
        System.out.println();
        printGoal(view.getSecretGoal());

        System.out.println("\n");
    }

    public void printFieldTemp() {         // TODO: Do a better printField (StringBuilder???)
        System.out.print(cli + "Occupied spots:");
        for (Coords coords : view.getMyField().getMatrix().keySet()) {
            System.out.format(cli + "(%d, %d)\n", coords.getX(), coords.getY());
            printCard(view.getMyField().getMatrix().get(coords));
        } System.out.println();
    }

    public void printField(String nickname) {
        Field field = null;
        if (nickname.equals(view.getNickname())) field = view.getMyField();
        else {
            for (Player p : view.getLobbies().get(view.getID()).getPlayers()) {
                if (p.getNickname().equals(nickname)) field = view.getFields().get(p);
            }
        }
        if (field == null) return;

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
        ItemBox item = card.getCorner(type);

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
