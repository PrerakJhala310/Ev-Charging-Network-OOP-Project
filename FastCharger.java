// CHILD CLASS: FastCharger (DC fast charger, quicker but costs more)
// Concepts used: inheritance, method overriding, runtime polymorphism

public class FastCharger extends ChargingPoint {

    private double ratePerUnit = 20;   // Rs. 20 for every kWh
    private double serviceFee = 50;    // Rs. 50 extra for every session
    private double speed = 50;         // charges 50 kWh in one hour

    public FastCharger(String pointId) {
        super(pointId);
    }

    // Cost = units x 20 + 50
    // Same method name as in ACCharger but a different formula: this is runtime polymorphism
    @Override
    public double calculateCost(double units) {
        return units * ratePerUnit + serviceFee;
    }

    // Time in minutes = units x 60 / 50
    @Override
    public int chargingTime(double units) {
        return (int) (units * 60 / speed);
    }

    @Override
    public String getChargerType() {
        return "Fast";
    }
}
