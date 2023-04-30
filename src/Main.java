public class Main {
    public static void main(String[] args) {
        System.out.println("Main thread");
        int quantity = 3;
        int period = 3000;
        Thread managerThread = new Thread(Manager.getManager());
        Thread requestThread = new Thread(new Generation(quantity, period));
        managerThread.start();
        requestThread.start();
        try {
            managerThread.join();
            requestThread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
