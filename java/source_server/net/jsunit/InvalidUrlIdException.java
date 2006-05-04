package net.jsunit;

public class InvalidUrlIdException extends Exception {
    private String idString;

    public InvalidUrlIdException(String idString) {
        this.idString = idString;
    }

    public String getIdString() {
        return idString;
    }
}
