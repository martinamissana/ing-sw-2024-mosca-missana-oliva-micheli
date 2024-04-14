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

    public Controller(GameHandler gh) {
        this.gh = gh;
    }

    public GameHandler getGh() {
        return gh;
    }



    //chat related methods
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
    public void createLobby(int numOfPlayers, Player lobbyCreator) throws LobbyDoesNotExistsException, FullLobbyException, NicknameAlreadyTakenException {
        gh.getLobbies().put(gh.getNumOfLobbies(),new Lobby(numOfPlayers));
        gh.getLobby(gh.getNumOfLobbies()).addPlayer(lobbyCreator);
        gh.setNumOfLobbies(gh.getNumOfLobbies()+1);

    }
    public void joinLobby(Player player,int lobbyID ) throws FullLobbyException, NicknameAlreadyTakenException, LobbyDoesNotExistsException {//avrebbe senso creare i player prima?
        //lobbyID is the index in lobbies
        if(gh.getLobbies().get(lobbyID)!=null)gh.getLobbies().get(lobbyID).addPlayer(player);
        else throw new LobbyDoesNotExistsException("Lobby with ID " + lobbyID + " does not exist");
    }
    public void leaveLobby(Player player,int lobbyID) throws LobbyDoesNotExistsException {
        if(gh.getLobbies().get(lobbyID)!=null)gh.getLobbies().get(lobbyID).getPlayers().remove(player);
        else throw new LobbyDoesNotExistsException("Lobby with ID " + lobbyID + " does not exist");
    }

    public void deleteLobby(int lobbyID) throws LobbyDoesNotExistsException {
        if(gh.getLobbies().get(lobbyID)!=null)gh.getLobbies().remove(lobbyID);
        else throw new LobbyDoesNotExistsException("Lobby with ID " + lobbyID + " does not exist");
    }

    //methods related to game
    public void deleteGame(Integer gameID) throws GameDoesNotExistException {
        if(gh.getActiveGames().containsKey(gameID))gh.getActiveGames().remove(gameID);
        else throw new GameDoesNotExistException("Game with ID " + gameID + " does not exist");
    }

    public void createGame(Lobby lobby) throws IOException {
        HashMap<Integer,Player> playerIntegerHashMap=new HashMap<>();
        ArrayList<Player> players=lobby.getPlayers();
        int i=0;
        for(Player p:players) {
            if(i==0) {//the first player in the lobby is also the first in the round
                playerIntegerHashMap.put(i,p);
                p.setGoesFirst(true);
            }
            playerIntegerHashMap.put(i,p);
            i++;
        }
        //scoreboard initialization->all values set at 0
        HashMap<Player,Integer> scoreboard= new HashMap<>();
        for(Integer key : playerIntegerHashMap.keySet()){
            scoreboard.put(playerIntegerHashMap.get(key),0);
        }
        gh.getActiveGames().put(gh.getNumOfGames(), new Game(gh.getNumOfGames(),lobby.getNumOfPlayers(),playerIntegerHashMap,scoreboard));
        gh.setNumOfGames(gh.getNumOfGames()+1);
    }




}
