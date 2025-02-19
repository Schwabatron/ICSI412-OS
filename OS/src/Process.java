import java.util.concurrent.Semaphore;

public abstract class Process implements Runnable{
    /*
    Private members:
    Thread
    Semaphore
    Boolean
     */

    private boolean quantum_expired; //Indicates if the quantum is expired: for now true for expired false for not expired
    private Semaphore semaphore; //Semaphore with 1 permit and no fairness setting for now
    private Thread thread; //since the current instance implements the runnable class i can use this i think

    public Process() {
        this.semaphore = new Semaphore(0);
        this.thread = new Thread(this);
        this.quantum_expired = false;
        this.thread.start();
    }

    public void requestStop() {//sets the boolean indicating that this process’ quantum has expired
        quantum_expired = true;
    }

    public abstract void main(); //will represent the main of our “program”

    public boolean isStopped() { //indicates if the semaphore is 0
        return semaphore.availablePermits() == 0;

    }

    public boolean isDone() {
        return !thread.isAlive();
    } //true when the Java thread is not alive

    public void start() { //releases (increments) the semaphore, allowing this thread to run
        semaphore.release();
    }

    public void stop()  { //acquires (decrements) the semaphore, stopping this thread from running
        try {
            semaphore.acquire();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public void run() { // This is called by the Thread - NEVER CALL THIS!!!
        try {
            semaphore.acquire();
            main();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    } //acquire the semaphore, then call main

    public void cooperate() { //if the boolean is true, set the boolean to false and call OS.switchProcess()
        if (quantum_expired) {
            quantum_expired = false;
            OS.switchProcess();
        }
    }
}
