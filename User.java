// CHILD CLASS: User (inherits from Person)
// A user signs up once with a password and car details, then signs in with email and password.
// Concepts used: inheritance (extends), super, method overriding, custom exception (throws)

public class User extends Person
{

    // Extra data that only a User has
    private String password;
    private String carNumber;
    private String carType;     // Hatchback, Sedan or SUV

    // Constructor: name, email and phone are sent to the Person constructor using super()
    public User(String name, String email, String phone, String password, String carNumber, String carType) 
    {
        super(name, email, phone);
        this.password = password;
        this.carNumber = carNumber;
        this.carType = carType;
    }

    public String getCarNumber()
    {
        return carNumber;
    }

    public String getCarType() 
    {
        return carType;
    }

    // Battery size depends on the car type (in kWh)
    public double getBatteryCapacity() 
    {
        if (carType.equals("Hatchback")) 
        {
            return 30;
        } else if (carType.equals("Sedan"))
        {
            return 40;
        } else 
        {
            return 60;
        }
    }

    // Checks the password typed at sign in.
    // If it is wrong, it throws our own InvalidLoginException.
    public void checkPassword(String enteredPassword) throws InvalidLoginException {
        if (!password.equals(enteredPassword)) {
            throw new InvalidLoginException("Wrong password. Please try again.");
        }
    }

    // Makes one line of text to save this user in users.txt
    public String toFileLine() 
    {
        return getName() + "," + getEmail() + "," + getPhone() + "," + password + "," + carNumber + "," + carType;
    }

    // Method overriding: same method as in Person, with car details added
    @Override
    public void displayDetails()
   {
        super.displayDetails();    // prints name, email and phone from Person
        System.out.println("Car    : " + carNumber + " (" + carType + ")");
        System.out.println("Battery: " + getBatteryCapacity() + " kWh");
    }
}
