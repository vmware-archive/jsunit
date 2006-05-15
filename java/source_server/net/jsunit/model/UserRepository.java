package net.jsunit.model;

public interface UserRepository {

    User find(String username, String password);

    void save(User user);

}
