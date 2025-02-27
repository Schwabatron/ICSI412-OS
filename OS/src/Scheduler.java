import java.time.Clock;
import java.util.*;


//no process running means current process = null
public class Scheduler {

    private Random rand = new Random(); //random, going to be used to decide what to schedule next

    private Clock clock = Clock.systemUTC(); // instance of a clock to allow the scheduler to keep track of the time


    private LinkedList<PCB> realtime_processes = new LinkedList<>(); // queue to hold all the processes
    private LinkedList<PCB> interactive_processes = new LinkedList<>(); // queue to hold all the processes
    private LinkedList<PCB> background_processes = new LinkedList<>(); // queue to hold all the processes

    private LinkedList<PCB> sleeping_processes = new LinkedList<>(); //queue to hold all the sleeping processes

    private Timer timer = new Timer();

    public PCB current_process;
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
        current_process.wake_up_time = clock.millis() + mills; //adding the millis to the clock
        sleeping_processes.add(current_process); //adding to sleeping queue
        current_process = null; //making current null so it doesnt add it back to the queue without waking up
        switchProcess(); //switching the process
    }

    public void Exit() {
        current_process = null;
        switchProcess();

    }

    public int CreateProcess(UserlandProcess up, OS.PriorityType p) {
        PCB new_process = new PCB(up, p);
        switch(p)
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




    public void switchProcess() { //prob here
        if (current_process != null && !current_process.isDone()) { //if current process is not null and not done

            switch(current_process.getPriority()) {
                case OS.PriorityType.background -> background_processes.add(current_process);
                case OS.PriorityType.realtime -> realtime_processes.add(current_process);
                case OS.PriorityType.interactive -> interactive_processes.add(current_process);
            }

            wake_up_sleepers();//attempt to wake up sleeping processes
        }
        current_process = choose_next_process();//set the current process to the new first element of the list
    }


    private void wake_up_sleepers() //method used to wake up the sleeping processes
    {

        ArrayList<PCB> processes_to_be_awoken = new ArrayList<>();

        for (PCB process : sleeping_processes) {
            if (process.wake_up_time <= clock.millis()) {
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

        if (!realtime_processes.isEmpty()) {
            System.out.println(random + " realtime_processes is not empty");

            if (random <= 6) { //if the number is 1-6 then we will run a realtime
                return realtime_processes.pollFirst();
            } else if (random <= 9) { //if it is 7-9 then we will run an interactive if they have one and background if we dont have one
                return !interactive_processes.isEmpty() ? interactive_processes.pollFirst() : background_processes.pollFirst();
            } else { // if it is 10 then we will run the background
                return background_processes.pollFirst(); //if its 10 then run a background process
            }

        } else if (!interactive_processes.isEmpty()) { // if realtime is empty and interactive is not

            random = rand.nextInt(4) + 1; //make new range 1-4
            System.out.println(random + " real time empty");
            if (random <= 3) { //3/4 chance to run interactive
                return interactive_processes.pollFirst();
            } else {
                return background_processes.pollFirst(); //1/4 to run background
            }

        } else {
            System.out.println("other queues empty");
            return background_processes.pollFirst(); // Only background processes exist
        }
    }
}
