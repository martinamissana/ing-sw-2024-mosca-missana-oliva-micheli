package it.polimi.ingsw.controller;

import it.polimi.ingsw.controller.exceptions.*;
import it.polimi.ingsw.model.card.Card;
import it.polimi.ingsw.model.card.CardSide;
import it.polimi.ingsw.model.card.ResourceCard;
import it.polimi.ingsw.model.card.StarterCard;
import it.polimi.ingsw.model.chat.Message;
import it.polimi.ingsw.model.commonItem.ItemBox;
import it.polimi.ingsw.model.deck.DeckBufferType;
import it.polimi.ingsw.model.deck.DeckTypeBox;
import it.polimi.ingsw.model.exceptions.*;
import it.polimi.ingsw.model.game.*;
import it.polimi.ingsw.model.goal.*;
import it.polimi.ingsw.model.observer.events.*;
import it.polimi.ingsw.model.player.Coords;
import it.polimi.ingsw.model.player.Pawn;
import it.polimi.ingsw.model.player.PawnBuffer;
import it.polimi.ingsw.model.player.Player;

import java.io.IOException;
import java.io.Serializable;
import java.util.*;

public class Controller implements Serializable {
    private final GameHandler gh;

    /**Class constructor
     * @param gh the game handler frown which the controller will extract all the information about the model
     */
    public Controller(GameHandler gh) {
        this.gh = gh;
    }

    /**
     * getter
     * @return gh game handler
     */
    public GameHandler getGh() {
        return gh;
    }

    // TODO: add a method to terminate the game if a player disconnects, it will be activated by a message from the vie

    /**
     * adds users to user list in game handler
     * @param username of the player that joined the server
     * @throws NicknameAlreadyTakenException when in user list there is already a user with the same nickname
     */
    public synchronized void login(String username) throws NicknameAlreadyTakenException, IOException {
        gh.addUser(new Player(username));
    }
    //methods related to lobby

    /**
     * creates a new lobby adding it to the list of lobbies in GameHandler,
     * the creator needs to specify the desired amount of player for the game,
     * the creator will automatically join the lobby
     * @param numOfPlayers - the number of player needed for the game to starts
     * @param creator - the player that created the lobby
     * @throws LobbyDoesNotExistsException - this will never be thrown
     */
    public synchronized void createLobby(int numOfPlayers, String creator) throws LobbyDoesNotExistsException {
        Player lobbyCreator = null;
        for (Player p : gh.getUsers()) {
            if (p.getNickname().equals(creator)) lobbyCreator = p;
        }
        if (lobbyCreator == null) throw new UnexistentUserException();

        try {
            gh.getLobbies().put(gh.getNumOfLobbies(), new Lobby(numOfPlayers));
            gh.getLobby(gh.getNumOfLobbies()).addPlayer(lobbyCreator);
            gh.notify( new LobbyCreatedEvent(lobbyCreator, gh.getLobbies().get(gh.getNumOfLobbies()),gh.getNumOfLobbies()));
            gh.setNumOfLobbies(gh.getNumOfLobbies() + 1);
        } catch (FullLobbyException | NicknameAlreadyTakenException | IOException ignored) {}
    }

    /**
     * adds the player to the specified lobby
     * when the chosen number or player for the game is reached, a game is created
     * @param nickname - the player that will be added to the lobby
     * @param lobbyID - the lobby the player wants to join
     * @throws FullLobbyException - if the lobby is already  full
     * @throws NicknameAlreadyTakenException -  if the nickname is already taken
     * @throws LobbyDoesNotExistsException - if the lobby does not exist
     */
    public synchronized void joinLobby(String nickname, int lobbyID) throws FullLobbyException, NicknameAlreadyTakenException, LobbyDoesNotExistsException, IOException, CannotJoinMultipleLobbiesException {
        Player player = null;
        for (Player p : gh.getUsers()) {
            if (p.getNickname().equals(nickname)) player = p;
        }
        if (player == null) throw new UnexistentUserException();
        //lobbyID is the index in lobbies
        for (Lobby l :gh.getLobbies().values()){
            if(l.getPlayers().contains(player)) throw new CannotJoinMultipleLobbiesException();
        }

        if(gh.getLobbies().containsKey(lobbyID)) {
            gh.getLobbies().get(lobbyID).addPlayer(player);
            gh.notify(new LobbyJoinedEvent(player,lobbyID));
        }
        else throw new LobbyDoesNotExistsException("Lobby with ID " + lobbyID + " does not exist");
    }

