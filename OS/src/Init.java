public class Init extends UserlandProcess {
    @Override
    public void main() {
        //OS.CreateProcess(new HelloWorld(), OS.PriorityType.realtime);
        //OS.CreateProcess(new GoodbyeWorld(), OS.PriorityType.background);
        //OS.CreateProcess(new interactive_process(), OS.PriorityType.interactive);
        for(int i = 0; i < 20; i++)
        {
            OS.CreateProcess(new TestVirtualMemory(), OS.PriorityType.realtime);
        }
        OS.Exit();
    }
}
