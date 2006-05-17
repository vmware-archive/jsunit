package net.jsunit.action;

import net.jsunit.repository.UserRepository;

public interface UserRepositoryAware {

    void setUserRepository(UserRepository repository);
}
