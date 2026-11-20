// INTERFACE: Payable
// An interface is a contract ("CAN-DO" ability).
// Every payment type that implements Payable MUST write these two methods.
// Concepts used: interface, abstraction, runtime polymorphism

public interface Payable
{

    void pay(double amount);

    String getMethodName();
}
