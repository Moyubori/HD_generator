package hd;

public class BusStop {

    private float x;

    private float y;

    private String name;

    private int estimatedDriveTimeToNextStop;

    public BusStop(float x, float y, String name, int estimatedDriveTimeToNextStop) {
        this.x = x;
        this.y = y;
        this.name = name;
        this.estimatedDriveTimeToNextStop = estimatedDriveTimeToNextStop;
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public String getName() {
        return name;
    }

    public int getEstimatedDriveTimeToNextStop() {
        return estimatedDriveTimeToNextStop;
    }
}
