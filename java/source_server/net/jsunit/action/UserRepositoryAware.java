package net.jsunit.action;

import net.jsunit.model.UserRepository;

public interface UserRepositoryAware {

    void setUserRepository(UserRepository repository);
}
