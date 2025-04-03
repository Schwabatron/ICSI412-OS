public class Ping extends UserlandProcess{


    @Override
    public void main() {
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        byte[] data = { 'p', 'i', 'n', 'g' };
        int i = 0;

        while(true)
        {
            KernelMessage ping = new KernelMessage(OS.GetPidByName("Ping"), OS.GetPidByName("Pong"), ++i, data);
            System.out.println("Ping: sending ping to pong");
            OS.SendMessage(ping);
            System.out.println("Ping: waiting for message from pong");
            KernelMessage response = OS.WaitForMessage();
            System.out.println("Ping: Received from Pong: " + response.toString());
            cooperate();
        }
    }
}