    /**
     * the specified player will be removed from the lobby, automatically deletes the lobby if there are no players left
     * @param nickname  the player that will leave the lobby
     * @param lobbyID - the ID of the lobby from which the player will be deleted
     * @throws LobbyDoesNotExistsException - if the lobby does not exist
     */
    public synchronized void leaveLobby(String nickname,int lobbyID) throws LobbyDoesNotExistsException, GameDoesNotExistException, IOException {
        Player player = null;
        for (Player p : gh.getUsers()) {
            if (p.getNickname().equals(nickname)) player = p;
        }
        if (player == null) throw new UnexistentUserException();
        if(gh.getLobbies().containsKey(lobbyID)) {
           gh.getLobbies().get(lobbyID).getPlayers().remove(player);
           gh.notify(new LobbyLeftEvent(player,gh.getLobby(lobbyID),lobbyID));
           if(gh.getActiveGames().containsKey(lobbyID) && gh.getGame(lobbyID).getPlayers().contains(player)) leaveGame(lobbyID, player);
           if(gh.getLobbies().get(lobbyID).getPlayers().isEmpty()) {
               deleteLobby(lobbyID);
               gh.notify(new LobbyDeletedEvent(lobbyID));
           }

        }
        else throw new LobbyDoesNotExistsException("Lobby with ID " + lobbyID + " does not exist");
    }

    /**
     * deletes the specified lobby from the list of lobbies
     * @param lobbyID - the ID of the lobby that will be deleted
     * @throws LobbyDoesNotExistsException if the lobby does not exist
     */
    public synchronized void deleteLobby(int lobbyID) throws LobbyDoesNotExistsException, GameDoesNotExistException {
        if(gh.getLobbies().containsKey(lobbyID)) {
            gh.getLobbies().remove(lobbyID);
            if(gh.getActiveGames().containsKey(lobbyID)) gh.getActiveGames().remove(lobbyID);
        }
        else throw new LobbyDoesNotExistsException("Lobby with ID " + lobbyID + " does not exist");
    }

    //methods related to game

    /**
     * creates a new game adding it to the list of active games in GameHandler,
     * all the players of the lobby in input will be added to the game and the scoreboard is initialized
     * the order in which players joined the lobby determines their playing sequence, but the first player is randomly chosen
     * @param lobbyID ID of the lobby that starts the game
     * @throws IOException produced by failed or interrupted I/O operations
     */
    private synchronized void createGame(Integer lobbyID) throws IOException, LobbyDoesNotExistsException {
        Lobby lobby = gh.getLobby(lobbyID);

        ArrayList<Player> players = lobby.getPlayers();
        Random r = new Random();
        int i = r.nextInt(lobby.getNumOfPlayers()); // the first player is randomly chosen
        Collections.rotate(players,i);
        players.getFirst().setGoesFirst(true);


        //scoreboard initialization->all values set at 0
        HashMap<Player,Integer> scoreboard= new HashMap<>();
        for(Player p : players) {
            scoreboard.put(p,0);
        }
        //the game is instantiated and added to the list to active games
        gh.getActiveGames().put(lobbyID, new Game(lobbyID,lobby.getNumOfPlayers(),players,scoreboard));
    }

    //TODO testing e javadoc

