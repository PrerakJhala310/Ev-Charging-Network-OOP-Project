// CLASS: Station
// One charging station, for example "DN Nagar" in "Andheri".
// Every station has 4 chargers stored in an array of objects: 2 AC and 2 Fast.
// Concepts used: array of objects, constructor, method overloading, instanceof,
//                runtime polymorphism, throwing a custom exception

import java.util.Random;

public class Station {

    private String stationName;
    private String area;
    private ChargingPoint[] chargers = new ChargingPoint[4];   // array of objects

    // Constructor: creates the station and its 4 chargers.
    // The array type is the parent class ChargingPoint, so it can hold both AC and Fast chargers.
    public Station(String stationName, String area, String code) {
        this.stationName = stationName;
        this.area = area;
        chargers[0] = new ACCharger(code + "-AC1");
        chargers[1] = new ACCharger(code + "-AC2");
        chargers[2] = new FastCharger(code + "-FAST1");
        chargers[3] = new FastCharger(code + "-FAST2");
    }

    public String getStationName() {
        return stationName;
    }

    public String getArea() {
        return area;
    }

    // Random traffic, so the station looks like a real busy station.
    // A random number 0, 1 or 2 decides what happens:
    //   0 = all chargers free,  1 = some busy and some free,  2 = station full
    public void simulateTraffic(Random random) {
        int situation = random.nextInt(3);

        for (int i = 0; i < chargers.length; i++) {
            boolean makeBusy = false;
            if (situation == 1) {
                makeBusy = random.nextBoolean();
            }
            if (situation == 2) {
                makeBusy = true;
            }
            if (makeBusy) {
                chargers[i].occupy(10 + random.nextInt(51));   // busy for 10 to 60 minutes
            }
        }
    }

    // Checks if a charger is the type the user wants (1 = AC, 2 = Fast).
    // instanceof tells us which child class the object belongs to.
    private boolean isType(ChargingPoint charger, int type) {
        if (type == 1 && charger instanceof ACCharger) {
            return true;
        }
        if (type == 2 && charger instanceof FastCharger) {
            return true;
        }
        return false;
    }

    // METHOD OVERLOADING: two methods with the same name but different parameters

    // Version 1: counts ALL free chargers
    public int countFree() {
        int free = 0;
        for (int i = 0; i < chargers.length; i++) {
            if (chargers[i].isAvailable()) {
                free++;
            }
        }
        return free;
    }

    // Version 2: counts free chargers of ONE type (1 = AC, 2 = Fast)
    public int countFree(int type) {
        int free = 0;
        for (int i = 0; i < chargers.length; i++) {
            if (isType(chargers[i], type) && chargers[i].isAvailable()) {
                free++;
            }
        }
        return free;
    }

    // Shortest waiting time among busy chargers (type 0 means any type)
    public int shortestWait(int type) {
        int shortest = 999;
        for (int i = 0; i < chargers.length; i++) {
            boolean rightType = (type == 0) || isType(chargers[i], type);
            if (rightType && !chargers[i].isAvailable() && chargers[i].getMinutesLeft() < shortest) {
                shortest = chargers[i].getMinutesLeft();
            }
        }
        return shortest;
    }

    // Finds a free charger of the chosen type.
    // If none is free, it throws our own ChargerNotAvailableException.
    public ChargingPoint findFreeCharger(int type) throws ChargerNotAvailableException {
        for (int i = 0; i < chargers.length; i++) {
            if (isType(chargers[i], type) && chargers[i].isAvailable()) {
                return chargers[i];
            }
        }
        throw new ChargerNotAvailableException("All chargers of this type at " + stationName
                + " are busy. Next one is free in " + shortestWait(type) + " minutes.");
    }

    // Short status, for example "3 of 4 free" or "FULL, next free in 15 min"
    public String getStatus() {
        if (countFree() == 0) {
            return "FULL, next free in " + shortestWait(0) + " min";
        }
        return countFree() + " of 4 free";
    }

    public void displayDetails() {
        System.out.println();
        System.out.println("  " + stationName + "  (" + getStatus() + ")");
        for (int i = 0; i < chargers.length; i++) {
            chargers[i].displayDetails();
        }
    }
}
