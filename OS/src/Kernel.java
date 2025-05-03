import java.util.Arrays;
import java.util.concurrent.Delayed;
import java.util.concurrent.Semaphore;

public class Kernel extends Process implements Device {

    //member of type "scheduler"
    private Scheduler scheduler;

    private VFS vfs;

    public boolean[] page_used = new boolean[1024];

    public Kernel() {
        this.scheduler = new Scheduler();
        this.vfs = new VFS();
        Arrays.fill(page_used, false); //initializing the page_used array
    }

    public Scheduler getScheduler() {
        return scheduler;
    }

    @Override
    public void main() {
            while (true) { // Warning on infinite loop is OK...
                switch (OS.currentCall) { // get a job from OS, do it
                    case CreateProcess ->
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
                    case SendMessage -> SendMessage((KernelMessage) OS.parameters.get(0));
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
                this.stop();

                // TODO: Now that we have done the work asked of us, start some process then go to sleep.
            }
    }

    private void SwitchProcess() {
        if(scheduler.current_process != null) {
            scheduler.current_process.demote_counter++;
            if(scheduler.current_process.demote_counter >= 5)
            {
                switch(scheduler.current_process.getPriority())
                {
                    case OS.PriorityType.realtime ->
                            {
                                System.out.println("Real-Time process " + scheduler.current_process.getName() + " is now a interactive process");
                                scheduler.current_process.setPriority(OS.PriorityType.interactive);
                            }
                    case OS.PriorityType.interactive ->
                            {
                                System.out.println("Interactive process " + scheduler.current_process.getName() + " is now a background process");
                                scheduler.current_process.setPriority(OS.PriorityType.background);
                            }
                }
            }
        }
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


    public int Open(String s) {

        PCB cur_process = scheduler.get_current_process();

        for(int i = 0; i < cur_process.VFS_ids.length; i++) {
            if(cur_process.VFS_ids[i] == -1)
            {
               int return_value = vfs.Open(s);
               if(return_value == -1)
               {
                   return -1;//fail
               }
               else
               {
                   cur_process.VFS_ids[i] = return_value; //putting the vfs id in the kernelland array
                   return i;
               }
            }

        }
        return -1; //fail: no space left
    }

    public void Close(int id) {
        int vfs_id = scheduler.get_current_process().VFS_ids[id]; //retrieving the vfs id
        vfs.Close(vfs_id); //using the vfs id to close the correct device
        scheduler.get_current_process().VFS_ids[id] = -1; //setting back to -1
    }

    public byte[] Read(int id, int size) {
        int vfs_id = scheduler.get_current_process().VFS_ids[id]; //retrieving the vfs id
        return vfs.Read(vfs_id, size);
    }

    public void Seek(int id, int to) {
        int vfs_id = scheduler.get_current_process().VFS_ids[id]; //retrieving the vfs id
        vfs.Seek(vfs_id, to);
    }

    public int Write(int id, byte[] data) {
        int vfs_id = scheduler.get_current_process().VFS_ids[id];
        return vfs.Write(vfs_id, data); // change this
    }

    private void SendMessage(KernelMessage km) {
        KernelMessage message = new KernelMessage(km); //copy of the kernel message
        message.sender_PID = scheduler.current_process.pid;
        scheduler.SendMessage(message); //using the copy this way the process and the kernel dont access the same object

    }

    private KernelMessage WaitForMessage() {
        KernelMessage message = scheduler.WaitForMessage();
        return message;
    }

    private int GetPidByName(String name) {
        int pid = scheduler.GetPidByName(name);
        return pid;
    }

    private void GetMapping(int virtualPage) {
        /*
        int page_num = scheduler.get_current_process().page_table[virtualPage];

        if(page_num == -1)
        {
            System.out.println("Page " + virtualPage + " not found: Seg fault"); //throw error message
            Exit(); //kill the current process
        }

        int slot = (int)(Math.random() * 2); //choosing a random slot (1 or 0)
        Hardware.TLB[slot][0] = virtualPage;
        Hardware.TLB[slot][1] = page_num;

         */
    }

    private int AllocateMemory(int size) {


        int num_pages = size / 1024; // amount of space needed in pages
        int starting_index = -1;

        for (int i = 0; i <= scheduler.current_process.page_table.length - num_pages; i++) {
            boolean found = true;

            for (int j = 0; j < num_pages; j++) {
                if (scheduler.current_process.page_table[i + j] != null) { //finding contiguous memory in the page table
                    found = false;
                    break;
                }
            }

            if (found) {
                starting_index = i;
                break;
            }
        }
        int temp_starting_index = starting_index;
        int pages_cleared = 0;

        for(int i = 0; i < page_used.length; i++) {
            if(!page_used[i])
            {
                page_used[i] = true;
                scheduler.current_process.page_table[temp_starting_index] = new VirtualToPhysicalMapping();
                temp_starting_index++;
                pages_cleared++;
                if(pages_cleared == num_pages)
                {
                    break;
                }
            }
        }

        if(pages_cleared != num_pages)
        {
            System.out.println("Could not find sufficient space in memory: Kernel");

            for (int i = starting_index; i < temp_starting_index; i++) { //opon a fail i need to reset all the memory that was going to be allocated
                int page_index = scheduler.current_process.page_table[i].physical_page_number;
                page_used[page_index] = false;
                scheduler.current_process.page_table[i] = null;
            }

            return -1;
        }

        return starting_index;
    }

    private boolean FreeMemory(int pointer, int size) {

        Hardware.ClearTLB();
        int num_pages = size / 1024; //getting the number of pages that need to be freed

        if(pointer < 0 || pointer + num_pages > scheduler.current_process.page_table.length)
        {
            return false; //if the pointer is negative, is asking you to free more memory than you have or is pointing ahead of the space you have then this did not work
        }

        for(int i = pointer; i < pointer + num_pages; i++) {
            int page_index = scheduler.current_process.page_table[i].physical_page_number;

            if(page_index == -1)
            {
                return false; //memory is already free
            }

            page_used[page_index] = false;
            scheduler.current_process.page_table[i] = null;
        }

        return true;
    }

    private void FreeAllMemory(PCB currentlyRunning) {
    }



}