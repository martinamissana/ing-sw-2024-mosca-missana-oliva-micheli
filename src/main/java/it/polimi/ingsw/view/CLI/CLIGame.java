package it.polimi.ingsw.view.CLI;

import it.polimi.ingsw.model.card.Card;
import it.polimi.ingsw.model.card.CardSide;
import it.polimi.ingsw.model.card.CornerType;
import it.polimi.ingsw.model.card.GoldenCard;
import it.polimi.ingsw.model.card.GoldenCardType;
import it.polimi.ingsw.model.card.ResourceCard;
import it.polimi.ingsw.model.card.StarterCard;
import it.polimi.ingsw.model.commonItem.CornerStatus;
import it.polimi.ingsw.model.commonItem.ItemBox;
import it.polimi.ingsw.model.commonItem.Kingdom;
import it.polimi.ingsw.model.commonItem.Resource;
import it.polimi.ingsw.model.deck.DeckBuffer;
import it.polimi.ingsw.model.deck.DeckBufferType;
import it.polimi.ingsw.model.player.Hand;
import it.polimi.ingsw.model.player.Player;
import it.polimi.ingsw.view.View;

import java.util.HashMap;

public class CLIGame {
    private final View view;
    private static final String reset = "\u001B[0m";
    private static final String FungiColor = "\u001B[30;41m"; // Red
    private static final String PlantColor = "\u001B[30;42m"; // Green
    private static final String InsectColor = "\u001B[30;45m"; // Purple
    private static final String AnimalColor = "\u001B[30;46m"; // Cyan
    private static final String StarterColor = "\u001B[30;48;2;245;200;157m"; // Meat
    private static final String GoldColor = "\u001B[30;43m"; // Gold
    private static final String CardBlockColor = "\u001B[40m";   // Black
    private static final String cli = "\u001B[38;2;255;165;0m" + "\n[+] " + "\u001B[0m";
    private static final String user = "\u001B[38;2;255;165;0m" + "\n[-] " + "\u001B[0m";

    public CLIGame(View view) {
        this.view = view;
    }

