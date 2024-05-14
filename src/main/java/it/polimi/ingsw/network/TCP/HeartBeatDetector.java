package it.polimi.ingsw.network.TCP;

import it.polimi.ingsw.controller.Controller;
import it.polimi.ingsw.controller.exceptions.UnexistentUserException;
import it.polimi.ingsw.model.exceptions.GameDoesNotExistException;
import it.polimi.ingsw.model.exceptions.LobbyDoesNotExistsException;

import java.io.IOException;
import java.net.Socket;

public class HeartBeatDetector  {
    private final TCPVirtualView vv;
    private final Controller c;
    private final Socket socket;
    long start;
    Thread thread;
    public HeartBeatDetector(TCPVirtualView vv, Controller c, Socket socket) {
        System.out.println("Detector created: " + vv.getNickname());
        this.vv = vv;
        this.c = c;
        this.socket = socket;
        start = System.currentTimeMillis();
        new Thread(() -> {
            while(true){
                try {
                    if (!check()) break;
                } catch (IOException e ) {
                    throw new RuntimeException(e);
                } catch (GameDoesNotExistException | UnexistentUserException | LobbyDoesNotExistsException e){

                }
            }
        }).start();
    }


    public Controller getC() {
        return c;
    }

    public Socket getSocket() {
        return socket;
    }

    public boolean check() throws LobbyDoesNotExistsException, IOException, GameDoesNotExistException, UnexistentUserException {
        if (System.currentTimeMillis() - start > 10000) {
            System.out.println("no heartbeat");
            try {
                vv.getIn().close();
                vv.getOut().close();
                socket.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            if(vv.getID()!=null)c.leaveLobby(vv.getNickname(),vv.getID());
            c.getGh().removeUser(vv.getNickname());
            return false;
        }
        return true;
    }

    public void resetTimer(){
        start = System.currentTimeMillis();
    }
}
