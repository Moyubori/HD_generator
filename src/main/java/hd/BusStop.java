package hd;

public class BusStop {

    private float x;
    private float y;

    private String id;

    private int estimatedDriveTimeToNextStop;

    public BusStop(float x, float y, String id, int estimatedDriveTimeToNextStop) {
        this.x = x;
        this.y = y;
        this.id = id;
        this.estimatedDriveTimeToNextStop = estimatedDriveTimeToNextStop;
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public String getId() {
        return id;
    }

    public int getEstimatedDriveTimeToNextStop() {
        return estimatedDriveTimeToNextStop;
    }

}
