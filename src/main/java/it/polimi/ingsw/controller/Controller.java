package it.polimi.ingsw.controller;

import it.polimi.ingsw.controller.exceptions.GameAlreadyStartedException;
import it.polimi.ingsw.model.chat.Message;
import it.polimi.ingsw.model.deck.DeckType;
import it.polimi.ingsw.model.exceptions.FullLobbyException;
import it.polimi.ingsw.model.exceptions.GameDoesNotExistException;
import it.polimi.ingsw.model.exceptions.LobbyDoesNotExistsException;
import it.polimi.ingsw.model.exceptions.NicknameAlreadyTakenException;
import it.polimi.ingsw.model.game.Game;
import it.polimi.ingsw.model.game.GameHandler;
import it.polimi.ingsw.model.game.Lobby;
import it.polimi.ingsw.model.player.Player;

import java.io.IOException;
import java.io.Serializable;
import java.util.*;

public class Controller implements Serializable {
    GameHandler gh;

    /**Class constructor
     * @param gh - the game handler frown which the controller will extract all the information about the model
     */
    public Controller(GameHandler gh) {
        this.gh = gh;
    }

    /**
     * getter
     * @return gh - game handler
     */
    public GameHandler getGh() {
        return gh;
    }

    //methods related to lobby
    /**
     * creates a new lobby adding it to the list of lobbies in GameHandler,
     * the creator needs to specify the desired amount of player for the game,
     * the creator will automatically join the lobby
     * @param numOfPlayers - the number of player needed for the game to starts
     * @param lobbyCreator - the player that created the lobby
     * @throws LobbyDoesNotExistsException - this will never be thrown
     * @throws FullLobbyException - this will never be thrown
     * @throws NicknameAlreadyTakenException - this will never be thrown
     */
    public void createLobby(int numOfPlayers, Player lobbyCreator) throws LobbyDoesNotExistsException, FullLobbyException, NicknameAlreadyTakenException {
        gh.getLobbies().put(gh.getNumOfLobbies(),new Lobby(numOfPlayers));
        gh.getLobby(gh.getNumOfLobbies()).addPlayer(lobbyCreator);
        gh.setNumOfLobbies(gh.getNumOfLobbies()+1);

    }
    /**
     * adds the player to the specified lobby
     * when the chosen number or player for the game is reached, a game is created
     * @param player - the player that will be added to the lobby
     * @param lobbyID - the lobby the player wants to join
     * @throws FullLobbyException - if the lobby is already  full
     * @throws NicknameAlreadyTakenException -  if the nickname is already taken
     * @throws LobbyDoesNotExistsException - if the lobby does not exist
     */
    public void joinLobby(Player player,int lobbyID ) throws FullLobbyException, NicknameAlreadyTakenException, LobbyDoesNotExistsException, IOException {//avrebbe senso creare i player prima?
        //lobbyID is the index in lobbies
        if(gh.getLobbies().containsKey(lobbyID)){
            gh.getLobbies().get(lobbyID).addPlayer(player);
            if(gh.getLobbies().get(lobbyID).getPlayers().size() == gh.getLobbies().get(lobbyID).getNumOfPlayers())
                this.createGame(gh.getLobbies().get(lobbyID));//in the last player has joined, the game is created
        }
        else throw new LobbyDoesNotExistsException("Lobby with ID " + lobbyID + " does not exist");
    }

    /**
     * the specified player will be removed from the lobby, automatically deletes the lobby if there are no players left
     * @param player - the player that will leave the lobby
     * @param lobbyID - the ID of the lobby from which the player will be deleted
     * @throws LobbyDoesNotExistsException - if the lobby does not exist
     */
    public void leaveLobby(Player player,int lobbyID) throws LobbyDoesNotExistsException, GameAlreadyStartedException {
        //if the game has already started the player will not be removed
        if(gh.getActiveGames().containsKey(lobbyID)) throw new GameAlreadyStartedException();
        else if(gh.getLobbies().containsKey(lobbyID)){
            gh.getLobbies().get(lobbyID).getPlayers().remove(player);
            if(gh.getLobbies().get(lobbyID).getPlayers().isEmpty())deleteLobby(lobbyID);
        }
        else throw new LobbyDoesNotExistsException("Lobby with ID " + lobbyID + " does not exist");
    }

