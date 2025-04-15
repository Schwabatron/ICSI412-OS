public class Hardware {


    private final static int PAGE_SIZE = 1024; //Page size

    public static byte[] memory = new byte[1_048_567]; //memory

    public static int[][] TLB = new int[2][2]; //2x2 array to represent the TLB

    //Methods to simulate Load and Store instructions as seen in assembly
    public static byte Read(int address) {
        while(true) {
            int page_num = address/PAGE_SIZE; //get the page number
            for(int i = 0; i < TLB.length; i++) {
                if(TLB[i][0] == page_num) {
                    int mapping = TLB[i][1];
                    int offset = address % PAGE_SIZE;
                    int physical_address = mapping * PAGE_SIZE + offset; //getting the physical address
                    return memory[physical_address];
                }
            }
            OS.GetMapping(page_num);
        }
    }

    public static void Write(int address, byte value) {

        while(true) {
            int page_num = address/PAGE_SIZE; //get the page number
            for(int i = 0; i < TLB.length; i++) {
                if(TLB[i][0] == page_num) {
                    int mapping = TLB[i][1];
                    int offset = address % PAGE_SIZE;
                    int physical_address = mapping * PAGE_SIZE + offset; //getting the physical address
                    memory[physical_address] = value;
                    return;
                }
            }
            OS.GetMapping(page_num);
        }
    }


}
