// CHILD CLASS: ACCharger (normal charger, slower and cheaper)
// Concepts used: inheritance, method overriding, runtime polymorphism

public class ACCharger extends ChargingPoint {

    private double ratePerUnit = 12;   // Rs. 12 for every kWh
    private double speed = 7;          // charges 7 kWh in one hour

    public ACCharger(String pointId) {
        super(pointId);
    }

    // Cost = units x 12
    @Override
    public double calculateCost(double units) {
        return units * ratePerUnit;
    }

    // Time in minutes = units x 60 / 7
    @Override
    public int chargingTime(double units) {
        return (int) (units * 60 / speed);
    }

    @Override
    public String getChargerType() {
        return "AC";
    }
}
