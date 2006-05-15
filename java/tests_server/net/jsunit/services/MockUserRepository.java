package net.jsunit.services;

import net.jsunit.model.UserRepository;
import net.jsunit.model.User;

public class MockUserRepository implements UserRepository {

    public static final String VALID_USERNAME = "validUsername";
    public static final String VALID_PASSWORD = "validPassword";
    public User savedUser;

    public User find(String username, String password) {
        if (username.equals(VALID_USERNAME) && password.equals(VALID_PASSWORD))
            return new User();
        else
            return null;
    }

    public void save(User user) {
        savedUser = user;
    }

}
