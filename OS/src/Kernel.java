public class Kernel extends Process  {

    //member of type "scheduler"
    private Scheduler scheduler;

    public Kernel() {
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
                    /*
                    // Priority Schduler
                    case Sleep -> Sleep((int) OS.parameters.get(0));
                    case GetPID -> OS.retVal = GetPid();
                    case Exit -> Exit();
                    // Devices
                    case Open ->
                    case Close ->
                    case Read ->
                    case Seek ->
                    case Write ->
                    // Messages
                    case GetPIDByName ->
                    case SendMessage ->
                    case WaitForMessage ->
                    // Memory
                    case GetMapping ->
                    case AllocateMemory ->
                    case FreeMemory ->
                     */
                }
                //call start on the next process to run
                start();
                try //not sure what all this is just was recommended by the IDE
                {
                    stop();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }

                // TODO: Now that we have done the work asked of us, start some process then go to sleep.
            }
    }

    private void SwitchProcess() {}

    // For assignment 1, you can ignore the priority. We will use that in assignment 2
    private int CreateProcess(UserlandProcess up, OS.PriorityType priority) {
        return 0; // change this
    }

    private void Sleep(int mills) {
    }

    private void Exit() {
    }

    private int GetPid() {
        return 0; // change this
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