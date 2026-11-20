// CLASS: Transaction
// One booking and its bill: who charged, where, how much, and how they paid.
// Concepts used: constructor overloading, encapsulation, runtime polymorphism,
//                ArrayList, wrapper classes (Double.parseDouble, Integer.parseInt)

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;

public class Transaction {

    private String transactionId;
    private String date;            // for example 2026-10-14
    private String time;            // for example 10:45
    private String userEmail;
    private String userName;
    private String carNumber;
    private String carType;
    private String area;
    private String stationName;
    private String chargerId;
    private String chargerType;
    private double units;           // energy in kWh
    private int minutes;            // charging time
    private double energyCost;
    private double gst;             // 18% tax
    private double totalAmount;
    private String paymentMethod;

    // CONSTRUCTOR 1: used when the user makes a NEW booking
    public Transaction(String transactionId, User user, Station station, ChargingPoint charger, double units) {
        this.transactionId = transactionId;
        this.date = LocalDate.now().toString();
        this.time = LocalTime.now().toString().substring(0, 5);   // keep only hours:minutes
        this.userEmail = user.getEmail();
        this.userName = user.getName();
        this.carNumber = user.getCarNumber();
        this.carType = user.getCarType();
        this.area = station.getArea();
        this.stationName = station.getStationName();
        this.chargerId = charger.getPointId();
        this.chargerType = charger.getChargerType();
        this.units = round(units);

        // Runtime polymorphism: Java runs the AC or the Fast version of these methods
        this.minutes = charger.chargingTime(units);
        this.energyCost = round(charger.calculateCost(units));

        this.gst = round(energyCost * 0.18);
        this.totalAmount = round(energyCost + gst);
        this.paymentMethod = "Pending";
    }

    // CONSTRUCTOR 2 (constructor overloading): builds a transaction from one line of transactions.txt
    public Transaction(String fileLine) {
        String[] parts = fileLine.split(",");
        transactionId = parts[0];
        date = parts[1];
        time = parts[2];
        userEmail = parts[3];
        userName = parts[4];
        carNumber = parts[5];
        carType = parts[6];
        area = parts[7];
        stationName = parts[8];
        chargerId = parts[9];
        chargerType = parts[10];
        units = Double.parseDouble(parts[11]);       // wrapper class: text to double
        minutes = Integer.parseInt(parts[12]);       // wrapper class: text to int
        energyCost = Double.parseDouble(parts[13]);
        gst = Double.parseDouble(parts[14]);
        totalAmount = Double.parseDouble(parts[15]);
        paymentMethod = parts[16];
    }

    // Rounds a number to 2 decimal places, for example 123.456 becomes 123.46
    private double round(double value) {
        return Math.round(value * 100) / 100.0;
    }

    // Getter and setter methods
    public String getTransactionId() {
        return transactionId;
    }

    public String getDate() {
        return date;
    }

    public String getTime() {
        return time;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public String getStationName() {
        return stationName;
    }

    public double getUnits() {
        return units;
    }

    public int getMinutes() {
        return minutes;
    }

    public double getEnergyCost() {
        return energyCost;
    }

    public double getGst() {
        return gst;
    }

    public double getTotalAmount() {
        return totalAmount;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    // One line of text to save in transactions.txt
    public String toFileLine() {
        return transactionId + "," + date + "," + time + "," + userEmail + "," + userName + ","
                + carNumber + "," + carType + "," + area + "," + stationName + "," + chargerId + ","
                + chargerType + "," + units + "," + minutes + "," + energyCost + "," + gst + ","
                + totalAmount + "," + paymentMethod;
    }

    // One row for the Excel (CSV) history file
    public String toExcelRow() {
        return transactionId + "," + date + "," + time + "," + userName + "," + carNumber + ","
                + carType + "," + area + "," + stationName + "," + chargerType + " (" + chargerId + "),"
                + units + "," + minutes + "," + energyCost + "," + gst + "," + totalAmount + "," + paymentMethod;
    }

    // All the lines of the bill, stored in an ArrayList.
    // The same lines are printed on screen AND written into the bill file.
    public ArrayList<String> getBillLines() {
        ArrayList<String> lines = new ArrayList<String>();
        lines.add("EV CHARGING NETWORK");
        lines.add("Charging Bill");
        lines.add("");
        lines.add("Bill No.        : " + transactionId);
        lines.add("Date and Time   : " + date + "  " + time);
        lines.add("Customer        : " + userName);
        lines.add("Email           : " + userEmail);
        lines.add("Car             : " + carNumber + " (" + carType + ")");
        lines.add("");
        lines.add("Station         : " + stationName + ", " + area);
        lines.add("Charger         : " + chargerId + " (" + chargerType + " charger)");
        lines.add("Energy charged  : " + units + " kWh");
        lines.add("Charging time   : about " + minutes + " minutes");
        lines.add("");
        lines.add("Energy cost     : Rs. " + energyCost);
        lines.add("GST (18%)       : Rs. " + gst);
        lines.add("TOTAL           : Rs. " + totalAmount);
        lines.add("Paid by         : " + paymentMethod);
        lines.add("");
        lines.add("Thank you for charging with us. Drive green!");
        return lines;
    }

    // Prints the bill on the screen using an enhanced for loop
    public void printBill() {
        System.out.println();
        for (String line : getBillLines()) {
            System.out.println("  " + line);
        }
    }

    // A short one-line summary, used in the history list
    public void printShort() {
        System.out.println("  " + transactionId + "   " + date + " " + time + "   " + stationName
                + "   " + units + " kWh   Rs. " + totalAmount + "   " + paymentMethod);
    }
}
