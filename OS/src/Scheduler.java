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
    //inside interrupt call "request stop" on the currently running PCB
    //If there is one (aka not null)
    //Needs 2 methods:
    /*
    public int CreateProcess(UserlandProcess up, OS.PriorityType p) //create a PCB fo the userland process
    //it to the list of pcbs and if nothing else is running call switchprocess to get it started



    public void SwitchProcess() //Take the currently running process and put it and the end of the list
    //It then takes the head of the list and runs it
    Corner Cases:
    - nothing is currently running(startup)
    - the user process is done() - we just dont add it to the list
     */

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
            processes.removeFirst();//remove the first element of the linked list (head)
        }
        if (!processes.isEmpty()) {
            current_process = processes.getFirst();
        }
    }

}
