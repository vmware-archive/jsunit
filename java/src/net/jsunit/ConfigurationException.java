package net.jsunit;

public class ConfigurationException extends Exception {
    private String propertyInError;

    public ConfigurationException(String property, Exception exception) {
        super(exception);
        this.propertyInError = property;
    }

    public String getPropertyInError() {
        return propertyInError;
    }
}
