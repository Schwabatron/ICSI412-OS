public class Init extends UserlandProcess {



    @Override
    public void main() {

        while(true)
        {
            try {
                OS.CreateProcess(new HelloWorld());
                cooperate();
                Thread.sleep(50);
                OS.CreateProcess(new GoodbyeWorld());
                cooperate();
                Thread.sleep(50);
            } catch (Exception e) { }
        }
    }
}
