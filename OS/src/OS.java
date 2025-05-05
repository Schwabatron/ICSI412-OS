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
        int i = 0;

        if(ki != null) {
            ki.start();
        }

        if(current_process != null) {
            try {
                current_process.stop();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }

        while(OS.retVal == null && OS.currentCall == CallType.CreateProcess) {
            try {
                i++;
                if(i>100)
                {
                    System.out.println("stuck in sleep loop in OS ");
                }
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
        //open a new swap file
        int sysfile_id = Open("File pageFile.txt");
        Write(sysfile_id, "1024".getBytes());
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
        parameters.clear();
        parameters.add(s);
        currentCall = CallType.Open;
        startTheKernel();
        return (int) retVal;
    }

    public static void Close(int id) {
        parameters.clear();
        parameters.add(id);
        currentCall = CallType.Close;
        startTheKernel();
    }

    public static byte[] Read(int id, int size) {
        parameters.clear();
        parameters.add(id);
        parameters.add(size);
        currentCall = CallType.Read;
        startTheKernel();
        return (byte[]) retVal;
    }

    public static void Seek(int id, int to) {
        parameters.clear();
        parameters.add(id);
        parameters.add(to);
        currentCall = CallType.Seek;
        startTheKernel();
    }

    public static int Write(int id, byte[] data) {
        parameters.clear();
        parameters.add(id);
        parameters.add(data);
        currentCall = CallType.Write;
        startTheKernel();
        return (int) retVal;
    }

    // Messages
    public static void SendMessage(KernelMessage km) {
        parameters.clear();
        parameters.add(km);
        currentCall = CallType.SendMessage;
        startTheKernel();
    }

    public static KernelMessage WaitForMessage() {
        parameters.clear();
        currentCall = CallType.WaitForMessage;
        startTheKernel();
        return (KernelMessage) retVal;
    }

    public static int GetPidByName(String name) { //Get pid by name
        parameters.clear();
        parameters.add(name);
        currentCall = CallType.GetPIDByName;
        startTheKernel();
        return (int) retVal;
    }

    // Memory
    public static void GetMapping(int virtualPage) {
        parameters.clear();
        parameters.add(virtualPage);
        currentCall = CallType.GetMapping;
        startTheKernel();
    }

    public static int AllocateMemory(int size) {
        if(size % 1024 != 0) //if the size is not a multiple of 1024
        {
            System.out.println("Allocate Memory Error");
            return -1; //return error -1
        }
        parameters.clear();
        parameters.add(size);
        currentCall = CallType.AllocateMemory;
        startTheKernel();
        return (int) retVal;
    }

    public static boolean FreeMemory(int pointer, int size) {
        if(size % 1024 != 0 || pointer % 1024 != 0) //making sure both pointer and size are both multiples of 1024
        {
            System.out.println("Free Memory Error");
            return false; //return false for maybe a fail
        }
        parameters.clear();
        parameters.add(pointer);
        parameters.add(size);
        currentCall = CallType.FreeMemory;
        startTheKernel();
        return (boolean) retVal;
    }
}
