package net.jsunit.services;

public interface UserRepository {
    User findUser(String username, String password);
}
