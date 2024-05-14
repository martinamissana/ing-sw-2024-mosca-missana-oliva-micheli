package it.polimi.ingsw.view.CLI;

import it.polimi.ingsw.controller.exceptions.*;
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
import it.polimi.ingsw.model.deck.DeckType;
import it.polimi.ingsw.model.deck.DeckTypeBox;
import it.polimi.ingsw.model.exceptions.*;
import it.polimi.ingsw.model.game.Action;
import it.polimi.ingsw.model.goal.*;
import it.polimi.ingsw.model.player.Coords;
import it.polimi.ingsw.model.player.Hand;
import it.polimi.ingsw.model.player.Player;
import it.polimi.ingsw.view.View;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

public class CLIGame {
    private final View view;
    private final Scanner input = new Scanner(System.in);
    private static final String reset = "\u001B[0m";
    private static final String warningColor = "\u001B[31m";
    private static final String FungiColor = "\u001B[30;41m"; // Red
    private static final String PlantColor = "\u001B[30;42m"; // Green
    private static final String InsectColor = "\u001B[30;45m"; // Purple
    private static final String AnimalColor = "\u001B[30;46m"; // Cyan
    private static final String StarterColor = "\u001B[30;48;2;245;200;157m"; // Skin
    private static final String cornerColor = "\u001B[30;48;2;200;150;100m"; // Darker skin

    private static final String GoldColor = "\u001B[30;43m"; // Gold
    private static final String CardBlockColor = "\u001B[40m";   // Black
    private static final String cli = "\u001B[38;2;255;165;0m" + "\n[+] " + "\u001B[0m";
    private static final String user = "\u001B[38;2;255;165;0m" + "\n[-] " + "\u001B[0m";

    public CLIGame(View view) {
        this.view = view;
    }