    /**
     * deletes the specified lobby from the list of lobbies
     * @param lobbyID - the ID of the lobby that will be deleted
     * @throws LobbyDoesNotExistsException if the lobby does not exist
     */
    public void deleteLobby(int lobbyID) throws LobbyDoesNotExistsException {
        if(gh.getLobbies().get(lobbyID)!=null)gh.getLobbies().remove(lobbyID);
        else throw new LobbyDoesNotExistsException("Lobby with ID " + lobbyID + " does not exist");
    }

    //methods related to game

    /**
     * creates a new game adding it to the list of active games in GameHandler,
     * all the players of the lobby in input will be added to the game and the scoreboard is initialized
     * the order in which players joined the lobby determines their playing sequence, but the first player is randomly chosen
     * @param lobby - the lobby that starts the game
     * @throws IOException - produced by failed or interrupted I/O operations
     */
    private void createGame(Lobby lobby) throws IOException {
        ArrayList<Player> players=lobby.getPlayers();
        Random r=new Random();
        int i=r.nextInt(lobby.getNumOfPlayers());//the first player is randomly chosen
        Collections.rotate(players,i);
        players.get(0).setGoesFirst(true);

        //scoreboard initialization->all values set at 0
        HashMap<Player,Integer> scoreboard= new HashMap<>();
        for(Player p : players){
            scoreboard.put(p,0);
        }
        //the game is instantiated and added to the list to active games
        gh.getActiveGames().put(gh.getNumOfGames(), new Game(gh.getNumOfGames(),lobby.getNumOfPlayers(),players,scoreboard));
        gh.setNumOfGames(gh.getNumOfGames()+1);
    }
    /**
     * deletes the specified game from the list of active games and also the associated lobby
     * @param gameID - the ID of the game that will be deleted
     * @throws GameDoesNotExistException - if the game does not exist
     */
    public void terminateGame(Integer gameID) throws GameDoesNotExistException, LobbyDoesNotExistsException {
        if(gh.getActiveGames().containsKey(gameID)){
            gh.getActiveGames().remove(gameID);
            deleteLobby(gameID);
        }
        else throw new GameDoesNotExistException("Game with ID " + gameID + " does not exist");
    }

    /**
     * will be called at the end of each round
     * @param gameID - the ID of the game where the status is checked
     */
    //method related to game phases
    public void checkGameStatus(Integer gameID) throws GameDoesNotExistException, LobbyDoesNotExistsException {
        Game game=gh.getActiveGames().get(gameID);
        //if last round has finished
        if(game.isLastRound()) {
            //c.winner(gh.getGame(gameID));
            terminateGame(gameID);
            return;
        }
        //if someone has reached 20 points
        for(Player p:game.getPlayers()){
            if(game.getScoreboard().get(p)>=20)game.setLastRound(true);
        }
        //if both decks are empty
        if(gh.getGame(gameID).getDeck(DeckType.RESOURCE).getCards().isEmpty()&&gh.getGame(gameID).getDeck(DeckType.GOLDEN).getCards().isEmpty())
            game.setLastRound(true);
    }

    //chat related methods
    /**
     * sends the message either to the specified receiver, or to every player in the global chat,
     * adds the message in the list of sent messages of the sender too
     * @param message - the message that will be sent
     * @param ID - the ID of the game where the player that sends the message is found
     * @throws GameDoesNotExistException- if the game does not exist
     */
    public void send(Message message, int ID) throws GameDoesNotExistException, LobbyDoesNotExistsException {
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


}
