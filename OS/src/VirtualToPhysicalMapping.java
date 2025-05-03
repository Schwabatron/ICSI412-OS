public class VirtualToPhysicalMapping {

    //Members
    public int physical_page_number; //physical page number

    public int disk_page_number; //Disk page number


    //Constructor setting both physical page number and disk page number to negative one
    public VirtualToPhysicalMapping() {
        physical_page_number = -1;
        disk_page_number = -1;
    }
}
