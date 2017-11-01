package hd;

import hd.events.*;

import java.io.OutputStream;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Random;

public class LogGenerator {

    public enum DriverMode {
        SLOW(1.1f),
        NORMAL(1),
        FAST(0.9f);

        private final float modifier;

        private DriverMode(float modifier) {
            this.modifier = modifier;
        }

        public float getModifier() {
            return modifier;
        }
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

    private DriverMode driverMode = DriverMode.NORMAL;

    private PassengersAmount passengersAmount = PassengersAmount.NORMAL;

    private final Random random = new Random();

    public void generate(OutputStream outputStream) {
        Queue<Event> events = new LinkedList<Event>();
        int busRideDuration = 0;
        for(BusStop stop : busStops) {
            busRideDuration += stop.getEstimatedDriveTimeToNextStop(); // sum up all the estimated times
        }
        busRideDuration *= driverMode.getModifier();
        busRideDuration += (busStops.size() + 1) * 30; // +30sec for every bus stop

        int lastStopIndex = -1;
        boolean triggerNextStop = true;

        float currentX = busStops.get(0).getX();
        float currentY = busStops.get(0).getY();

        int leftStopAt = 0;

        for(int i = 0; i < busRideDuration; i++) {
            int currentTimeStamp = startTimestamp + i;
            if(lastStopIndex < busStops.size() - 1) {
                if(Math.abs(currentX - busStops.get(lastStopIndex + 1).getX()) < 0.01f && Math.abs(currentY - busStops.get(lastStopIndex + 1).getY()) < 0.01f) {
                    triggerNextStop = true;
                }
            }
            if(triggerNextStop) {
                triggerNextStop = false;
                lastStopIndex++;
                events.add(new OpenDoorsEvent(currentTimeStamp, driverId, busId, busStops.get(lastStopIndex).getId()));
                int passengerCount = passengerCount(lastStopIndex);
                int passengersDiff = 0;
                if(lastStopIndex > 0) {
                    passengersDiff = passengerCount - passengerCount(lastStopIndex - 1);
                }
                int passengersExiting = 0;
                if(passengerCount > 0 && lastStopIndex != 0) {
                    passengersExiting = random.nextInt(passengerCount);
                }
                for(int j = 0; j < passengersExiting; j++) {
                    events.add(new PassengerOutEvent(currentTimeStamp, driverId, busId));
                }
                for(int j = 0; j < passengerCount + passengersDiff - passengersExiting; j++) {
                    events.add(new PassengerInEvent(currentTimeStamp, driverId, busId));
                }
                if(lastStopIndex == busStops.size() - 1) {
                    for(int j = 0; j < passengerCount(lastStopIndex - 1); j++) {
                        events.add(new PassengerOutEvent(currentTimeStamp, driverId, busId));
                    }
                }
                i += 30;
                currentTimeStamp = startTimestamp + i;
                leftStopAt = currentTimeStamp;
                events.add(new CloseDoorsEvent(currentTimeStamp, driverId, busId));
            }  else {
                if(lastStopIndex < busStops.size() - 1) {
                    int transitTime = (int)(busStops.get(lastStopIndex).getEstimatedDriveTimeToNextStop() * driverMode.getModifier());
                    int timeSinceLastStop = currentTimeStamp - leftStopAt;
                    float progress = (float) timeSinceLastStop / transitTime;
                    float prevStopX = busStops.get(lastStopIndex).getX();
                    float prevStopY = busStops.get(lastStopIndex).getY();
                    float nextStopX = busStops.get(lastStopIndex + 1).getX();
                    float nextStopY = busStops.get(lastStopIndex + 1).getY();
                    float xDistance = nextStopX - prevStopX;
                    float yDistance = nextStopY - prevStopY;
                    currentX = prevStopX + (xDistance * progress);
                    currentY = prevStopY + (yDistance * progress);
                }
            }
            if(i % 10 == 0) { // log GPS
                events.add(new GpsEvent(startTimestamp + i, driverId, busId, currentX, currentY));
            }
        }
        for(Event event : events) {
            event.Print(outputStream);
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
        currentStop += 1;
        float progress = (float) currentStop / busStops.size();
        int amount = passengersAmount.getValue();
        return (int)(progress < 0.5f ? amount * progress : amount * (1 - progress));
    }

}
