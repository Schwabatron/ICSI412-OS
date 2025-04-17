public class Hardware {


    private final static int PAGE_SIZE = 1024; //Page size

    public static byte[] memory = new byte[1_048_567]; //memory

    public static int[][] TLB = new int[2][2]; //2x2 array to represent the TLB

    //Methods to simulate Load and Store instructions as seen in assembly
    public static byte Read(int address) {
        int page_num = address/PAGE_SIZE; //get the page number
        int physical_address = getPhysicalAddress(address, page_num);

        if (physical_address != -1)
            return memory[physical_address];

        OS.GetMapping(page_num);
        physical_address = getPhysicalAddress(address, page_num);

        if (physical_address != -1)
            return memory[physical_address];

        OS.Exit();
        return 0;
    }

    private static int getPhysicalAddress(int address, int page_num) {
        int physical_address = -1;
        for(int i = 0; i < TLB.length; i++) {
                if(TLB[i][0] == page_num) {
                    int mapping = TLB[i][1];
                    int offset = address % PAGE_SIZE;

                    physical_address = mapping * PAGE_SIZE + offset; //getting the physical address
                }
            }
        return physical_address;
    }

    public static void Write(int address, byte value) {
            int page_num = address/PAGE_SIZE; //get the page number
            int physical_address = getPhysicalAddress(address, page_num);
            if (physical_address != -1)
            {
                memory[physical_address] = value;
                return;
            }
            OS.GetMapping(page_num);
            physical_address = getPhysicalAddress(address, page_num);
            if(physical_address != -1)
            {
                memory[physical_address] = value;
                return;
            }

            OS.Exit();
        
    }

    public static void ClearTLB()
    {
        for (int i = 0; i < TLB.length; i++) {
            TLB[i][0] = -1; // Invalidate VPN
            TLB[i][1] = -1; // Invalidate PPN
        }
    }



}
