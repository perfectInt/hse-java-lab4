public class Elevator implements Runnable {
    public int id;
    public Direction direction;
    public int currentFloor = 1;
    public int callingFloor;
    public int targetFloor;
    public final static Object obj = new Object();

    public Elevator(int id) {
        this.id = id;
        direction = Direction.WAITING;
    }

    public synchronized void move(int targetFloor, int initialFloor) {
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

    public boolean access(int id) {
        return this.id == id;
    }

    @Override
    public void run() {
        System.out.println("Elevator " + this.id + " thread");
        Manager manager = Manager.getManager();
        while (manager.condition || !manager.isEmpty()) {
            synchronized (Generation.obj) {
                while (manager.isEmpty()) {
                    System.out.println("Ждем заявок");
                    try {
                        Generation.obj.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
            if (manager.peekRequest() == null) continue;
            Request request = manager.pollRequest();
            move(request.callingFloor, this.currentFloor);
            move(request.targetFloor, this.currentFloor);
            synchronized (obj) {
                if (manager.peekRequest() == null) continue;
                if (manager.getBestElevator(manager.peekRequest()) == this.id) {
                    Request req = manager.pollRequest();
                    move(req.callingFloor, this.currentFloor);
                    move(req.targetFloor, this.currentFloor);
                }
            }
        }
    }
}
