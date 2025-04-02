public class Init extends UserlandProcess {
    @Override
    public void main() {
        //OS.CreateProcess(new HelloWorld(), OS.PriorityType.realtime);
        //OS.CreateProcess(new GoodbyeWorld(), OS.PriorityType.background);
        //OS.CreateProcess(new interactive_process(), OS.PriorityType.interactive);
        OS.CreateProcess(new Ping(), OS.PriorityType.background);
        OS.CreateProcess(new Pong(), OS.PriorityType.background);
        OS.Exit();
    }
}
