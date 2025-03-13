import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.Random;

public class FakeFileSystem implements Device{


    public RandomAccessFile[] raf_array = new RandomAccessFile[10];

    @Override
    public int Open(String s) { //expect a simple filename as param
        if(s == null || s.isEmpty()) {
            throw new IllegalArgumentException("Not a valid string for Open");
        }

        for(int i = 0; i < raf_array.length; i++)
        {
            if(raf_array[i] != null)
            {
                try {
                    raf_array[i] = new RandomAccessFile(s, "rw");
                } catch (FileNotFoundException e) {
                    throw new RuntimeException(e);
                }
                return i;
            }
        }

        return -1; // returning a placeholder to indicate that there are no more spaces available
    }

    @Override
    public void Close(int id) {
        if(id < 0 || id >= raf_array.length)
        {
            throw new IllegalArgumentException("Not a valid id for Close");
        }


        try {
            raf_array[id].close(); //close the file
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        raf_array[id] = null; //null the space

    }

    @Override
    public byte[] Read(int id, int size) {
        if(id < 0 || id >= raf_array.length || raf_array[id] == null) {
            throw new IllegalArgumentException("Not a valid id for Read");
        }

        byte[] data = new byte[size];

        try {
           int read_bytes = raf_array[id].read(data);

           if(read_bytes == -1 )
           {
               System.out.println("Read returned -1");
               return new byte[0]; //didnt read properly or file is empty
           }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }


        return data;
    }

    @Override
    public void Seek(int id, int to) {
        if(id < 0 || id >= raf_array.length || raf_array[id] == null) {
            throw new IllegalArgumentException("Not a valid id for Seek");
        }

        try {
            raf_array[id].seek(to); //moving the file pointer to "to"
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public int Write(int id, byte[] data) {
        if(id < 0 || id >= raf_array.length || raf_array[id] == null) {
            throw new IllegalArgumentException("Not a valid id for write");
        }

        try {
            raf_array[id].write(data);
            //TODO: What should be returned by the write function?
            return data.length;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
