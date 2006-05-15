package net.jsunit.model;

import net.jsunit.utility.StringUtility;

public class User {
    private String firstName;
    private String lastName;
    private String emailAddress;
    private String password;

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public String getPassword() {
        return password;
    }

    public boolean isValid() {
        return !StringUtility.isEmpty(getFirstName()) &&
                !StringUtility.isEmpty(getLastName()) &&
                !StringUtility.isEmpty(getEmailAddress()) &&
                !StringUtility.isEmpty(getPassword());
    }
}
