package net.jsunit.services;

public class DefaultUserRepository implements UserRepository {
    public User findUser(String username, String password) {
        return new User();
    }
}
