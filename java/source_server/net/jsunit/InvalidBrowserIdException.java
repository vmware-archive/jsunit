package net.jsunit;

public class InvalidBrowserIdException extends Throwable {
    private String idString;

    public InvalidBrowserIdException(int invalidId) {
        this(String.valueOf(invalidId));
    }

    public InvalidBrowserIdException(String invalidIdString) {
        super("Invalid browser ID: " + invalidIdString);
        this.idString = String.valueOf(invalidIdString);
    }

    public String getIdString() {
        return idString;
    }
}
