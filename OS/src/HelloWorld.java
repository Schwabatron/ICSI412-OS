public class HelloWorld extends UserlandProcess { //Create main method that makes an infinite loop that just prints hello world make sure that each calls cooperate and thread.sleep(50)
    @Override
    public void main() {
        while(true)
        {
            try {
                cooperate();
                Thread.sleep(50);
                System.out.println("Hello World!");
            } catch (Exception e) { }
        }
    }
}
