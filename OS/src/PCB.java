public class PCB { // Process Control Block
    private static int nextPid = 1;

    private final UserlandProcess up;
    public int pid;
    private OS.PriorityType priority;

    public long wake_up_time;//variable to keep track of the time the process can wake back up from sleep

    public int demote_counter; //counter to keep track of how many times a process has run until the timer stop

    public boolean is_sleeping = false;

    PCB(UserlandProcess up, OS.PriorityType priority) {
        this.demote_counter = 0;
        this.up = up;
        this.pid = nextPid++;
        this.priority = priority;
    }
    public boolean is_sleeping() {
        return is_sleeping;
    }

    public String getName() {
        return up.getClass().getSimpleName();
    }

    OS.PriorityType getPriority() {
        return priority;
    }

    public void requestStop() {
        demote_counter++;
        if(demote_counter >= 5)
        {
            demote_counter = 0;
            switch(priority)
            {
                case OS.PriorityType.realtime -> {
                    System.out.println("Real time process " + up.getClass().getSimpleName() + " demoted to an interactive process");
                    setPriority(OS.PriorityType.interactive);
                }
                case OS.PriorityType.interactive -> {
                    System.out.println("interactive process " + up.getClass().getSimpleName() + " demoted to a background process");
                    setPriority(OS.PriorityType.background);
                }
            }
        }
        this.up.requestStop();
    }

    public void stop() throws InterruptedException { /* calls userlandprocess’ stop. Loops with Thread.sleep() until
ulp.isStopped() is true.  */
        this.up.stop();

        while(!this.up.isStopped())
        {
            Thread.sleep(50);
        }

    }

    public boolean isDone() { /* calls userlandprocess’ isDone() */
        return this.up.isDone();
    }

    void start() { /* calls userlandprocess’ start() */
        this.up.start();
    }

    public void setPriority(OS.PriorityType newPriority) {
        priority = newPriority;
    }
}
