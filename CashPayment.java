// CLASS: CashPayment (implements Payable)
// The user pays cash at the station counter.

public class CashPayment implements Payable {

    @Override
    public void pay(double amount) {
        System.out.println("Please pay Rs. " + amount + " in cash at the station counter.");
        System.out.println("Cash payment recorded.");
    }

    @Override
    public String getMethodName() {
        return "Cash";
    }
}
