// CLASS: CardPayment (implements Payable)
// A simulation only. We keep just the last 4 digits of the card, like real bills do.

public class CardPayment implements Payable {

    private String lastFourDigits;

    public CardPayment(String cardNumber) {
        // substring takes the last 4 characters of the card number
        this.lastFourDigits = cardNumber.substring(cardNumber.length() - 4);
    }

    @Override
    public void pay(double amount) {
        System.out.println("Charging Rs. " + amount + " to card ending " + lastFourDigits + "...");
        System.out.println("Card payment successful.");
    }

    @Override
    public String getMethodName() {
        return "Card ending " + lastFourDigits;
    }
}
