package hd;

import hd.events.*;

import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Random;

public class LogGenerator {

    public enum DriverMode {
        SLOW,
        NORMAL,
        FAST
    };

    public enum PassengersAmount {
        LOW(50),
        NORMAL(150),
        HIGH(300);

        private final int value;

        private PassengersAmount(int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }
    };

    private int startTimestamp;

    private String driverId;

    private String busId;

    private List<BusStop> busStops;

    private DriverMode driverMode;

    private PassengersAmount passengersAmount;

    private final Random random = new Random();

    void generate() {
        Queue<Event> events = new LinkedList<Event>();
        int busRideDuration = 0;
        for(BusStop stop : busStops) {
            busRideDuration += stop.getEstimatedDriveTimeToNextStop(); // sum up all the estimated times
        }
        if(driverMode == DriverMode.FAST) {
            busRideDuration *= 1 - random.nextFloat(); // longer bus ride
        } else if(driverMode == DriverMode.SLOW) {
            busRideDuration *= 1 + random.nextFloat(); // shorter bus ride
        }
        busRideDuration += busStops.size() * 30; // +30sec for every bus stop

        int lastStopIndex = -1;
        boolean triggerNextStop = true;

        for(int i = 0; i < busRideDuration; i++) {
            if(triggerNextStop) {
                triggerNextStop = false;
                events.add(new OpenDoorsEvent());
                lastStopIndex++;
                int passengerCount = passengerCount(lastStopIndex);
                int passengersDiff = passengerCount - passengerCount(lastStopIndex - 1);
                int passengersExiting = random.nextInt(passengerCount);
                for(int j = 0; j < passengersExiting; j++) {
                    events.add(new PassengerOutEvent());
                }
                for(int j = 0; j < passengersDiff + passengersExiting; j++) {
                    events.add(new PassengerInEvent());
                }
                i += 30;
                events.add(new CloseDoorsEvent());
            }
            if(i % 10 == 0) { // log GPS
                events.add(new GpsEvent());
            }

        }
    }

    public void setStartTimestamp(int startTimestamp) {
        this.startTimestamp = startTimestamp;
    }

    public void setDriverId(String driverId) {
        this.driverId = driverId;
    }

    public void setBusId(String busId) {
        this.busId = busId;
    }

    public void setBusStops(List<BusStop> busStops) {
        this.busStops = busStops;
    }

    public void setDriverMode(DriverMode driverMode) {
        this.driverMode = driverMode;
    }

    public void setPassengersAmount(PassengersAmount passengersAmount) {
        this.passengersAmount = passengersAmount;
    }

    private int passengerCount(int currentStop) {
        float progress = (float) currentStop / busStops.size();
        int amount = passengersAmount.getValue() * (random.nextInt(passengersAmount.getValue() / 5) - (passengersAmount.getValue() / 10));
        return (int)(progress < 0.5f ? amount * progress : amount * (1 - progress));
    }

}
