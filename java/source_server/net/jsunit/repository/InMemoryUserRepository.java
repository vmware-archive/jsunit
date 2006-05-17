package net.jsunit.repository;

import net.jsunit.model.User;

import java.util.List;
import java.util.ArrayList;

public class InMemoryUserRepository implements UserRepository {
    private List<User> users = new ArrayList<User>();

    public void save(User user) {
        users.add(user);
    }

    public User find(String emailAddress, String password) {
        for (User user : users)
            if (user.getEmailAddress().equals(emailAddress) && user.getPassword().equals(password))
                return user;
        return null;
    }
}
