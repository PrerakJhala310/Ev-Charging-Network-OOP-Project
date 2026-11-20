// ABSTRACT CLASS: ChargingPoint
// A general charger. We never create a plain ChargingPoint object,
// we only create its child classes: ACCharger and FastCharger.
// Concepts used: abstract class, abstract methods, concrete methods, encapsulation

public abstract class ChargingPoint {

    private String pointId;       // for example S1-AC1
    private boolean available;    // true = free, false = busy
    private int minutesLeft;      // if busy, minutes until it becomes free

    // Constructor: every new charger starts as free
    public ChargingPoint(String pointId) {
        this.pointId = pointId;
        this.available = true;
        this.minutesLeft = 0;
    }

    // Abstract methods: no body here, each child class writes its own version
    public abstract double calculateCost(double units);
    public abstract int chargingTime(double units);
    public abstract String getChargerType();

    // Concrete methods: same for every charger
    public String getPointId() {
        return pointId;
    }

    public boolean isAvailable() {
        return available;
    }

    public int getMinutesLeft() {
        return minutesLeft;
    }

    // Makes the charger busy for some minutes
    public void occupy(int minutes) {
        available = false;
        minutesLeft = minutes;
    }

    // Makes the charger free again
    public void release() {
        available = true;
        minutesLeft = 0;
    }

    public void displayDetails() {
        if (available) {
            System.out.println("     " + pointId + "   " + getChargerType() + "   FREE");
        } else {
            System.out.println("     " + pointId + "   " + getChargerType() + "   busy, free in " + minutesLeft + " min");
        }
    }
}
