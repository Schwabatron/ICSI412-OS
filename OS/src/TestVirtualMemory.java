public class TestVirtualMemory extends UserlandProcess {



    @Override
    public void main() {

        try { //used to fix race condition where this process is moving too fast and returning the byte array before createprocess finishes
            Thread.sleep(100);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        int pointer = OS.AllocateMemory(1024*100);

        if(pointer == -1) {
            System.out.println("Memory allocation failed");
        }
        else
        {
                System.out.println("Memory allocated: " + pointer);
        }
    }
}
