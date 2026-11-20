// CUSTOM EXCEPTION 2: ChargerNotAvailableException
// Thrown when every charger of the chosen type at a station is busy.

public class ChargerNotAvailableException extends Exception {

    public ChargerNotAvailableException(String message) {
        super(message);
    }
}
