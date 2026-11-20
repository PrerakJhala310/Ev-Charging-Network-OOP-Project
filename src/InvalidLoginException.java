// CUSTOM EXCEPTION 1: InvalidLoginException
// Thrown when the email is not registered or the password is wrong.
// Steps (same as our lab): extend Exception, write a constructor that takes a String,
// and pass that message to the parent class using super().

public class InvalidLoginException extends Exception {

    public InvalidLoginException(String message) {
        super(message);
    }
}
