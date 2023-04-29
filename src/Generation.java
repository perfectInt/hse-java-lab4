public class Generation implements Runnable {
    Manager manager = Manager.getManager();

    public Generation(int quantity, int period) {
        this.quantity = quantity;
        this.period = period;
    }

    int quantity = 0;
    int period = 0;

    @Override
    public void run() {
        System.out.println("Generation thread");
        for (int i = 0; i < quantity; i++) {
            int randomCallingFloor = (int) (Math.random() * 10) + 1;
            int randomTargetFloor = (int) (Math.random() * 10) + 1;
            while (randomCallingFloor == randomTargetFloor) {
                randomTargetFloor = (int) (Math.random() * 10) + 1;
            }
            Request request = new Request(randomTargetFloor, randomCallingFloor);
            manager.addRequest(request);
            System.out.println(request);
            try {
                Thread.sleep(period);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        manager.condition = false;
    }
}
