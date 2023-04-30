import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class Manager implements Runnable {
    private static Manager uniqueManager = new Manager();
    List<Request> requests = new ArrayList<>();
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

    public synchronized int getBestElevator(Request request) {
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

    public int checkMatches() {
        return 1;
    }

    public Request pollRequest() {
        if (!isEmpty()) {
            Request request = requests.get(0);
            requests.remove(0);
            return request;
        } else {
            return null;
        }
    }

    public Request peekRequest() {
        return !isEmpty() ? requests.get(0) : null;
    }
}
