public interface Device { //device interface
    /*
    every device we make must implement this interface
     */


    /*
    String parameter allow us to pass in basically anything since devices are so generic
    the int returned is the device ID
    the caller can use the device ID to access the particular device and config
     */
    int Open(String s);
    void Close(int id);
    byte[] Read(int id, int size);
    void Seek(int id, int to);
    int Write(int id, byte[] data);

}
