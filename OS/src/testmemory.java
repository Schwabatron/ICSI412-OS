public class testmemory extends UserlandProcess{

    @Override
    public void main() {
        try { //used to fix race condition where this process is moving too fast and returning the byte array before createprocess finishes
            Thread.sleep(100);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        int pointer = OS.AllocateMemory(2048);

        if(pointer == -1) {
            System.out.println("Memory allocation failed");
        }
        else
        {
            System.out.println("Memory allocated: " + pointer);
        }

        for(int i = 0; i < 2048; i++) {
            Hardware.Write(i + pointer, (byte)100);
        }


        boolean freetest = OS.FreeMemory(pointer, 2048);

        if(freetest) {
            System.out.println("Memory free successfully");
        }
        else
        {
            System.out.println("Memory not free successfully");
        }

        for(int i = 0; i < 10; i++) {
            byte temp = Hardware.Read(i + pointer);
            System.out.println(temp);
        }

    }
}
