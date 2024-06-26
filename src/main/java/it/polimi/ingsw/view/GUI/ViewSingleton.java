package it.polimi.ingsw.view.GUI;

import it.polimi.ingsw.network.RMI.RemoteInterface;
import it.polimi.ingsw.view.RMIView;
import it.polimi.ingsw.view.TCPView;
import it.polimi.ingsw.view.View;
import it.polimi.ingsw.view.ViewController;

import java.io.IOException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class ViewSingleton {
    private static ViewSingleton instance;
    private View view = null;
    private ViewController viewController = null;

    private ViewSingleton() {}

    public static ViewSingleton getInstance() {
        if (instance == null)
            instance = new ViewSingleton();
        return instance;
    }

    public void initialize(String connection, String IP, String port) {
        if (connection.equalsIgnoreCase("RMI")) {
            try {
                Registry registry = LocateRegistry.getRegistry(IP, 0);
                String remoteObjectName = "RMIServer";
                RemoteInterface RMIServer;
                RMIServer = (RemoteInterface) registry.lookup(remoteObjectName);
                this.view = new RMIView(RMIServer);

            } catch (RemoteException | NotBoundException e) {
                e.printStackTrace();
            }
        } else {
            try {
                this.view = new TCPView(IP, Integer.parseInt(port));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            new Thread(() -> {
                try {
                    ((TCPView) view).startClient();
                } catch (IOException | ClassNotFoundException e) {
                    Thread.currentThread().interrupt();
                }
            }).start();
        }

        viewController = new ViewController(view);
    }

    public Boolean isInitialized() { return (view != null) && (viewController != null); }

    public View getView() {
        if (view != null) return view;
        else throw new NullPointerException("view is null");
    }

    public ViewController getViewController() {
        if (viewController != null) return viewController;
        else throw new NullPointerException("view controller is null");
    }
}
