public class GoodbyeWorld extends UserlandProcess{ //Create main method that makes an infinite loop that just prints goodbye world make sure that each calls cooperate and thread.sleep(50)
    @Override
    public void main() {
        while(true)
        {
            try {
                System.out.println("Goodbye World!");
                OS.Sleep(200);
                cooperate();
                Thread.sleep(50);
            } catch (Exception e) { }
        }
    }
}
