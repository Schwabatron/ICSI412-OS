
public class TestDevices extends UserlandProcess {
    @Override
    public void main() {
        try { //used to fix race condition where this process is moving too fast and returning the byte array before createprocess finishes
            Thread.sleep(100);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        int random_dev = OS.Open("random 100");
        int file_dev = OS.Open("file OS/src/test.txt");

        if(random_dev == -1) {
            System.out.println("Random device not found/failed to open");
        }else if(file_dev == -1) {
            System.out.println("File device not found/failed to open");
        }else{


            byte[] test_data = "hello world".getBytes();

            OS.Write(random_dev, test_data);
            OS.Write(file_dev, test_data);

            byte[] read_data_random = OS.Read(random_dev, test_data.length);
            OS.Seek(file_dev, 0);
            byte[] read_data_file = OS.Read(file_dev, test_data.length);

            System.out.println("read data from random dev " + new String(read_data_random));
            System.out.println("read data from file dev " + new String(read_data_file));
            OS.Close(random_dev);
            OS.Close(file_dev);
        }
        OS.Exit();

    }
}
