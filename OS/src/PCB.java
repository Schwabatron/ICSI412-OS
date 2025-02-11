public class PCB { // Process Control Block
    private static int nextPid = 1;
    public int pid;
    private OS.PriorityType priority;

    PCB(UserlandProcess up, OS.PriorityType priority) {
        pid = nextPid++;
    }

    public String getName() {
        return null;
    }

    OS.PriorityType getPriority() {
        return priority;
    }

    public void requestStop() {
    }

    public void stop() { /* calls userlandprocess’ stop. Loops with Thread.sleep() until
ulp.isStopped() is true.  */


    }

    public boolean isDone() { /* calls userlandprocess’ isDone() */
        return false; // Change
    }

    void start() { /* calls userlandprocess’ start() */
    }

    public void setPriority(OS.PriorityType newPriority) {
        priority = newPriority;
    }
}
