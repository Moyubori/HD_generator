package hd.events;

import java.io.IOException;
import java.io.OutputStream;

public class GpsEvent extends Event {

    private final String EVENT_SYMBOL = "G";

    private float positionX, positionY;

    public GpsEvent(int timeStamp, String driverId, String busId, float posX, float posY) {
        super(timeStamp, driverId, busId);
        positionX = posX;
        positionY = posY;
    }

    public void Print(OutputStream outputStream) {
        String log = (new StringBuilder())
                .append(busId).append(",")
                .append(driverId).append(",")
                .append(timeStamp).append(",")
                .append(EVENT_SYMBOL).append(",")
                .append("\"").append(positionX).append(";").append(positionY).append("\"")
                .toString();
        writeToOutputStream(outputStream,log);
    }
}