    /**
     * removes a player from a game and terminates said game
     * @param gameID ID of the player's game
     * @param nickname who's leaving the game
     * @throws GameDoesNotExistException thrown if the specified ID doesn't correspond to any game
     * @throws LobbyDoesNotExistsException thrown if the specified ID doesn't correspond to any game, thus having no lobby
     */
    public synchronized void leaveGame(Integer gameID,String nickname) throws GameDoesNotExistException, LobbyDoesNotExistsException  {
        Player player = null;
        for (Player p : gh.getUsers()) {
            if (p.getNickname().equals(nickname)) player = p;
        }
        if (player == null) throw new UnexistentUserException();
        if(gh.getActiveGames().containsKey(gameID)) {
            gh.getActiveGames().get(gameID).getPlayers().remove(player);
            terminateGame(gameID);
        }
        else throw new GameDoesNotExistException("Lobby with ID " + gameID + " does not exist");
    }

    /**
     * deletes the specified game from the list of active games and also the associated lobby
     * @param gameID the ID of the game that will be deleted
     * @throws GameDoesNotExistException - if the game does not exist
     */
    public synchronized void terminateGame(Integer gameID) throws GameDoesNotExistException, LobbyDoesNotExistsException {
        if(gh.getActiveGames().containsKey(gameID)){
            gh.getActiveGames().remove(gameID);
            deleteLobby(gameID);
        }
        else throw new GameDoesNotExistException("Game with ID " + gameID + " does not exist");
    }

    //chat related methods

    /**
     * sends the message either to the specified receiver, or to every player in the global chat,
     * adds the message in the list of sent messages of the sender too
     * @param message the message that will be sent
     * @param ID the ID of the game where the player that sends the message is found
     * @throws GameDoesNotExistException- if the game does not exist
     */
    public synchronized void send(Message message, int ID) throws GameDoesNotExistException, LobbyDoesNotExistsException, PlayerChatMismatchException {
        if((message.getReceiver()!=null &&!gh.getLobby(ID).getPlayers().contains(message.getReceiver()) )|| !gh.getLobby(ID).getPlayers().contains(message.getSender()))
            throw new PlayerChatMismatchException();
        try {
            if (!message.isGlobal()) {
                message.getSender().getChat().getSentMessages().add(message);
                message.getReceiver().getChat().getReceivedMessages().add(message);
            } else {
                message.getSender().getChat().getSentMessages().add(message);
                ArrayList<Player> list;
                if(gh.getActiveGames().containsKey(ID)) {
                    list=gh.getGame(ID).getPlayers();
                }
                else {
                    list=gh.getLobby(ID).getPlayers();
                }
                for (Player player : list) {
                    if (player != message.getSender()) player.getChat().getReceivedMessages().add(message);
                }
            }
            message.setOrder(Message.getCounter());
            Message.setCounter(Message.getCounter()+1);
        }
        catch (GameDoesNotExistException e) {
            throw new GameDoesNotExistException("Game with ID " + ID + " does not exist");
        }
    }

    // SET UP

    /**
     * Set decks and deck buffers
     * @param gameID ID of the game played
     * @throws GameDoesNotExistException if gameID does not correspond to a game in game handler
     */
    public synchronized void setGameArea(Integer gameID) throws GameDoesNotExistException, IOException {
        Game game = gh.getGame(gameID);

        // setting decks and deck buffers:
        game.getResourceDeck().shuffle();
        game.getGoldenDeck().shuffle();
        for(DeckBufferType type : DeckBufferType.values()) game.getDeckBuffer(type).refill();
        giveStarterCards(gameID);
        setCommonGoals(gameID);
        giveGoals(gameID);

    }

    /**
     * @param gameID ID of the game played
     * @throws GameDoesNotExistException if gameID does not correspond to a game in game handler
     * @throws IOException for building the starter deck
     */
    public synchronized void giveStarterCards(Integer gameID) throws GameDoesNotExistException, IOException {
        Game game = gh.getGame(gameID);
        ArrayList<Player> players = game.getPlayers();

        // Creating starter deck + drawing starter cards for all players:
        ArrayList<StarterCard> starter = CardsPreset.getStarterCards();
        Collections.shuffle(starter);

        for (Player p : players) {
            try {
                p.getHand().addCard(starter.removeLast());
            }
            catch(HandIsFullException ignored) {}   // isn't supposed to happen
        }
    }

