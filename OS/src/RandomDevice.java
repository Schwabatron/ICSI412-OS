import java.util.Random;

public class RandomDevice implements Device {

    //random array
    public Random[] random_devices = new Random[10];


    @Override
    public int Open(String s) {
        int seed = 0;
        boolean seed_available = false;


        if(s != null && !s.isEmpty()) { //if the string is not null or empty
            seed = Integer.parseInt(s); //using the int as the seed for random
            seed_available = true;
        }

        //find a place in the array for the random device to go into
        for(int i = 0; i < random_devices.length; i++)
        {
            if(random_devices[i] == null)
            {
                random_devices[i] = seed_available ? new Random(seed) : new Random();
                return i;
            }
        }

        //TODO: find out what to return in the instance there are no more slots available
        return -1;
    }

    @Override
    public void Close(int id) {
        random_devices[id] = null;
    }

    @Override
    public byte[] Read(int id, int size) {
        if(random_devices[id] == null || id >= random_devices.length)
        {
            throw new IllegalArgumentException("No random device found");
        }

        Random rand = random_devices[id];

        byte[] random_bytes = new byte[size];

        rand.nextBytes(random_bytes); //used to fill a byte array with random numbers

        return random_bytes;
    }

    @Override
    public void Seek(int id, int to) {
        if(random_devices[id] == null || id >= random_devices.length)
        {
            throw new IllegalArgumentException("No random device found");
        }

        Random rand = random_devices[id];

        byte[] random_bytes = new byte[to];

        rand.nextBytes(random_bytes); //used to fill a byte array with random numbers but not return them

    }

    /*
    do nothing just return 0
     */
    @Override
    public int Write(int id, byte[] data) {
        return 0;
    }
}
