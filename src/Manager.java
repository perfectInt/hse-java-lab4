import java.util.LinkedList;
import java.util.Queue;

public class Manager implements Runnable {
    private static Manager uniqueManager = new Manager();
    Queue<Request> requests = new LinkedList<>();
    Elevator firstElevator = new Elevator(1);
    Elevator secondElevator = new Elevator(2);
    Thread firstElevatorThread = new Thread(firstElevator);
    Thread secondElevatorThread = new Thread(secondElevator);
    boolean condition = true;

    public static Manager getManager() {
        return uniqueManager;
    }

    @Override
    public void run() {
        System.out.println("Manager thread");
        firstElevatorThread.start();
        secondElevatorThread.start();
        try {
            firstElevatorThread.join();
            secondElevatorThread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public synchronized int getBestElevator(Request request) throws InterruptedException {
        int res = Math.abs(firstElevator.currentFloor - request.callingFloor) < Math.abs(secondElevator.currentFloor - request.callingFloor) ?
                1 : 2;
        if (res == 1) {
            firstElevator.targetFloor = request.targetFloor;
            firstElevator.callingFloor = request.callingFloor;
        } else {
            secondElevator.targetFloor = request.targetFloor;
            secondElevator.callingFloor = request.callingFloor;
        }
        return res;
    }

    public void addRequest(Request request) {
        requests.add(request);
    }

    public boolean isEmpty() {
        return requests.isEmpty();
    }

    public Request pollRequest() {
        if (!isEmpty()) {
            return requests.poll();
        } else {
            return null;
        }
    }
}