    /**
     * Allows the player to choose a pawn while he is in the lobby
     * @param lobbyID ID of the lobby
     * @param player who is choosing the pawn
     * @param color color of the desired pawn
     * @throws PawnAlreadyTakenException if the chosen pawn has already been taken
     * @throws LobbyDoesNotExistsException if lobbyID does not correspond to a lobby in game handler
     *
     */
    public synchronized void choosePawn (Integer lobbyID, String nickname,Pawn color) throws PawnAlreadyTakenException, LobbyDoesNotExistsException, GameAlreadyStartedException, IOException, GameDoesNotExistException {
        Player player = null;
        for (Player p : gh.getUsers()) {
             if (p.getNickname().equals(nickname)) player = nicknamef (player == null) throw new UnexistentUserException();
            if (gh.getActiveGames().containsKey(lobbyID)) throw new GameAlreadyStartedException();
            Lobby lobby = gh.getLobby(lobbyID);
            PawnBuffer pawnList = lobby.getPawnBuffer();

            if (pawnList.getPawnList().contains(color) || player.getPawn() != null) {
                pawnList.getPawnList().remove(color);
                player.setPawn(color);
                // gh.notify(new PawnAssignedEvent(player,color));
            } else throw new PawnAlreadyTakenException();

            for (Player p : lobby.getPlayers()) {
                if (p.getPawn() != Pawn.BLUE && p.getPawn() != Pawn.YELLOW && p.getPawn() != Pawn.RED && p.getPawn() != Pawn.GREEN)
                    return;
            }
            if (gh.getLobby(lobbyID).getPlayers().size() == gh.getLobby(lobbyID).getNumOfPlayers()) {
                createGame(lobbyID);
                setGameArea(lobbyID);
            }
        }
    }

    /**
     * Allows the player to choose the side of the starter card before placing it down
     * if all the players have placed the started card the game phase changes to CHOOSING PRIVATE GOAL and the hand of each player is filled
     * @param player who is playing the card
     * @param side side chosen by the player
     */
    public synchronized void chooseCardSide(Integer ID,Player player, CardSide side) throws GameDoesNotExistException, EmptyDeckException, HandIsFullException {
        if(gh.getGame(ID).getGamePhase()!=GamePhase.PLACING_STARTER_CARD) throw new WrongThreadException();
        StarterCard card = (StarterCard) player.getHand().getCard(0);
        if(!card.getSide().equals(side)) card.flip();
        player.getField().addCard(card);
        player.getHand().removeCard(card);
        for(Player p:gh.getGame(ID).getPlayers()){
            if(p.getHand().getSize()!=0)return;
        }
        gh.getGame(ID).setGamePhase(GamePhase.CHOOSING_PRIVATE_GOAL);
        fillHands(ID);
    }

    /**
     * Fill all players' hands with 2 resource cards and 1 golden card
     * @param gameID ID of the game played
     * @throws GameDoesNotExistException if gameID does not correspond to a game in game handler
     * @throws EmptyDeckException (isn't supposed to happen)
     * @throws HandIsFullException if in hands are already present other cards
     */
    public synchronized  void fillHands(Integer gameID) throws GameDoesNotExistException, EmptyDeckException, HandIsFullException {
        Game game = gh.getGame(gameID);

        for (Player p : game.getPlayers()) {
            p.getHand().addCard(game.getResourceDeck().draw());
            p.getHand().addCard(game.getResourceDeck().draw());
            p.getHand().addCard(game.getGoldenDeck().draw());
        }
    }

    /**
     * Set the common goals in the game
     * @param gameID ID of the game played
     * @throws GameDoesNotExistException if gameID does not correspond to a game in game handler
     */
    public synchronized void setCommonGoals(Integer gameID) throws GameDoesNotExistException {
        Game game = gh.getGame(gameID);

        game.setCommonGoal1(game.getGoals().getGoal());
        game.setCommonGoal2(game.getGoals().getGoal());
    }

    /**
     * adds to the choosable Goals two goals from which the player can choose
     * @param gameID ID of the game played
     */
    public synchronized void giveGoals(Integer gameID) throws GameDoesNotExistException {
        Game game = gh.getGame(gameID);

        for (Player p : game.getPlayers()) {
            p.getChoosableGoals().add(gh.getGame(gameID).getGoals().getGoal());
            p.getChoosableGoals().add(gh.getGame(gameID).getGoals().getGoal());
        }

    }