    public String ItemsToColor(ItemBox item) {
        return switch(item) {
            case Kingdom.ANIMAL -> AnimalColor;
            case Kingdom.FUNGI -> FungiColor;
            case Kingdom.INSECT -> InsectColor;
            case Kingdom.PLANT -> PlantColor;
            case Resource.INKWELL, Resource.MANUSCRIPT, Resource.QUILL -> GoldColor;
            case CornerStatus.EMPTY -> StarterColor;
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
        if (item.equals(CornerStatus.EMPTY)) return StarterColor + "   " + reset;

        if (card.getClass().getName().equals("it.polimi.ingsw.model.card.StarterCard")) return switch (item) {
            case Kingdom.FUNGI -> ItemsToColor(item) + " F ";
            case Kingdom.PLANT -> ItemsToColor(item) + " P ";
            case Kingdom.ANIMAL -> ItemsToColor(item) + " A ";
            case Kingdom.INSECT -> ItemsToColor(item) + " I ";
            default -> ItemsToColor(card.getKingdom()) + "   ";
        } + reset;

        return switch (item) {
            case Kingdom.FUNGI -> StarterColor + " F ";
            case Kingdom.PLANT -> StarterColor + " P ";
            case Kingdom.ANIMAL -> StarterColor + " A ";
            case Kingdom.INSECT -> StarterColor + " I ";
            default -> ItemsToColor(card.getKingdom()) + "   ";
        } + reset;
    }

    private String printUpper(Card card) {
        if (card.getClass().getName().equals("it.polimi.ingsw.model.card.CardBlock"))
            return CardBlockColor + "             " + reset;

        String kingdom = ItemsToColor(card.getKingdom());
        String north = ItemToString(card, CornerType.NORTH);
        String west = ItemToString(card, CornerType.WEST);
        String points = kingdom + "       " + reset;

        if (card.getSide().equals(CardSide.FRONT)) {
            int directPoints;
            if (card.getClass().getName().equals("it.polimi.ingsw.model.card.StarterCard")) {
                directPoints = 0;
            } else directPoints = ((ResourceCard) card).getPoints();

            // Setting string of points for cards:
            if (directPoints != 0) {
                if (card.getClass().getName().equals("it.polimi.ingsw.model.card.GoldenCard")) {
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
            }
        }

        return west + " " + points + " " + north;
    }

    private String printMiddle(Card card) {
        if (card.getClass().getName().equals("it.polimi.ingsw.model.card.CardBlock"))
            return CardBlockColor + "             " + reset;

        if (card.getClass().getName().equals("it.polimi.ingsw.model.card.StarterCard")) {
            if (card.getSide().equals(CardSide.FRONT)) return StarterColor + "             " + reset;

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

            if (card.getClass().getName().equals("it.polimi.ingsw.model.card.GoldenCard"))
                return ItemsToColor(card.getKingdom()) + "     " + reset + GoldColor + kingdom + reset + ItemsToColor(card.getKingdom()) + "     " + reset;
        } else kingdom = "   ";

        return ItemsToColor(card.getKingdom()) + "     " + kingdom + "     " + reset;
    }

    private String printLower(Card card) {
        if (card.getClass().getName().equals("it.polimi.ingsw.model.card.CardBlock"))
            return CardBlockColor + "             " + reset;

        String kingdom = ItemsToColor(card.getKingdom());

        String south = ItemToString(card, CornerType.SOUTH);
        String east = ItemToString(card, CornerType.EAST);

        if (card.getClass().getName().equals("it.polimi.ingsw.model.card.GoldenCard") && card.getSide().equals(CardSide.FRONT)) {
            StringBuilder requirements = new StringBuilder();
            for (Kingdom k : ((GoldenCard) card).getRequirements().keySet()) {
                switch(k) {
                    case ANIMAL -> requirements.append("A".repeat(Math.max(0, ((GoldenCard) card).getRequirements().get(k))));
                    case FUNGI -> requirements.append("F".repeat(Math.max(0, ((GoldenCard) card).getRequirements().get(k))));
                    case INSECT -> requirements.append("I".repeat(Math.max(0, ((GoldenCard) card).getRequirements().get(k))));
                    case PLANT -> requirements.append("P".repeat(Math.max(0, ((GoldenCard) card).getRequirements().get(k))));
                }
            }

            return south + kingdom + switch (requirements.toString().length()) {
                case 1 -> "   " + reset + StarterColor + requirements + reset + kingdom + "   ";
                case 2 -> "  " + reset + StarterColor + requirements + reset + kingdom + "   ";
                case 3 -> "  " + reset + StarterColor + requirements + reset + kingdom + "  ";
                case 4 -> " " + reset + StarterColor + requirements + reset + kingdom + "  ";
                case 5 -> " " + reset + StarterColor + requirements + reset + kingdom + " ";
                default -> throw new IllegalStateException("Unexpected value: " + requirements.toString().length());
            } + reset + east;
        }
        return south + kingdom + "       " + reset + east;
    }

    public void printCard(Card card) {
        System.out.println(cli + printUpper(card) + cli + printMiddle(card) + cli + printLower(card) + "\n");
    }

    public void printHand(Player player) {
        Hand hand = view.getHand();

        System.out.print("\n" + cli + player.getNickname());
        if (player.getNickname().toUpperCase().charAt(player.getNickname().length() - 1) == 'S') System.out.print("' hand:" + cli);
        else System.out.print("'s hand:" + cli);

        for(int i = 0; i < hand.getSize(); i++)
            if (i != hand.getSize() - 1) {
                System.out.print(printUpper(hand.getCard(i)) + "\t");
            } else System.out.print(printUpper(hand.getCard(i)) + cli);

        for(int i = 0; i < hand.getSize(); i++)
            if (i != hand.getSize() - 1) {
                System.out.print(printMiddle(hand.getCard(i)) + "\t");
            } else System.out.print(printMiddle(hand.getCard(i)) + cli);

        for(int i = 0; i < hand.getSize(); i++)
            if (i != hand.getSize() - 1) {
                System.out.print(printLower(hand.getCard(i)) + "\t");
            } else System.out.println(printLower(hand.getCard(i)));
    }

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
    }

    public void printScoreboard() {
        HashMap<Player, Integer> scoreboard = view.getScoreboard();
        for (Player p : scoreboard.keySet()) {
            System.out.print(cli + "[" + p.getNickname() + " - " + scoreboard.get(p) + "]");
        }
    }
}
