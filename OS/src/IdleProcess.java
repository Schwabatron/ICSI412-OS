public class IdleProcess extends UserlandProcess {
    @Override
    public void main() {
        while (true) {
            try {
                cooperate();
                Thread.sleep(50);
            } catch (Exception e) { }
        }
    }
}