    /**
     * Allows the player to choose his personal goal
     * @param player who is choosing the personal goal
     * @param goal goal chosen by the player
     */
    public synchronized void choosePersonalGoal(Integer ID,Player player, Goal goal) throws IllegalGoalChosenException, GameDoesNotExistException, WrongGamePhaseException {
        if(gh.getGame(ID).getGamePhase()!=GamePhase.CHOOSING_PRIVATE_GOAL)  throw new WrongGamePhaseException();
        if(player.getChoosableGoals().contains(goal))player.setPrivateGoal(goal);
        else throw new IllegalGoalChosenException();
        for(Player p:gh.getGame(ID).getPlayers()){
            if(p.getPrivateGoal()==null)return;
        }
        gh.getGame(ID).setGamePhase(GamePhase.PLAYING_GAME);
    }

    // GAME

    /**
     * flips a card in a player's hand
     * @param gameID ID of the player's game
     * @param playerName nickname of the player who's flipping the card
     * @param handPos position of the card in the player's hand
     * @throws GameDoesNotExistException thrown if the specified ID doesn't correspond to any active game
     */
    public synchronized void flipCard(Integer gameID, String playerName, int handPos) throws GameDoesNotExistException {

        // get game from ID and player from nickname
        Game game = gh.getGame(gameID);
        Player player = game.getPlayers().stream()
                                         .filter(p -> p.getNickname().equals(playerName))
                                         .findFirst().orElseThrow(UnexistentUserException);

        // flip the card
        if(game.getPlayers().contains(player))
            player.getHand().getCard(handPos).flip();
    }

    /**
     * plays a card from the hand of a player to their field, at the specified position.
     * if it's the game's last round
     * @param gameID ID of the calling player's game
     * @param player player who's playing the card
     * @param handPos card's position index in the player's hand
     * @param coords position in the field to play the selected card to
     * @throws GameDoesNotExistException thrown if the given ID does not correspond to any Game
     * @throws NotYourTurnException thrown when a player tries to perform an action when it's not their turn
     * @throws IllegalActionException thrown if the selected game's current expected action isn't PLAY
     * @throws IllegalMoveException thrown if the selected card cannot be played to the field as requested
     * @throws LobbyDoesNotExistsException thrown if the game's lobby does not exist
     */
    public synchronized void playCard(Integer gameID, Player player, int handPos, Coords coords) throws GameDoesNotExistException, NotYourTurnException, IllegalActionException, IllegalMoveException, LobbyDoesNotExistsException {

        // get game from ID
        Game game = gh.getGame(gameID);

        // if it's not this player's turn
        if (!player.getNickname().equals(game.getCurrPlayer().getNickname()))
            throw new NotYourTurnException();

        // if the game's state isn't PLAY
        if (game.getAction() != Action.PLAY)
            throw new IllegalActionException();

        // get card in player's specified hand position
        ResourceCard card = (ResourceCard) player.getHand().getCard(handPos);

        // place card in the field and calculate points
        int points = player.getField().addCard(card, coords);

        // remove card from player's hand
        player.getHand().removeCard(card);

        // add points to player's score
        game.addToScore(player, points);

        // set the game's current action to DRAW after playing a card
        // only if it's not the last round (because if it is,
        // players cannot draw cards and should pass instead)
        if (!game.isLastRound())
            game.setAction(Action.DRAW);
        else
            nextTurn(gameID); // on the last round, place a card and pass. and if this is the last player, terminate game
    }

