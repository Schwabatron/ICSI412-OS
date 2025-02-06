public abstract class Process implements Runnable{
    public Process() {
    }

    public void requestStop() {
    }

    public abstract void main();

    public boolean isStopped() {
        return false; // obviously wrong
    }

    public boolean isDone() {
        return false; // obviously wrong
    }

    public void start() {
    }

    public void stop() {
    }

    public void run() { // This is called by the Thread - NEVER CALL THIS!!!
    }

    public void cooperate() {
    }
}
