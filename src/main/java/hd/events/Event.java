package hd.events;

import java.io.IOException;
import java.io.OutputStream;

public abstract class Event {

    protected int timeStamp;

    protected String driverId;

    protected String busId;

    public Event(int timeStamp, String driverId, String busId) {
        this.timeStamp = timeStamp;
        this.driverId = driverId;
        this.busId = busId;
    }

    public abstract void Print(OutputStream outputStream);

    protected void writeToOutputStream(OutputStream outputStream, String strToWrite) {
        try {
            outputStream.write(strToWrite.getBytes());
            outputStream.write("\n".getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
