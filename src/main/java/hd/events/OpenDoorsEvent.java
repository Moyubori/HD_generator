package hd.events;

import java.io.IOException;
import java.io.OutputStream;

public class OpenDoorsEvent extends Event {

    private final String EVENT_SYMBOL = "O";

    private String stopId;

    public OpenDoorsEvent(int timeStamp, String driverId, String busId, String stopId) {
        super(timeStamp, driverId, busId);
        this.stopId = stopId;
    }

    public void Print(OutputStream outputStream) {
        String log = (new StringBuilder())
                .append(busId).append(",")
                .append(driverId).append(",")
                .append(timeStamp).append(",")
                .append(EVENT_SYMBOL).append(",")
                .append(stopId)
                .toString();
        writeToOutputStream(outputStream,log);
    }
}