    /**
     * draws a card from a player's game's deck or deckBuffer and adds it to their hand.
     * then checks whether the game's last round has been reached, and updates the game's state accordingly.
     * @param gameID ID of the game of the drawing player
     * @param player player who's drawing the card
     * @param deckTypeBox type of card source to draw from
     * @throws GameDoesNotExistException thrown if the given ID does not correspond to any Game
     * @throws NotYourTurnException thrown when a player tries to perform an action when it's not their turn
     * @throws IllegalActionException thrown if the selected game's current expected action isn't DRAW
     * @throws HandIsFullException thrown if the selected game's current player has their hand full
     * @throws EmptyDeckException thrown if the selected deck is out of cards
     * @throws EmptyBufferException thrown if the selected deck buffer is out of cards
     */
    public synchronized void drawCard(Integer gameID, Player player, DeckTypeBox deckTypeBox) throws GameDoesNotExistException, NotYourTurnException, IllegalActionException, HandIsFullException, EmptyDeckException, EmptyBufferException, LobbyDoesNotExistsException {

        // get game from ID
        Game game = gh.getGame(gameID);

        // if it's not this player's turn
        if (!player.getNickname().equals(game.getCurrPlayer().getNickname()))
            throw new NotYourTurnException();

        // if the game's state isn't DRAW
        if (game.getAction() != Action.DRAW)
            throw new IllegalActionException();

        // draw a card and add it to the current player's hand
        ResourceCard newCard = game.drawFromSource(deckTypeBox);
        player.getHand().addCard(newCard);

        // set the game's current action to PLAY after drawing a card
        game.setAction(Action.PLAY);

        // update the game's current player and check
        // whether the game's last round has been reached or not
        nextTurn(gameID);
    }

    /**
     * updates the game's "whoseTurn" attribute and sets it to the next player's playerID.
     * at the end of every round, this method checks if the game has reached its last round
     * and updates the game's lastRound parameter accordingly.
     * when the last round is completed, the winner is declared and the game is terminated.
     * @param gameID ID of the game to advance the turn of
     * @throws GameDoesNotExistException thrown if the given ID does not correspond to any Game
     */
    public synchronized void nextTurn(Integer gameID) throws GameDoesNotExistException, LobbyDoesNotExistsException {

        // get game from ID
        Game game = gh.getGame(gameID);

        // if last round has finished, declare the winner and terminate the game
        if(game.isLastRound() && game.getWhoseTurn() == game.getNumOfPlayers()-1) {
            winner(gameID);
            terminateGame(gameID);
        }

        // if the current player is the last in the round,
        // update the game's status to last round if needed
        if(game.getWhoseTurn() == game.getNumOfPlayers()-1)
            updateLastRound(gameID);

        // update turn counter
        game.setWhoseTurn((game.getWhoseTurn()+1) % game.getNumOfPlayers());

    }

    /**
     * updates the game's state, setting it to its last round if a player
     * has reached 20 points or if there's no remaining cards in both decks
     * @param gameID - the ID of the game where the status is checked
     */
    public synchronized void updateLastRound(Integer gameID) {
        Game game = gh.getActiveGames().get(gameID);

        // if it's already last round there's no need to update
        if (game.isLastRound()) return;

        // if both decks are empty
        if (game.getResourceDeck().getCards().isEmpty() &&
                game.getGoldenDeck().getCards().isEmpty())
            game.setLastRound(true);

        // if someone has reached 20 points
        for(Map.Entry<Player, Integer> entry : game.getScoreboard().entrySet())
            if(entry.getValue() >= 20)
                game.setLastRound(true);
    }

    // FINAL PHASE

    /**
     * checks in the field the private goal of the players and adds the points to the scoreboard
     * @param gameID index of the game where the evaluation is done
     */
    private  synchronized void evaluatePrivateGoal(Integer gameID){
        Game game=gh.getActiveGames().get(gameID);
        for(Player p:game.getPlayers()){
            if(p.getPrivateGoal().getClass()== ResourceGoal.class){
                resourceEvaluator(gameID, (ResourceGoal) p.getPrivateGoal(),p);
            } else if (p.getPrivateGoal().getClass()== L_ShapeGoal.class) {
                L_ShapeEvaluator(gameID, (L_ShapeGoal) p.getPrivateGoal(),p);
            }
            else{
                diagonalEvaluator(gameID, (DiagonalGoal) p.getPrivateGoal(),p);
            }

        }
    }

