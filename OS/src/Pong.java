public class Pong extends UserlandProcess {

    @Override
    public void main() {
        byte[] data = { 'p', 'o', 'n', 'g' };

        while(true)
        {
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            KernelMessage pong = new KernelMessage(OS.GetPidByName("Pong"), OS.GetPidByName("Ping"), 1, data);
            System.out.println("sending pong to ping");
            OS.SendMessage(pong);
            System.out.println("waiting for message from ping");
            KernelMessage response = OS.WaitForMessage();
            System.out.println("Received from Ping: " + response.toString());
            cooperate();
        }
    }
}
