package it.polimi.ingsw.controller;

import it.polimi.ingsw.model.chat.Message;
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
import java.util.ArrayList;
import java.util.HashMap;

public class Controller implements Serializable {
    GameHandler gh;

    /**lass constructor
     * @param gh
     */
    public Controller(GameHandler gh) {
        this.gh = gh;
    }

    /**
     * gets the GameHandler
     * @return gh
     */
    public GameHandler getGh() {
        return gh;
    }

    //chat related methods

    /**
     * sends the message either to the specified receiver, or to every player in the global chat,
     * adds the message in the list of sent messages of the sender too
     * @param message
     * @param game
     * @throws GameDoesNotExistException
     */
    public void send(Message message, int game) throws GameDoesNotExistException {
        try {
            if (!message.isGlobal()) {
                message.getSender().getChat().getSentMessages().add(message);
                message.getReceiver().getChat().getReceivedMessages().add(message);
            } else {
                message.getSender().getChat().getSentMessages().add(message);
                for (Player player : gh.getGame(game).getPlayers().values()) {
                    if (player != message.getSender()) player.getChat().getReceivedMessages().add(message);
                }
            }
        }
            catch (GameDoesNotExistException e) {
                    throw new GameDoesNotExistException("Game with ID " + game + " does not exist");
                }
    }

    //methods related to lobby

    /**
     * creates a new lobby adding it to the list of lobbies in GameHandler,
     * the creator needs to specify the desired amount of player for the game,
     * the creator will automatically join the lobby
     * @param numOfPlayers
     * @param lobbyCreator
     * @throws LobbyDoesNotExistsException
     * @throws FullLobbyException
     * @throws NicknameAlreadyTakenException
     */
    public void createLobby(int numOfPlayers, Player lobbyCreator) throws LobbyDoesNotExistsException, FullLobbyException, NicknameAlreadyTakenException {
        gh.getLobbies().put(gh.getNumOfLobbies(),new Lobby(numOfPlayers));
        gh.getLobby(gh.getNumOfLobbies()).addPlayer(lobbyCreator);
        gh.setNumOfLobbies(gh.getNumOfLobbies()+1);

    }
    /**
     * adds the player to the specified lobby
     * @param player
     * @param lobbyID
     * @throws FullLobbyException if the lobby is full
     * @throws NicknameAlreadyTakenException if the nickname is already taken
     * @throws LobbyDoesNotExistsException if the lobby does not exist
     */
    public void joinLobby(Player player,int lobbyID ) throws FullLobbyException, NicknameAlreadyTakenException, LobbyDoesNotExistsException {//avrebbe senso creare i player prima?
        //lobbyID is the index in lobbies
        if(gh.getLobbies().get(lobbyID)!=null)gh.getLobbies().get(lobbyID).addPlayer(player);
        else throw new LobbyDoesNotExistsException("Lobby with ID " + lobbyID + " does not exist");
    }

    /**
     * the specified player will be removed from the lobby
     * @param player
     * @param lobbyID
     * @throws LobbyDoesNotExistsException if the lobby does not exist
     */
    public void leaveLobby(Player player,int lobbyID) throws LobbyDoesNotExistsException {
        if(gh.getLobbies().get(lobbyID)!=null)gh.getLobbies().get(lobbyID).getPlayers().remove(player);
        else throw new LobbyDoesNotExistsException("Lobby with ID " + lobbyID + " does not exist");
    }

    /**
     * deletes the specified lobby from the list of lobbies
     * @param lobbyID
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
     * the order in which players joined the lobby determines their playing sequence
     * @param lobby
     * @throws IOException
     */
    public void createGame(Lobby lobby) throws IOException {
        HashMap<Integer,Player> playerIntegerHashMap=new HashMap<>();
        ArrayList<Player> players=lobby.getPlayers();
        int i=0;
        for(Player p:players) {                 //copies the players from the lobby to the HashMap that will be given in input to the Game constructor
            if(i==0) {                          //the first player in the lobby is also the first in the round, and so on for the other players
                playerIntegerHashMap.put(i,p);
                p.setGoesFirst(true);
            }
            else {playerIntegerHashMap.put(i,p);}
            i++;
        }
        //scoreboard initialization->all values set at 0
        HashMap<Player,Integer> scoreboard= new HashMap<>();
        for(Integer key : playerIntegerHashMap.keySet()){
            scoreboard.put(playerIntegerHashMap.get(key),0);
        }
        //the game is instantiated and added to the list to active games
        gh.getActiveGames().put(gh.getNumOfGames(), new Game(gh.getNumOfGames(),lobby.getNumOfPlayers(),playerIntegerHashMap,scoreboard));
        gh.setNumOfGames(gh.getNumOfGames()+1);
    }
    /**
     * deletes the specified game from the list of active games
     * @param gameID
     * @throws GameDoesNotExistException if the game does not exist
     */
    public void deleteGame(Integer gameID) throws GameDoesNotExistException {
        if(gh.getActiveGames().containsKey(gameID))gh.getActiveGames().remove(gameID);
        else throw new GameDoesNotExistException("Game with ID " + gameID + " does not exist");
    }
}
