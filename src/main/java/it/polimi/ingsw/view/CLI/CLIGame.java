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
    public static final String ResourcesColor = "\u001B[30;43m"; // Gold
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
                case Resource.INKWELL -> ResourcesColor + " I " + reset;
                case Resource.MANUSCRIPT -> ResourcesColor + " M " + reset;
                case Resource.QUILL -> ResourcesColor + " Q " + reset;
                case Kingdom.FUNGI -> FungiColor + " F " + reset;
                case Kingdom.PLANT -> PlantColor + " P " + reset;
                case Kingdom.ANIMAL -> AnimalColor + " A " + reset;
                case Kingdom.INSECT -> InsectColor + " I " + reset;
                case CornerStatus.EMPTY -> StarterColor + "   " + reset;
                case null, default -> KingdomToColor(card.getKingdom()) + "   " + reset;
            };
        }
        else return switch (item) {
            case Resource.INKWELL -> ResourcesColor + " I " + reset;
            case Resource.MANUSCRIPT -> ResourcesColor + " M " + reset;
            case Resource.QUILL -> ResourcesColor + " Q " + reset;
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

        int points = 0;
        if (card.getClass().getName().equals("it.polimi.ingsw.model.card.ResourceCard") || card.getClass().getName().equals("it.polimi.ingsw.model.card.GoldenCard")) {
            points = ((ResourceCard) card).getPoints();
        }

        if (points != 0) return west + kingdom + "   " + reset + ResourcesColor + points + reset + kingdom + "   " + reset + north;
        else return west + kingdom + "       " + reset + north;
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
                case AnimalColor -> "A";
                case FungiColor -> "F";
                case InsectColor -> "I";
                case PlantColor -> "P";
                default -> throw new IllegalStateException("Unexpected value: " + KingdomToColor(card.getKingdom()));
            };
        } else kingdom = " ";

        return KingdomToColor(card.getKingdom()) + "      " + kingdom + "      " + reset;
    }

    private String printLower(Card card) {
        if (card.getClass().getName().equals("it.polimi.ingsw.model.card.CardBlock"))
            return CardBlockColor + "             " + reset;

        String kingdom = KingdomToColor(card.getKingdom());

        String south = ItemToString(card, CornerType.SOUTH);
        String east = ItemToString(card, CornerType.EAST);

        return south + kingdom + "       " + kingdom + east;
    }

    public void printCard(Card card) {
        System.out.println(cli + printUpper(card) + cli + printMiddle(card) + cli + printLower(card) + "\n");
    }

    public void printHand(Player player) {
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
        System.out.print(cli + "Resource Deck (" + game.getDeck(DeckType.RESOURCE).getCards().size() + ") + card spaces:" + cli);

        int i;
        for (i = 0; i < game.getDeck(DeckType.RESOURCE).getCards().size(); i++) {
            System.out.print(KingdomToColor(game.getDeck(DeckType.RESOURCE).getCards().get(i).getKingdom()) + "|" + reset);
            if (i == game.getDeck(DeckType.RESOURCE).getCards().size() - 1) System.out.print(KingdomToColor(game.getDeck(DeckType.RESOURCE).getCards().get(i).getKingdom()) + "  |" + reset + cli);
        }
        for (i = 0; i < game.getDeck(DeckType.RESOURCE).getCards().size(); i++) {
            System.out.print(KingdomToColor(game.getDeck(DeckType.RESOURCE).getCards().get(i).getKingdom()) + "|" + reset);
            if (i == game.getDeck(DeckType.RESOURCE).getCards().size() - 1)  System.out.println(KingdomToColor(game.getDeck(DeckType.RESOURCE).getCards().get(i).getKingdom()) + "  |" + reset);
        }

        System.out.print(cli + "Resource Card spaces:");
        System.out.print(cli + printUpper(game.getDeckBuffer(DeckBufferType.RES1).getCard()) + "\t" + printUpper(game.getDeckBuffer(DeckBufferType.RES2).getCard()));
        System.out.print(cli + printMiddle(game.getDeckBuffer(DeckBufferType.RES1).getCard()) + "\t" + printMiddle(game.getDeckBuffer(DeckBufferType.RES2).getCard()));
        System.out.println(cli + printLower(game.getDeckBuffer(DeckBufferType.RES1).getCard()) + "\t" + printLower(game.getDeckBuffer(DeckBufferType.RES2).getCard()));


        System.out.print(cli + "Golden Deck (" + game.getDeck(DeckType.GOLDEN).getCards().size() + ") + card spaces:" + cli);
        for (i = 0; i < game.getDeck(DeckType.GOLDEN).getCards().size(); i++) {
            System.out.print(KingdomToColor(game.getDeck(DeckType.GOLDEN).getCards().get(i).getKingdom()) + "|" + reset);
            if (i == game.getDeck(DeckType.GOLDEN).getCards().size() - 1) System.out.print(KingdomToColor(game.getDeck(DeckType.GOLDEN).getCards().get(i).getKingdom()) + "  |" + reset + cli);
        }
        for (i = 0; i < game.getDeck(DeckType.GOLDEN).getCards().size(); i++) {
            System.out.print(KingdomToColor(game.getDeck(DeckType.GOLDEN).getCards().get(i).getKingdom()) + "|" + reset);
            if (i == game.getDeck(DeckType.GOLDEN).getCards().size() - 1) System.out.println(KingdomToColor(game.getDeck(DeckType.GOLDEN).getCards().get(i).getKingdom()) + "  |" + reset);
        }

        System.out.print(cli + "Golden Card spaces:");
        System.out.print(cli + printUpper(game.getDeckBuffer(DeckBufferType.GOLD1).getCard()) + "\t" + printUpper(game.getDeckBuffer(DeckBufferType.GOLD2).getCard()));
        System.out.print(cli + printMiddle(game.getDeckBuffer(DeckBufferType.GOLD1).getCard()) + "\t" + printMiddle(game.getDeckBuffer(DeckBufferType.GOLD2).getCard()));
        System.out.println(cli + printLower(game.getDeckBuffer(DeckBufferType.GOLD1).getCard()) + "\t" + printLower(game.getDeckBuffer(DeckBufferType.GOLD2).getCard()));
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
