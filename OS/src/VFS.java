public class VFS implements Device{

    public class VFSMapping { //class for the mapping between id and device
        public Device device; //the device
        public int deviceId; //the device ID

        public VFSMapping(Device device, int deviceId) {
            this.device = device;
            this.deviceId = deviceId;
        }
    }

    VFSMapping[] mappings = new VFSMapping[10]; //10 long (consistent with other devices) array to hold the mappings with the index of this array being the
    //VFS id

    @Override
    public int Open(String s) {
        return 0;
    }

    @Override
    public void Close(int id) {

    }

    @Override
    public byte[] Read(int id, int size) {
        return new byte[0];
    }

    @Override
    public void Seek(int id, int to) {

    }

    @Override
    public int Write(int id, byte[] data) {
        return 0;
    }
}
