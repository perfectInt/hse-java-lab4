public class Request {
    public final int targetFloor;
    public final int callingFloor;
    public Direction direction;

    public Request(int targetFloor, int callingFloor) {
        this.targetFloor = targetFloor;
        this.callingFloor = callingFloor;
        direction = targetFloor > callingFloor ? Direction.UP : Direction.DOWN;
    }

    @Override
    public String toString() {
        return "Request{" +
                "targetFloor=" + targetFloor +
                ", callingFloor=" + callingFloor +
                ", direction=" + direction +
                '}';
    }
}
