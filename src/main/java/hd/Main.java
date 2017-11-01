package hd;

import javax.inject.Inject;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Main {

    public static void main(String[] args) throws IOException {

        LogGenerator logGenerator = new LogGenerator();
        List<BusStop> stops = Arrays.asList(
                new BusStop(0,0, "0", 90),
                new BusStop(10, 10, "1", 60),
                new BusStop(-10, 0, "2", 0)
        );
        logGenerator.setBusId("123");
        logGenerator.setDriverId("456");
        logGenerator.setBusStops(stops);
        logGenerator.setStartTimestamp(0);
        logGenerator.generate(System.out);
    }

}