    /**
     * checks in the field of every player the common goals and adds the points to the scoreboard
     * @param gameID index of the game where the evaluation is done
     */
    private synchronized void evaluateCommonGoal(Integer gameID){
        Game game=gh.getActiveGames().get(gameID);
        Goal commonGoal1= game.getCommonGoal1();
        Goal commonGoal2= game.getCommonGoal2();
        for(Player p: game.getPlayers()){
            if(commonGoal1.getClass()==ResourceGoal.class){
                resourceEvaluator(gameID, (ResourceGoal) commonGoal1,p);
            } else if (commonGoal1.getClass()==L_ShapeGoal.class) {
                L_ShapeEvaluator(gameID, (L_ShapeGoal) commonGoal1,p);
            }
            else{
                diagonalEvaluator(gameID, (DiagonalGoal) commonGoal1,p);
            }
            if(commonGoal2.getClass()==ResourceGoal.class){
                resourceEvaluator(gameID, (ResourceGoal) commonGoal2,p);
            } else if (commonGoal2.getClass()==L_ShapeGoal.class) {
                L_ShapeEvaluator(gameID, (L_ShapeGoal) commonGoal2,p);
            }
            else{
                diagonalEvaluator(gameID, (DiagonalGoal) commonGoal2,p);
            }
        }
    }

    /**
     * adds the list of winners (players with the highest score) of the game after the evaluation of the private and the common goals
     * @param gameID index of the game where the evaluation is done
     */
    public synchronized void winner(Integer gameID){
        Game game=gh.getActiveGames().get(gameID);
        evaluatePrivateGoal(gameID);
        evaluateCommonGoal(gameID);
        HashMap<Player,Integer> scoreboard=game.getScoreboard();
        ArrayList<Player> winners=new ArrayList<>();
        int maxValue = 0;
        for (Map.Entry<Player, Integer> entry : scoreboard.entrySet()) {
            if (entry.getValue() > maxValue) {
                maxValue = entry.getValue();
            }
        }
        for(Player p:scoreboard.keySet()){
            if(scoreboard.get(p)==maxValue) winners.add(p);
        }
        game.setWinners(winners);
    }

    /**
     * checks the presence of the Diagonal goal in the field and adds the points to the scoreboard
     * @param gameID index of the game where the evaluation is done
     * @param goal goal that needs to be found in the field
     * @param player player who has to complete the goal
     */
    private synchronized void diagonalEvaluator(Integer gameID, DiagonalGoal goal, Player player) {
        Game game=gh.getActiveGames().get(gameID);
        HashMap<Coords, Card> field = player.getField().getMatrix();
        HashSet<Coords> done = new HashSet<>(); // indicates the cards already used for the goal
        if (goal.getType() == DiagonalGoalType.UPWARD) {
            for (Coords coords : field.keySet()) {
                Coords secondCard = new Coords(coords.getX() + 1, coords.getY());
                Coords thirdCard = new Coords(coords.getX() + 2, coords.getY());
                if (!done.contains(coords) && !done.contains(secondCard) && !done.contains(thirdCard) && field.get(coords).getKingdom() == goal.getColor() && field.containsKey(secondCard) && field.containsKey(thirdCard) && field.get(secondCard).getKingdom() == goal.getColor() && field.get(thirdCard).getKingdom() == goal.getColor()) {
                    done.add(coords);
                    done.add(secondCard);
                    done.add(thirdCard);
                    game.addToScore(player, goal.getPoints());
                }
            }
        } else {
            for (Coords coords : field.keySet()) {
                Coords secondCard = new Coords(coords.getX(), coords.getY() + 1);
                Coords thirdCard = new Coords(coords.getX(),coords.getY() + 2);
                if (!done.contains(coords) && !done.contains(secondCard) && !done.contains(thirdCard) && field.get(coords).getKingdom() == goal.getColor() && field.containsKey(secondCard) && field.containsKey(thirdCard) && field.get(secondCard).getKingdom() == goal.getColor() && field.get(thirdCard).getKingdom() == goal.getColor()) {
                    done.add(coords);
                    done.add(secondCard);
                    done.add(thirdCard);
                    game.addToScore(player, goal.getPoints());
                }
            }
        }
    }

