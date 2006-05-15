package net.jsunit.model;

public class DefaultUserRepository implements UserRepository {
    public void save(User user) {
    }

    public User find(String username, String password) {
        return new User();
    }
}
