// CLASS: UpiPayment (implements Payable)
// A simulation only. No real money is moved.

public class UpiPayment implements Payable {

    private String upiId;

    public UpiPayment(String upiId) {
        this.upiId = upiId;
    }

    @Override
    public void pay(double amount) {
        System.out.println("Sending payment request of Rs. " + amount + " to " + upiId + "...");
        System.out.println("UPI payment successful.");
    }

    @Override
    public String getMethodName() {
        return "UPI (" + upiId + ")";
    }
}
