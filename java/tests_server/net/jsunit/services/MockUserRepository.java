package net.jsunit.services;

public class MockUserRepository implements UserRepository {
    public static final String VALID_USERNAME = "validUsername";
    public static final String VALID_PASSWORD = "validPassword";

    public User findUser(String username, String password) {
        if (username.equals(VALID_USERNAME) && password.equals(VALID_PASSWORD))
            return new User();
        else
            return null;
    }

}
