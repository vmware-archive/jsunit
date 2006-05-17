package net.jsunit.repository;

import net.jsunit.model.User;

public interface UserRepository {

    User find(String emailAddress, String password);

    void save(User user);

}
