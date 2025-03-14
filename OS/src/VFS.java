public class VFS implements Device{

    public class VFSMapping { //class for the mapping between id and device
        public Device device; //the device
        public int deviceId; //the device ID

        public VFSMapping(Device device, int deviceId) {
            this.device = device;
            this.deviceId = deviceId;
        }
    }

    VFSMapping[] mappings = new VFSMapping[20]; //20 long (consistent with other devices) array to hold the mappings with the index of this array being the
    //VFS id

    @Override
    public int Open(String s) {
        int device_id;
        Device device;
        String[] device_and_arg = s.split(" "); //splitting the string into the device and the argument
        if(device_and_arg[0].equals("random")) {
            device = new RandomDevice();
            if(device_and_arg.length == 2) {
                device_id = device.Open(device_and_arg[1]);
            }
            else
            {
                device_id = device.Open("");
            }

        }
        else if(device_and_arg[0].equals("file")) {
            device = new FakeFileSystem();
            device_id = device.Open(device_and_arg[1]);
        }
        else
        {
            throw new IllegalArgumentException("Unknown device type: " + device_and_arg[0]);
        }

        VFSMapping mapping = new VFSMapping(device, device_id);

        for(int i = 0; i < mappings.length; i++)
        {
            if(mappings[i] == null)
            {
                mappings[i] = mapping;
                return i;
            }
        }
        return -1;

    }

    @Override
    public void Close(int id) {
        mappings[id].device.Close(mappings[id].deviceId);
        mappings[id] = null;

    }

    @Override
    public byte[] Read(int id, int size) {
        return mappings[id].device.Read(mappings[id].deviceId, size);
    }

    @Override
    public void Seek(int id, int to) {
        mappings[id].device.Seek(mappings[id].deviceId, to);
    }

    @Override
    public int Write(int id, byte[] data) {
        return mappings[id].device.Write(mappings[id].deviceId, data);
    }
}
