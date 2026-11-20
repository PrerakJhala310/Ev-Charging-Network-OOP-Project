// PARENT CLASS: Person
// Every person in our system has a name, an email and a phone number.
// The User class inherits (extends) this class.
// Concepts used: class, object, encapsulation, parameterized constructor

public class Person {

    // Private data members: they can only be changed through methods (encapsulation)
    private String name;
    private String email;
    private String phone;

    // Parameterized constructor: runs automatically when an object is created
    public Person(String name, String email, String phone) {
        this.name = name;
        this.email = email;
        this.phone = phone;
    }

    // Getter methods: give read access to the private data
    public String getName()
    {
        return name;
    }

    public String getEmail() 
    {
        return email;
    }

    public String getPhone() 
    {
        return phone;
    }

    // Shows the person's details. The child class User overrides this method.
    public void displayDetails()
    {
        System.out.println("Name   : " + name);
        System.out.println("Email  : " + email);
        System.out.println("Phone  : " + phone);
    }
}
