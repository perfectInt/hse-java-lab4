public class Elevator implements Runnable {
    public int id;
    public Direction direction;
    public int currentFloor = 1;
    public int callingFloor;
    public int targetFloor;
    private final static Object obj = new Object();

    public Elevator(int id) {
        this.id = id;
        direction = Direction.WAITING;
    }

    public void move(int targetFloor, int initialFloor) {
        direction = targetFloor > initialFloor ? Direction.UP : Direction.DOWN;
        while (targetFloor != currentFloor) {
            System.out.println("Elevator " + this.id + " is moving " + direction + " to " + targetFloor + " floor (" + this.currentFloor + ")");
            if (direction == Direction.UP) this.currentFloor++;
            else this.currentFloor--;
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        System.out.println("Elevator " + this.id + " has arrived to " + this.currentFloor + " floor");
    }

    @Override
    public void run() {
        System.out.println("Elevator " + this.id + " thread");
        Manager manager = Manager.getManager();
        while (manager.condition || !manager.isEmpty()) {
            synchronized (obj) {
                Request request = manager.pollRequest();
                if (request == null) continue;
                move(request.callingFloor, this.currentFloor);
                move(request.targetFloor, this.currentFloor);
            }
            Request request = manager.pollRequest();
            if (request == null) continue;
            move(request.callingFloor, this.currentFloor);
            move(request.targetFloor, this.currentFloor);
        }
    }
}
