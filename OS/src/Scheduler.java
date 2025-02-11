import java.util.LinkedList;
import java.util.Timer;
import java.util.TimerTask;

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

    public int CreateProcess(UserlandProcess up, OS.PriorityType p) {
        current_process = new PCB(up, p);
        processes.add(current_process);
        //Not sure how to check if nothing else is running
        if(current_process.isDone())
        {
            switchProcess();
        }
        return current_process.pid;
    }

    public void switchProcess() {
        if(current_process != null) { // if something is currently running or the user process is done
            processes.addLast(processes.remove());
        }
        if(!processes.isEmpty())
        {
            current_process = processes.getLast();
            current_process.start();
        }
        else
        {
            System.out.println("No processes available");
        }
    }

}
