public class Init extends UserlandProcess {
    @Override
    public void main() {
        OS.CreateProcess(new HelloWorld());
        OS.CreateProcess(new GoodbyeWorld());
        while(true)
        {
            try {
                cooperate();
                Thread.sleep(50);
            } catch (Exception e) { }
        }
    }
}
