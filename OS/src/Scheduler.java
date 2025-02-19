import java.util.LinkedList;
import java.util.Timer;
import java.util.TimerTask;


//no process running means current process = null
public class Scheduler {
    //member:
    //Private linked list of type PCB
    private LinkedList<PCB> processes = new LinkedList<>();
    //private instance of timer class
    private Timer timer = new Timer();
    //public reference to the pcb that is currently running
    public PCB current_process;
    //constructor that schedules using the timer the interrupt for every 250ms
    public Scheduler() {
        timer.scheduleAtFixedRate(new TimerTask() {
            public void run() {
                if(current_process != null) {
                    current_process.requestStop();
                }
            }
        }, 0 , 250);
    }

    public int CreateProcess(UserlandProcess up, OS.PriorityType p) { //prob here
        PCB new_process = new PCB(up, p);
        processes.add(new_process);
        //Not sure how to check if nothing else is running
        if(current_process == null)
        {
            switchProcess();
        }
        return new_process.pid;
    }

    /*
    corner cases:
    - nothing is currently running (startup) dont put null on the list
    - the user process is done (dont add it to the list)
     */
    public void switchProcess() { //prob here
        if (current_process != null) { //if current process is not null
            if (!current_process.isDone()) { // if the current process is NOT done
                processes.addLast(current_process); //take the current process and move it to the back of the linked list
            }
        }
        current_process = processes.pollFirst(); //set the current process to the new first element of the list

    }

}
