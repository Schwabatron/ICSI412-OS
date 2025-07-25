import java.util.Arrays;
import java.util.LinkedList;

public class PCB { // Process Control Block
    private static int nextPid = 1;

    private final UserlandProcess up;
    public int pid;
    private OS.PriorityType priority;

    public long wake_up_time;//variable to keep track of the time the process can wake back up from sleep

    public int demote_counter; //counter to keep track of how many times a process has run until the timer stop

    public int[] VFS_ids = new int[10];

    public LinkedList<KernelMessage> message_Queue = new LinkedList<>();

     private String name;

     public boolean woken_up = false; //flag to keep track if this process has been woken up (message queue bad name)

    public int[] page_table = new int[100];

    PCB(UserlandProcess up, OS.PriorityType priority) {
        this.demote_counter = 0;
        this.up = up;
        this.pid = nextPid++;
        this.priority = priority;
        this.name = up.getClass().getSimpleName();

        for(int i = 0; i < 10; i++) {
            VFS_ids[i] = -1;
        }
        for(int i = 0; i < page_table.length; i++) { //initializing the page table to all -1
            page_table[i] = -1;
        }
    }
    public String getName() {
        return name;
    }

    OS.PriorityType getPriority() {
        return priority;
    }

    public void requestStop() {
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
