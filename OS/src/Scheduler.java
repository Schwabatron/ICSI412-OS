import java.time.Clock;
import java.util.*;


//no process running means current process = null
public class Scheduler {

    private Kernel ki;



    private Random rand = new Random(); //random, going to be used to decide what to schedule next

    private Clock clock = Clock.systemUTC(); // instance of a clock to allow the scheduler to keep track of the time


    private LinkedList<PCB> realtime_processes = new LinkedList<>(); // queue to hold all the processes

    private LinkedList<PCB> interactive_processes = new LinkedList<>(); // queue to hold all the processes

    private LinkedList<PCB> background_processes = new LinkedList<>(); // queue to hold all the processes

    private LinkedList<PCB> sleeping_processes = new LinkedList<>(); //queue to hold all the sleeping processes

    private LinkedList<PCB> waiting_processes = new LinkedList<>(); //keeping track of processes waiting for messages





    private Timer timer = new Timer();

    public PCB current_process;

    public PCB get_current_process() { //accessor
        return current_process;
    }


    public Scheduler() {
        timer.scheduleAtFixedRate(new TimerTask() {
            public void run() {
                if(current_process != null) {
                    current_process.requestStop();
                }
            }
        }, 0 , 250);
    }

    public void Sleep(int mills) { //Call sleep in the scheduler(doesnt exist yet?)
        current_process.demote_counter = 0;
        current_process.wake_up_time = clock.millis() + mills; //adding the millis to the clock
        sleeping_processes.add(current_process); //adding to sleeping queue
        current_process = null; //making current null so it doesnt add it back to the queue without waking up
        switchProcess(); //switching the process
    }

    public void Exit() {
        for(int i = 0; i < current_process.VFS_ids.length; i++) {
            if(current_process.VFS_ids[i] != -1) {
                ki.Close(i); //closing all of the current processes running devices
            }
        }
        current_process = null; //making the process null so it doesnt get rescheduled
        switchProcess(); //switching process

    }

    public int CreateProcess(UserlandProcess up, OS.PriorityType p) {
        PCB new_process = new PCB(up, p);
        switch(p) //adding to the correct priority queue
        {
            case OS.PriorityType.background -> background_processes.add(new_process);
            case OS.PriorityType.realtime -> realtime_processes.add(new_process);
            case OS.PriorityType.interactive -> interactive_processes.add(new_process);
        }
        //Not sure how to check if nothing else is running
        if(current_process == null)
        {
            switchProcess();
        }

        return new_process.pid;
    }




    public void switchProcess() {
        if (current_process != null && !current_process.isDone()) { //if current process is not null and not done
            switch(current_process.getPriority()) {
                case OS.PriorityType.background -> background_processes.add(current_process);
                case OS.PriorityType.realtime -> realtime_processes.add(current_process);
                case OS.PriorityType.interactive -> interactive_processes.add(current_process);
            }
        }
        else if(current_process != null && current_process.isDone()) {
            Exit();
        }
        wake_up_sleepers();//attempt to wake up sleeping processes
        current_process = choose_next_process();//finds the next process to run and removes it from the front of the queue
        if(current_process != null && current_process.woken_up) {
            OS.retVal = current_process.message_Queue.pollFirst();
            current_process.woken_up = false;
        }
    }


    private void wake_up_sleepers() //method used to wake up the sleeping processes
    {
        long current_time = clock.millis();

        ArrayList<PCB> processes_to_be_awoken = new ArrayList<>();

        for (PCB process : sleeping_processes) { //iterate through sleepers
            if (process.wake_up_time <= current_time) { //if the wake up time is less than the current time then wake them up
                switch (process.getPriority()) {
                    case OS.PriorityType.background ->{
                        background_processes.add(process);
                    }
                    case OS.PriorityType.realtime -> {
                        realtime_processes.add(process);
                    }
                    case OS.PriorityType.interactive -> {
                        interactive_processes.add(process);
                    }
                }
                processes_to_be_awoken.add(process);
            }
        }

        sleeping_processes.removeAll(processes_to_be_awoken);
    }

    private PCB choose_next_process() {
        int random = rand.nextInt(10) + 1; //picks a number 1-10
        //System.out.println(random);

        if (!realtime_processes.isEmpty()) {
            //System.out.println(random);
           // System.out.println(random + " realtime_processes is not empty");

            if (random <= 6) { //if the number is 1-6 then we will run a realtime
                return realtime_processes.pollFirst();
            } else if (random <= 9) { //if it is 7-9 then we will run an interactive if they have one and background if we dont have one
                return !interactive_processes.isEmpty() ? interactive_processes.pollFirst() : background_processes.pollFirst();
            } else { // if it is 10 then we will run the background
                return background_processes.pollFirst(); //if its 10 then run a background process
            }

        } else if (!interactive_processes.isEmpty()) { // if realtime is empty and interactive is not

            random = rand.nextInt(4) + 1; //make new range 1-4
           // System.out.println(random + " real time empty");
            if (random <= 3) { //3/4 chance to run interactive
                return interactive_processes.pollFirst();
            } else {
                return background_processes.pollFirst(); //1/4 to run background
            }

        } else {
            var process = background_processes.pollFirst();
            System.out.println("next process chosen -> " + process.getName());
            return process; // Only background processes exist
        }
    }

    public int GetPidByName(String name) {
        List<LinkedList<PCB>> allQueues = Arrays.asList(
                realtime_processes,
                interactive_processes,
                background_processes,
                sleeping_processes,
                waiting_processes
        );

        for(var queue : allQueues) {
            for(PCB process : queue) {
                if(process.getName().equals(name)) {

                    return process.pid; //return the process with the matching name
                }
            }
        }

        return -1; //fail
    }

    public void SendMessage(KernelMessage km) {
        List<LinkedList<PCB>> allQueues = Arrays.asList(
                realtime_processes,
                interactive_processes,
                background_processes,
                sleeping_processes,
                waiting_processes
        );

        for(var queue : allQueues) {
            for(PCB process : queue) {
                if(km.receiver_PID == process.pid) { //if we find the process that we would like to send the message to
                    //System.out.println("kernel message added to queue for process: " + process.getName());
                    process.message_Queue.add(km); //add the message to the message queue
                    if(waiting_processes.contains(process)) { //checking if this process was waiting on a message
                        waiting_processes.remove(process); //remove from the waiting process queue
                        process.woken_up = true;
                        //System.out.println("process removed from the waiting queue: " + process.getName());
                        switch(process.getPriority()) //add it back to its correct priority queue
                        {
                            case OS.PriorityType.background -> background_processes.add(process);
                            case OS.PriorityType.realtime -> realtime_processes.add(process);
                            case OS.PriorityType.interactive -> interactive_processes.add(process);
                        }
                    }
                }
            }
        }


    }

    public KernelMessage WaitForMessage() {
        if(!current_process.message_Queue.isEmpty()) {
            return current_process.message_Queue.pollFirst();
        }
        else {
            waiting_processes.add(current_process);
            current_process = null;
            switchProcess();
        }

        return null; //not sure what to return
    }
}