    /**
     * checks the presence of the Resource goal in the field and adds the points to the scoreboard
     * @param gameID index of the game where the evaluation is done
     * @param goal goal that needs to be found in the field
     * @param player player who has to complete the goal
     */
    private synchronized void resourceEvaluator(Integer gameID, ResourceGoal goal, Player player) {
        Game game = gh.getActiveGames().get(gameID);
        HashMap<ItemBox, Integer> totalResources = player.getField().getTotalResources();
        while (true) {
            for (ItemBox item : goal.getResourceList()) {
                if (totalResources.get(item) == 0) return;
                totalResources.replace(item, totalResources.get(item) - 1);
            }
            game.addToScore(player, goal.getPoints());
        }

    }

    /**
     * checks the presence of the L_Shape goal in the field and adds the points to the scoreboard
     * @param gameID index of the game where the evaluation is done
     * @param goal goal that needs to be found in the field
     * @param player player who has to complete the goal
     */
    private synchronized void L_ShapeEvaluator(Integer gameID,L_ShapeGoal goal,Player player){
        HashSet<Coords> done=new HashSet<>();
        HashMap<Coords, Card> field=player.getField().getMatrix();
        switch (goal.getType()){
            case UP_RIGHT -> {
                for(Coords coords: field.keySet()){
                    Coords secondCard=new Coords(coords.getX()-1,coords.getY());
                    Coords thirdCard=new Coords(coords.getX()-2,coords.getY()-1);
                    genericL_ShapeEvaluator(gameID,goal,player,coords,secondCard,thirdCard,done);
                }
            }
            case UP_LEFT -> {
                for(Coords coords: field.keySet()){
                    Coords secondCard=new Coords(coords.getX(),coords.getY()-1);
                    Coords thirdCard=new Coords(coords.getX()-1,coords.getY()-2);
                    genericL_ShapeEvaluator(gameID,goal,player,coords,secondCard,thirdCard,done);
                }
            }
            case DOWN_LEFT -> {
                for(Coords coords: field.keySet()){
                    Coords secondCard=new Coords(coords.getX()+1,coords.getY());
                    Coords thirdCard=new Coords(coords.getX()+2,coords.getY()+1);
                    genericL_ShapeEvaluator(gameID,goal,player,coords,secondCard,thirdCard,done);
                }
            }
            case DOWN_RIGHT -> {
                for(Coords coords: field.keySet()){
                    Coords secondCard=new Coords(coords.getX(),coords.getY()+1);
                    Coords thirdCard=new Coords(coords.getX()+1,coords.getY()+2);
                    genericL_ShapeEvaluator(gameID,goal,player,coords,secondCard,thirdCard,done);
                }
            }
        }
    }

    /**
     * useful method to avoid repetition of code based on the direction of L_Shape goal
     * @param gameID index of the game where the evaluation is done
     * @param goal goal that needs to be found in the field
     * @param player player who has to complete the goal
     * @param firstCard coordinates of the card with a different color from the other two in the L_Shape
     * @param secondCard coordinates of one of the cards with the main L_Shape color
     * @param thirdCard coordinates of one of the cards with the main L_Shape color
     * @param done HashMap that maps the cards already used to complete the goal
     */
    private synchronized void genericL_ShapeEvaluator(Integer gameID,L_ShapeGoal goal,Player player,Coords firstCard,Coords secondCard,Coords thirdCard,HashSet<Coords> done){
        Game game=gh.getActiveGames().get(gameID);
        HashMap<Coords, Card> field=player.getField().getMatrix();
        if(!done.contains(firstCard)&&!done.contains(secondCard)&&!done.contains(thirdCard)&&field.get(firstCard).getKingdom()==goal.getSecondaryColor()&&field.containsKey(secondCard)&&field.containsKey(thirdCard)&&field.get(secondCard).getKingdom()==goal.getMainColor()&&field.get(thirdCard).getKingdom()==goal.getMainColor()){
            done.add(firstCard);
            done.add(secondCard);
            done.add(thirdCard);
            game.addToScore(player, goal.getPoints());
        }
    }

}
