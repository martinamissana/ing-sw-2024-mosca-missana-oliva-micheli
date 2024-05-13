package it.polimi.ingsw.network.TCP;

import it.polimi.ingsw.controller.Controller;

import java.io.IOException;
import java.net.Socket;

public class HeartBeatDetector  {
    private final TCPVirtualView vv;
    private final Controller c;
    private final Socket socket;
    long start;
    Thread thread;
    public HeartBeatDetector(TCPVirtualView vv, Controller c, Socket socket) {
        this.vv = vv;
        this.c = c;
        this.socket = socket;
        start = System.currentTimeMillis();
        new Thread(() -> {
            while(check()){}
        }).start();
    }


    public Controller getC() {
        return c;
    }

    public Socket getSocket() {
        return socket;
    }

    public synchronized boolean check() {
        if (System.currentTimeMillis() - start > 10000) {
            System.out.println("no heartbeat");
            try {
                vv.getIn().close();
                vv.getOut().close();
                socket.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            c.getGh().removeUser(vv.getNickname());
            return false;
        }
        return true;
    }

    public void resetTimer(){
        start = System.currentTimeMillis();
    }
}
