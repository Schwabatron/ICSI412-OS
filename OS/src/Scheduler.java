public class Scheduler {
    //member:
    //Private linked list of type PCB
    //private instance of timer class
    //public reference to the pcb that is currently running
    //constructor that schedules using the timer the interrupt for every 250ms
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

}
