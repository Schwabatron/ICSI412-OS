import java.util.ArrayList;
import java.util.List;

public class OS {
    private static Kernel ki; // The one and only one instance of the kernel.

    public static List<Object> parameters = new ArrayList<>();
    public static Object retVal;

    public enum CallType {SwitchProcess,SendMessage, Open, Close, Read, Seek, Write, GetMapping, CreateProcess, Sleep, GetPID, AllocateMemory, FreeMemory, GetPIDByName, WaitForMessage, Exit}
    public static CallType currentCall;

    private static void startTheKernel() {
        PCB current_process = ki.getScheduler().current_process;
        ki.start();


        if(current_process != null) {
            try {
                current_process.stop();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
        while(OS.retVal == null) {
            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public static void switchProcess() {
        parameters.clear();
        currentCall = CallType.SwitchProcess;
        startTheKernel();
    }

    public static void Startup(UserlandProcess init) {
            ki = new Kernel();
            CreateProcess(init, PriorityType.interactive);
            CreateProcess(new IdleProcess(), PriorityType.background);
    }

    public enum PriorityType {realtime, interactive, background}
    public static int CreateProcess(UserlandProcess up) {//used when no priority is used
        return  CreateProcess(up,PriorityType.interactive);
    }

    // For assignment 1, you can ignore the priority. We will use that in assignment 2
    public static int CreateProcess(UserlandProcess up, PriorityType priority) {
        parameters.clear();
        parameters.add(up);
        parameters.add(priority);
        currentCall = CallType.CreateProcess;
        startTheKernel();
        return (int) retVal;
    }

    public static int GetPID() {
        parameters.clear();
        currentCall = CallType.GetPID;
        startTheKernel();
        return (int) retVal;
    }

    public static void Exit() {
        parameters.clear();
        currentCall = CallType.Exit;
        startTheKernel();
    }

    public static void Sleep(int mills) {
        parameters.clear();
        parameters.add(mills);
        currentCall = CallType.Sleep;
        startTheKernel();
    }

    // Devices
    public static int Open(String s) {
        return 0;
    }

    public static void Close(int id) {
    }

    public static byte[] Read(int id, int size) {
        return null;
    }

    public static void Seek(int id, int to) {
    }

    public static int Write(int id, byte[] data) {
        return 0;
    }

    // Messages
    public static void SendMessage(KernelMessage km) {
    }

    public static KernelMessage WaitForMessage() {
        return null;
    }

    public static int GetPidByName(String name) {
        return 0; // Change this
    }

    // Memory
    public static void GetMapping(int virtualPage) {
    }

    public static int AllocateMemory(int size ) {
        return 0; // Change this
    }

    public static boolean FreeMemory(int pointer, int size) {
        return false; // Change this
    }
}
