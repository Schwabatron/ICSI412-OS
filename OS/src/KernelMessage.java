import java.util.Arrays;

public class KernelMessage {
    //sender PID
    public int sender_PID;

    //Receiving PID
    public int receiver_PID;

    //int to indicate what the messages purpose is for
    public int message_type;

    //whatever the applications want it to be (payload for data)
    public byte[] data;


    public KernelMessage(int senderPid, int targetPid, int messageType, byte[] data) { //reg constructor
        this.sender_PID = senderPid;
        this.receiver_PID = targetPid;
        this.message_type = messageType;
        this.data = data.clone();
    }

    public KernelMessage(KernelMessage original) { //copy constructor
        this.sender_PID = original.sender_PID;
        this.receiver_PID = original.receiver_PID;
        this.message_type = original.message_type;
        this.data = original.data.clone();
    }

    @Override
    public String toString() { //toString method
        return "KernelMessage [sender_PID=" + sender_PID + ", receiver_PID=" + receiver_PID + ", message_type=" + message_type + ", data=" + new String(data) + "]";
    }





}
