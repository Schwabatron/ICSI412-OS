public class interactive_process extends UserlandProcess {
    @Override
    public void main() {
        while(true)
        {
            try {
                System.out.println("interactive World!");
                cooperate();
                Thread.sleep(50);
            } catch (Exception e) { }
        }
    }
}
