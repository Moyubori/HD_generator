package hd.events;

import java.io.OutputStream;
import java.util.Random;

public class PassengerOutEvent extends Event {

    private final String EVENT_SYMBOL = "W";

    private int doorId;

    public PassengerOutEvent(int timeStamp, String driverId, String busId) {
        super(timeStamp, driverId, busId);
        doorId = (new Random()).nextInt(3);
    }

    public void Print(OutputStream outputStream) {
        String log = (new StringBuilder())
                .append(busId).append(",")
                .append(driverId).append(",")
                .append(timeStamp).append(",")
                .append(EVENT_SYMBOL).append(",")
                .append(doorId)
                .toString();
        writeToOutputStream(outputStream,log);
    }

}