    protected void placeStarterCard() {
        if (view.getHand().getSize() == 0 || !view.getHand().getCard(0).getClass().equals(StarterCard.class)) return;
        StarterCard card = (StarterCard) view.getHand().getCard(0);
        System.out.print(cli + "FRONT SIDE:");
        printCard(card);
        card.flip();
        System.out.print(cli + "BACK SIDE:");
        printCard(card);

        CardSide side;

        do {
            System.out.print(cli + "Which side do you prefer?" + user);
            String choice = input.nextLine();

            side = switch (choice.toUpperCase()) {
                case "FRONT", "F" -> CardSide.FRONT;
                case "BACK", "B" -> CardSide.BACK;
                default -> null;
            };
            if (side == null) System.out.println(warningColor + "\n[ERROR]: Invalid choice!!\n" + reset);
            else {
                System.out.print(cli + "Positioning starter card");
                printDots();

                try {
                    view.chooseCardSide(side);
                }
                catch (IOException | GameDoesNotExistException | EmptyDeckException | HandIsFullException |
                       UnexistentUserException | WrongGamePhaseException e) {
                    System.out.print(e.getClass().getName());
                }
            }
        } while (side == null);

        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    protected void chooseSecretGoal () {
        ArrayList<Goal> goals = view.getSecretGoalChoices();

        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        goals.stream().mapToInt(Goal::getGoalID).forEach(System.out::println);
        System.out.print(cli + "You can choose between these two goals:");
        printGoal(goals.getFirst());
        printGoal(goals.getLast());

        int ID;
        do {
            System.out.print(cli + "Select ID of the goal you want:" + user);
            ID = input.nextInt();

            try {
                view.chooseSecretGoal(ID);
            } catch (IOException | UnexistentUserException e) {
                throw new RuntimeException(e);
            } catch (WrongGamePhaseException | GameDoesNotExistException ignored) {
            } catch (IllegalGoalChosenException e) {
                System.out.print(warningColor + "[ERROR]: Invalid choice!!" + reset);
                ID = -1;
            }
        } while (ID == -1);
    }

    protected void playCard() {
        if (!view.isYourTurn()) {
            System.out.println(warningColor + "[ERROR]: Cannot play card because it's not your turn!!" + reset);
            return;
        }
        if (!view.getAction().equals(Action.PLAY)) {
            System.out.println(warningColor + "[ERROR]: Cannot play card because you have to draw!!" + reset);
            return;
        }
        int choice;
        Coords coords = null;

        do {
            printHand();
            System.out.print(cli + "Which card do you want to play?" + user);
            choice = input.nextInt();

            if (choice < 0 && choice >= view.getHand().getSize()) {
                System.out.println(warningColor + "\n[ERROR]: Invalid choice!!\n" + reset);
                choice = -1;
            } else {
                // printField();
                System.out.print(cli + "In which position you want to put this card? Select X:" + user);
                int X = input.nextInt();
                System.out.print(cli + "Now select Y:" + user);
                int Y = input.nextInt();
                coords = new Coords(X, Y);


                try {
                    view.playCard(choice, coords);
                    Thread.sleep(500);
                } catch (IllegalActionException | NotYourTurnException | LobbyDoesNotExistsException |
                         GameDoesNotExistException ignored) {}
                catch (IllegalMoveException e) {
                    System.out.println(warningColor + "[ERROR]: Illegal placement. Cannot play card in this spot!!" + reset);
                    coords = null;
                } catch (UnexistentUserException | IOException | InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        } while (choice == -1 && coords == null);
    }

    protected void drawCard() {
        if (!view.isYourTurn()) {
            System.out.println(warningColor + "[ERROR]: Cannot draw card because it's not your turn!!" + reset);
            return;
        }
        if (!view.getAction().equals(Action.DRAW)) {
            System.out.println(warningColor + "[ERROR]: Cannot draw card because you have to play one first!!" + reset);
            return;
        }

        printGameArea();
        String choice;
        DeckTypeBox type;
        do {
            System.out.print(cli + "From where do you want to draw?" +
                    cli + "Resource Deck -> ResDeck" +
                    cli + "Golden Deck -> GoldDeck" +
                    cli + "Resource card spaces -> RES1 - RES2" +
                    cli + "Golden card spaces -> GOLD1 - GOLD2" + user);
            choice = input.nextLine();

            type = switch (choice.toUpperCase()) {      // RD, GD help with testing
                case "RD", "RESDECK", "RESOURCE DECK" -> DeckType.RESOURCE;
                case "GD", "GOLDDECK", "GOLDEN DECK" -> DeckType.GOLDEN;
                case "R1", "RES1" -> DeckBufferType.RES1;
                case "R2", "RES2" -> DeckBufferType.RES2;
                case "G1", "GOLD1" -> DeckBufferType.GOLD1;
                case "G2", "GOLD2" -> DeckBufferType.GOLD2;
                default -> null;
            };

            if (type == null) System.out.print(warningColor + "\n[ERROR]: Invalid choice!!\n" + reset);
        } while (type == null);

        try {
            view.drawCard(type);
        } catch (IllegalActionException | HandIsFullException | EmptyBufferException | NotYourTurnException |
                 LobbyDoesNotExistsException | GameDoesNotExistException ignored) {
        } catch (EmptyDeckException e) {
            System.out.println(warningColor + "[ERROR]: Cannot draw from this deck. Reason: empty!!" + reset);
        } catch (IOException | UnexistentUserException e) {
            throw new RuntimeException(e);
        }
    }

    private String ItemsToColor(ItemBox item) {
        return switch(item) {
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

    private void printGoal(Goal goal) {
        if (goal.getClass().equals(L_ShapeGoal.class)) {
            System.out.print(cli + "L-Shape Goal " + goal.getGoalID() + ": " + ((L_ShapeGoal) goal).getType() + " with two " + ((L_ShapeGoal) goal).getMainColor() + " and " + ((L_ShapeGoal) goal).getSecondaryColor() + ". Points = " + goal.getPoints());
        } else if (goal.getClass().equals(DiagonalGoal.class)) {
            System.out.print(cli + "Diagonal Goal " + goal.getGoalID() + ": " + ((DiagonalGoal) goal).getType() + " of " + ((DiagonalGoal) goal).getColor() + ". Points = " + goal.getPoints());
        } else {
            System.out.print(cli + "Resource Goal " + goal.getGoalID() + ": " + goal.getPoints() + " points if you have: ");
            for (ItemBox item : ((ResourceGoal) goal).getResourceList()) {
                System.out.print(item);
            }
        }
    }

    private String printUpper(Card card) {
        if (card.getClass().getName().equals("it.polimi.ingsw.model.card.CardBlock"))
            return CardBlockColor + "             " + reset;

        String kingdom = ItemsToColor(card.getKingdom());
        String north = ItemToString(card, CornerType.NORTH);
        String east = ItemToString(card, CornerType.EAST);
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
                return north + kingdom + "  " + reset + points + kingdom + "  " + reset + east;
            }
        }

        return north + points + east;
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
        String west = ItemToString(card, CornerType.WEST);

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

    private void printGameArea() {
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

    protected void printScoreboard() {
        HashMap<Player, Integer> scoreboard = view.getScoreboard();
        for (Player p : scoreboard.keySet()) {
            System.out.print(cli + "[" + p.getNickname() + " - " + scoreboard.get(p) + "]");
        }
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
