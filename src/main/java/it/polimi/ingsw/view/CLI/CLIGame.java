package it.polimi.ingsw.view.CLI;

import it.polimi.ingsw.model.card.*;
import it.polimi.ingsw.model.commonItem.CornerStatus;
import it.polimi.ingsw.model.commonItem.ItemBox;
import it.polimi.ingsw.model.commonItem.Kingdom;
import it.polimi.ingsw.model.commonItem.Resource;
import it.polimi.ingsw.model.deck.DeckBufferType;
import it.polimi.ingsw.model.deck.DeckType;
import it.polimi.ingsw.model.exceptions.GameDoesNotExistException;
import it.polimi.ingsw.model.game.Game;
import it.polimi.ingsw.model.game.GameHandler;
import it.polimi.ingsw.model.player.Hand;
import it.polimi.ingsw.model.player.Player;

public class CLIGame extends CLI {
    public static GameHandler gh;
    public static final String reset = "\u001B[0m";
    public static final String FungiColor = "\u001B[30;41m"; // Red
    public static final String PlantColor = "\u001B[30;42m"; // Green
    public static final String InsectColor = "\u001B[30;45m"; // Purple
    public static final String AnimalColor = "\u001B[30;46m"; // Cyan
    public static final String StarterColor = "\u001B[30;48;2;245;200;157m"; // Meat
    public static final String GoldColor = "\u001B[30;43m"; // Gold
    public static final String CardBlockColor = "\u001B[40m";

    @Override
    public void setGameHandler(GameHandler gh) {
        CLIGame.gh = gh;
    }

    public String KingdomToColor(Kingdom kingdom) {
        return switch(kingdom) {
            case ANIMAL -> AnimalColor;
            case FUNGI -> FungiColor;
            case INSECT -> InsectColor;
            case PLANT -> PlantColor;
            case null -> StarterColor;
        };
    }

    public String ItemToString(Card card, CornerType type) {
        ItemBox item = card.getCorner(type);

        if (card.getClass().getName().equals("it.polimi.ingsw.model.card.StarterCard")) {
            return switch (item) {
                case Resource.INKWELL -> GoldColor + " I " + reset;
                case Resource.MANUSCRIPT -> GoldColor + " M " + reset;
                case Resource.QUILL -> GoldColor + " Q " + reset;
                case Kingdom.FUNGI -> FungiColor + " F " + reset;
                case Kingdom.PLANT -> PlantColor + " P " + reset;
                case Kingdom.ANIMAL -> AnimalColor + " A " + reset;
                case Kingdom.INSECT -> InsectColor + " I " + reset;
                case CornerStatus.EMPTY -> StarterColor + "   " + reset;
                case null, default -> KingdomToColor(card.getKingdom()) + "   " + reset;
            };
        }
        else return switch (item) {
            case Resource.INKWELL -> GoldColor + " I " + reset;
            case Resource.MANUSCRIPT -> GoldColor + " M " + reset;
            case Resource.QUILL -> GoldColor + " Q " + reset;
            case Kingdom.FUNGI -> StarterColor + " F " + reset;
            case Kingdom.PLANT -> StarterColor + " P " + reset;
            case Kingdom.ANIMAL -> StarterColor + " A " + reset;
            case Kingdom.INSECT -> StarterColor + " I " + reset;
            case CornerStatus.EMPTY -> StarterColor + "   " + reset;
            case null, default -> KingdomToColor(card.getKingdom()) + "   " + reset;
        };
    }

    private String printUpper(Card card) {
        if (card.getClass().getName().equals("it.polimi.ingsw.model.card.CardBlock"))
            return CardBlockColor + "             " + reset;

        String kingdom = KingdomToColor(card.getKingdom());

        String north = ItemToString(card, CornerType.NORTH);
        String west = ItemToString(card, CornerType.WEST);

        if (card.getSide().equals(CardSide.FRONT)) {
            int directPoints = 0;
            if (card.getClass().getName().equals("it.polimi.ingsw.model.card.ResourceCard") || card.getClass().getName().equals("it.polimi.ingsw.model.card.GoldenCard")) {
                directPoints = ((ResourceCard) card).getPoints();
            }

            if (directPoints != 0) {
                String points;
                if (card.getClass().getName().equals("it.polimi.ingsw.model.card.GoldenCard") && ((GoldenCard) card).getType() == GoldenCardType.CORNER)
                    points = directPoints + "xC";
                else if (card.getClass().getName().equals("it.polimi.ingsw.model.card.GoldenCard") && ((GoldenCard) card).getType() == GoldenCardType.RESOURCE) {
                    points = directPoints + "x" + switch (((GoldenCard) card).getPointResource()) {
                        case INKWELL -> "I";
                        case MANUSCRIPT -> "M";
                        case QUILL -> "Q";
                    };
                } else points = " " + directPoints + " ";

                return west + kingdom + "  " + reset + GoldColor + points + reset + kingdom + "  " + reset + north;
            }
        }
        return west + kingdom + "       " + reset + north;
    }

