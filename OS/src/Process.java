public abstract class Process implements Runnable{
    /*
    Private members:
    Thread
    Semaphore
    Boolean
     */

    public Process() {
    }

    public void requestStop() { //sets the boolean indicating that this process’ quantum has expired
    }

    public abstract void main(); //will represent the main of our “program”

    public boolean isStopped() {
        return false; // obviously wrong
    } //indicates if the semaphore is 0

    public boolean isDone() {
        return false; // obviously wrong
    } //true when the Java thread is not alive

    public void start() { //releases (increments) the semaphore, allowing this thread to run
    }

    public void stop() { //acquires (decrements) the semaphore, stopping this thread from running
    }

    public void run() { // This is called by the Thread - NEVER CALL THIS!!!
    } //acquire the semaphore, then call main

    public void cooperate() { //if the boolean is true, set the boolean to false and call OS.switchProcess()
    }
}
