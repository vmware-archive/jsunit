package net.jsunit.repository;

import net.jsunit.model.User;

public class MockUserRepository implements UserRepository {

    public static final String VALID_EMAIL_ADDRESS = "validEmailAddress";
    public static final String VALID_PASSWORD = "validPassword";
    public User savedUser;
    public User userToReturn;

    public User find(String emailAddress, String password) {
        if (emailAddress.equals(VALID_EMAIL_ADDRESS) && password.equals(VALID_PASSWORD))
            return new User();
        else
            return null;
    }

    public void save(User user) {
        savedUser = user;
    }

}
