package hd.events;

import java.io.IOException;
import java.io.OutputStream;

public class CloseDoorsEvent extends Event {

    private final String EVENT_SYMBOL = "Z";

    public CloseDoorsEvent(int timeStamp, String driverId, String busId) {
        super(timeStamp, driverId, busId);
    }

    public void Print(OutputStream outputStream) {
        String log = (new StringBuilder())
                        .append(busId).append(",")
                        .append(driverId).append(",")
                        .append(timeStamp).append(",")
                        .append(EVENT_SYMBOL)
                        .toString();
        writeToOutputStream(outputStream,log);
    }

}
