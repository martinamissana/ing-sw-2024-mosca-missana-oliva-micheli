package it.polimi.ingsw.view.GUI;

import it.polimi.ingsw.network.RMI.ClientRemoteInterface;
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
import java.rmi.server.UnicastRemoteObject;

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

    public void initialize(Boolean isRMI) throws NotBoundException, IOException {
        if (view == null) {
            if (isRMI) {
                try {
                    Registry registry;
                    registry = LocateRegistry.getRegistry();
                    String remoteObjectName = "RMIServer";
                    RemoteInterface RMIServer;
                    RMIServer = (RemoteInterface) registry.lookup(remoteObjectName);
                    view = new RMIView(RMIServer);
                    RMIServer.connect((ClientRemoteInterface) UnicastRemoteObject.exportObject((ClientRemoteInterface)this.view,0));
                } catch (RuntimeException | RemoteException | NotBoundException e) {
                    throw new RuntimeException(e);
                }
            } else {
                try {
                    view = new TCPView("127.0.0.1", 4321);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                new Thread(() -> {
                    try {
                        ((TCPView) view).startClient();
                    } catch (IOException | ClassNotFoundException e) {
                        throw new RuntimeException(e);
                    }
                }).start();
            }
            viewController = new ViewController(view);
        }
    }

    public Boolean isInitialized() { return (view != null) && (viewController != null); }

    public View getView() {
        if (view != null) return view;
        else throw new NullPointerException();
    }

    public ViewController getViewController() {
        if (viewController != null) return viewController;
        else throw new NullPointerException();
    }
}
