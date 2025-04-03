public class Pong extends UserlandProcess {

    @Override
    public void main() {
        byte[] data = { 'p', 'o', 'n', 'g' };
        int i = 0;
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }


        while(true)
        {

            KernelMessage pong = new KernelMessage(OS.GetPidByName("Pong"), OS.GetPidByName("Ping"), ++i, data);
            System.out.println("Pong: sending pong to ping");
            OS.SendMessage(pong);
            System.out.println("Pong: waiting for message from ping");
            KernelMessage response = OS.WaitForMessage();
            System.out.println("Pong: Received from Ping: " + response.toString());
            cooperate();
        }
    }
}
