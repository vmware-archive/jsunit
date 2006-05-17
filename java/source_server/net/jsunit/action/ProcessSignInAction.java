package net.jsunit.action;

import com.opensymphony.xwork.Action;
import net.jsunit.model.User;
import net.jsunit.repository.UserRepository;

public class ProcessSignInAction implements Action, UserRepositoryAware {
    private UserRepository repository;
    private String emailAddress;
    private String password;

    public String execute() throws Exception {
        User user = repository.find(emailAddress, password);
        return user != null ? SUCCESS : INPUT;
    }

    public void setUserRepository(UserRepository repository) {
        this.repository = repository;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    public void setPassword(String password) {
        this.password = password;
    }

}
