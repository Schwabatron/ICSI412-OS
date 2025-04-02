public class Ping extends UserlandProcess{


    @Override
    public void main() {
        byte[] data = { 'p', 'i', 'n', 'g' };

        while(true)
        {
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            KernelMessage ping = new KernelMessage(OS.GetPidByName("Ping"), OS.GetPidByName("Pong"), 2, data);
            System.out.println("sending ping to pong");
            OS.SendMessage(ping);
            System.out.println("waiting for message from pong");
            KernelMessage response = OS.WaitForMessage();
            System.out.println("Received from Pong: " + response.toString());
            cooperate();
        }
    }
}
