package it.polimi.ingsw.view.CLI;

public class Lock {
    private final Object lock = new Object();
    private boolean stop = false;

    public Lock() {}

    protected void waitForUpdate() throws InterruptedException {
        stop = true;
        synchronized (lock) {
            while (stop) {
                lock.wait();
            }
        }
    }

    protected void notifyUpdateReceived() {
        synchronized (lock) {
            stop = false;
            lock.notifyAll();
        }
    }
}
