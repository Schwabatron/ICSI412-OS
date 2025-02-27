import java.util.concurrent.Semaphore;

public class Kernel extends Process  {

    //member of type "scheduler"
    private Scheduler scheduler;

    public Kernel() {
        this.scheduler = new Scheduler();
    }

    public Scheduler getScheduler() {
        return scheduler;
    }

    @Override
    public void main() {
            while (true) { // Warning on infinite loop is OK...
                switch (OS.currentCall) { // get a job from OS, do it
                    case CreateProcess ->  // Note how we get parameters from OS and set the return value
                            OS.retVal = CreateProcess((UserlandProcess) OS.parameters.get(0), (OS.PriorityType) OS.parameters.get(1));
                    case SwitchProcess -> SwitchProcess();
                    case Sleep -> Sleep((int) OS.parameters.get(0));
                    case GetPID ->
                        OS.retVal = GetPid();
                    case Exit -> Exit();
                    case Open ->
                        OS.retVal = Open((String) OS.parameters.get(0));
                    case Close -> Close((int) OS.parameters.get(0));
                    case Read ->
                        OS.retVal = Read((int) OS.parameters.get(0), (int) OS.parameters.get(1));
                    case Seek -> Seek((int) OS.parameters.get(0), (int) OS.parameters.get(1));
                    case Write ->
                        OS.retVal = Write((int) OS.parameters.get(0), (byte[]) OS.parameters.get(1));
                    case GetPIDByName ->
                        OS.retVal = GetPidByName((String) OS.parameters.get(0));
                    case SendMessage -> SendMessage();
                    case WaitForMessage ->
                            OS.retVal = WaitForMessage();
                    case GetMapping -> GetMapping((int) OS.parameters.get(0));
                    case AllocateMemory ->
                            OS.retVal = AllocateMemory((int) OS.parameters.get(0));
                    case FreeMemory ->
                        OS.retVal = FreeMemory((int) OS.parameters.get(0), (int) OS.parameters.get(1));
                }
                if (scheduler.current_process != null) {
                    scheduler.current_process.start();
                }
                //scheduler.current_process.start();
                this.stop();

                // TODO: Now that we have done the work asked of us, start some process then go to sleep.
            }
    }

    private void SwitchProcess() {
        scheduler.switchProcess();
    }

    // For assignment 1, you can ignore the priority. We will use that in assignment 2
    private int CreateProcess(UserlandProcess up, OS.PriorityType priority) {
         return scheduler.CreateProcess(up, priority);
    }

    private void Sleep(int mills) { //Call sleep in the scheduler(doesnt exist yet?)
        scheduler.Sleep(mills);
    }

    private void Exit() {
        scheduler.Exit();
    }

    private int GetPid() { //return the pid of the currently running process
       return scheduler.current_process.pid; //return current pid
    }

    private int Open(String s) {
        return 0; // change this
    }

    private void Close(int id) {
    }

    private byte[] Read(int id, int size) {
        return null; // change this
    }

    private void Seek(int id, int to) {
    }

    private int Write(int id, byte[] data) {
        return 0; // change this
    }

    private void SendMessage(/*KernelMessage km*/) {
    }

    private KernelMessage WaitForMessage() {
        return null;
    }

    private int GetPidByName(String name) {
        return 0; // change this
    }

    private void GetMapping(int virtualPage) {
    }

    private int AllocateMemory(int size) {
        return 0; // change this
    }

    private boolean FreeMemory(int pointer, int size) {
        return true;
    }

    private void FreeAllMemory(PCB currentlyRunning) {
    }

}