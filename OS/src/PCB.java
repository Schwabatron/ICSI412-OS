public class PCB { // Process Control Block
    private static int nextPid = 1;

    private final UserlandProcess up;
    public int pid;
    private OS.PriorityType priority;

    PCB(UserlandProcess up, OS.PriorityType priority) {
        this.up = up;
        this.pid = nextPid++;
        this.priority = priority;
    }

    public String getName() {
        return null;
    }

    OS.PriorityType getPriority() {
        return priority;
    }

    public void requestStop() {
        up.requestStop();
    }

    public void stop() throws InterruptedException { /* calls userlandprocess’ stop. Loops with Thread.sleep() until
ulp.isStopped() is true.  */
        this.up.stop();
    }

    public boolean isDone() { /* calls userlandprocess’ isDone() */
        return up.isDone();
    }

    void start() { /* calls userlandprocess’ start() */
        this.up.start();
    }

    public void setPriority(OS.PriorityType newPriority) {
        priority = newPriority;
    }
}