    private String printMiddle(Card card) {
        if (card.getClass().getName().equals("it.polimi.ingsw.model.card.CardBlock"))
            return CardBlockColor + "             " + reset;

        if (card.getClass().getName().equals("it.polimi.ingsw.model.card.StarterCard")) {
            if (card.getSide().equals(CardSide.FRONT)) return StarterColor + "             " + reset;

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
            kingdom = switch (KingdomToColor(card.getKingdom())) {
                case AnimalColor -> " A ";
                case FungiColor -> " F ";
                case InsectColor -> " I ";
                case PlantColor -> " P ";
                default -> throw new IllegalStateException("Unexpected value: " + KingdomToColor(card.getKingdom()));
            };

            if (card.getClass().getName().equals("it.polimi.ingsw.model.card.GoldenCard"))
                return KingdomToColor(card.getKingdom()) + "     " + reset + GoldColor + kingdom + reset + KingdomToColor(card.getKingdom()) + "     " + reset;
        } else kingdom = "   ";

        return KingdomToColor(card.getKingdom()) + "     " + kingdom + "     " + reset;
    }

    private String printLower(Card card) {
        if (card.getClass().getName().equals("it.polimi.ingsw.model.card.CardBlock"))
            return CardBlockColor + "             " + reset;

        String kingdom = KingdomToColor(card.getKingdom());

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

    public void printHand(Player player, int swapFactor) {
        switch (swapFactor) {
            case -1 -> {
                for (int i = 0; i < player.getHand().getSize(); i++) player.getHand().getCard(i).flip();
            }
            case 0, 1, 2 -> {
                try {
                    player.getHand().getCard(swapFactor).flip();
                } catch (IndexOutOfBoundsException ignored) {}
            }
        }
        Hand hand = player.getHand();

        System.out.print("\n" + cli + player.getNickname());
        if (player.getNickname().toUpperCase().charAt(player.getNickname().length() - 1) == 'S') System.out.print("' hand:" + cli);
        else System.out.print("'s hand:" + cli);

        for(int i = 0; i < hand.getSize(); i++)
            if (i != hand.getSize() - 1) {
                System.out.print(printUpper(hand.getCard(i)) + "\t");
            } else System.out.print(printUpper(hand.getCard(i)));

        System.out.print(cli);
        for(int i = 0; i < hand.getSize(); i++)
            if (i != hand.getSize() - 1) {
                System.out.print(printMiddle(hand.getCard(i)) + "\t");
            } else System.out.print(printMiddle(hand.getCard(i)));

        System.out.print(cli);
        for(int i = 0; i < hand.getSize(); i++)
            if (i != hand.getSize() - 1) {
                System.out.print(printLower(hand.getCard(i)) + "\t");
            } else System.out.print(printLower(hand.getCard(i)));

        System.out.println();
    }

    public void printGameArea(Integer gameID) throws GameDoesNotExistException {
        Game game = gh.getGame(gameID);
        ResourceCard next;

        // Printing resource deck + spaces
        if (!game.getDeck(DeckType.RESOURCE).getCards().isEmpty()) {
            next = game.getDeck(DeckType.RESOURCE).getCards().getLast();
            next.flip();
        } else next = null;

        System.out.print(cli + "Resource Deck (" + game.getDeck(DeckType.RESOURCE).getCards().size() + ") + card spaces:" + cli);
        if (next != null) System.out.print(printUpper(next) + "\t\t");
        else System.out.print("\t\t\t\t\t");
        if (game.getDeckBuffer(DeckBufferType.RES1).getCard() != null) System.out.print(printUpper(game.getDeckBuffer(DeckBufferType.RES1).getCard()) + "\t");
        else System.out.print("\t\t\t\t");
        if (game.getDeckBuffer(DeckBufferType.RES2).getCard() != null) System.out.print(printUpper(game.getDeckBuffer(DeckBufferType.RES2).getCard()) + cli);
        else System.out.print("\t\t\t\t");

        if (next != null) System.out.print(printMiddle(next) + "\t\t");
        else System.out.print("\t\t\t\t\t");
        if (game.getDeckBuffer(DeckBufferType.RES1).getCard() != null) System.out.print(printMiddle(game.getDeckBuffer(DeckBufferType.RES1).getCard()) + "\t");
        else System.out.print("\t\t\t\t");
        if (game.getDeckBuffer(DeckBufferType.RES2).getCard() != null) System.out.print(printMiddle(game.getDeckBuffer(DeckBufferType.RES2).getCard()) + cli);
        else System.out.print("\t\t\t\t");

        if (next != null) System.out.print(printLower(next) + "\t\t");
        else System.out.print("\t\t\t\t\t");
        if (game.getDeckBuffer(DeckBufferType.RES1).getCard() != null) System.out.print(printLower(game.getDeckBuffer(DeckBufferType.RES1).getCard()) + "\t");
        else System.out.print("\t\t\t\t");
        if (game.getDeckBuffer(DeckBufferType.RES2).getCard() != null) System.out.print(printLower(game.getDeckBuffer(DeckBufferType.RES2).getCard()) + cli);
        else System.out.print("\t\t\t\t");

        // Printing golden deck + spaces
        if (!game.getDeck(DeckType.GOLDEN).getCards().isEmpty()) {
            next = game.getDeck(DeckType.GOLDEN).getCards().getLast();
            next.flip();
        } else next = null;

        System.out.print(cli + "Golden Deck (" + game.getDeck(DeckType.GOLDEN).getCards().size() + ") + card spaces:" + cli);
        if (next != null) System.out.print(printUpper(next) + "\t\t");
        else System.out.print("\t\t\t\t\t");
        if (game.getDeckBuffer(DeckBufferType.GOLD1).getCard() != null) System.out.print(printUpper(game.getDeckBuffer(DeckBufferType.GOLD1).getCard()) + "\t");
        else System.out.print("\t\t\t\t");
        if (game.getDeckBuffer(DeckBufferType.GOLD2).getCard() != null) System.out.print(printUpper(game.getDeckBuffer(DeckBufferType.GOLD2).getCard()) + cli);
        else System.out.print("\t\t\t\t");

        if (next != null) System.out.print(printMiddle(next) + "\t\t");
        else System.out.print("\t\t\t\t\t");
        if (game.getDeckBuffer(DeckBufferType.GOLD1).getCard() != null) System.out.print(printMiddle(game.getDeckBuffer(DeckBufferType.GOLD1).getCard()) + "\t");
        else System.out.print("\t\t\t\t");
        if (game.getDeckBuffer(DeckBufferType.GOLD2).getCard() != null) System.out.print(printMiddle(game.getDeckBuffer(DeckBufferType.GOLD2).getCard()) + cli);
        else System.out.print("\t\t\t\t");

        if (next != null) System.out.print(printLower(next) + "\t\t");
        else System.out.print("\t\t\t\t\t");
        if (game.getDeckBuffer(DeckBufferType.GOLD1).getCard() != null) System.out.print(printLower(game.getDeckBuffer(DeckBufferType.GOLD1).getCard()) + "\t");
        else System.out.print("\t\t\t\t");
        if (game.getDeckBuffer(DeckBufferType.GOLD2).getCard() != null) System.out.print(printLower(game.getDeckBuffer(DeckBufferType.GOLD2).getCard()) + cli);
        else System.out.print("\t\t\t\t");
    }

    public void printScoreboard(Integer gameID) throws GameDoesNotExistException {
        Game game = gh.getGame(gameID);
        for (Player p : game.getPlayers()) {
            System.out.print(cli + "[" + p.getNickname() + " - " + game.getScoreboard().get(p) + "]");
        }
    }

    public void isYourTurn() {
        System.out.print(cli + "It's your turn!! You have to ");
    }
}
