package net.jsunit.configuration;

public class ConfigurationException extends RuntimeException {
    private ConfigurationProperty propertyInError;
    private String invalidValue;
 
    public ConfigurationException(ConfigurationProperty property, String invalidValue) {
        this.propertyInError = property;
        this.invalidValue = invalidValue;    	
    }
    
    public ConfigurationException(ConfigurationProperty property, String invalidValue, Exception exception) {
        super(exception);
        this.propertyInError = property;
        this.invalidValue = invalidValue;    	
    }

    public ConfigurationProperty getPropertyInError() {
        return propertyInError;
    }

    public String getInvalidValue() {
        return invalidValue;
    }
}
