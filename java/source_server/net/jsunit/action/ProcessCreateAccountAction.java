package net.jsunit.action;

import com.opensymphony.xwork.Action;
import net.jsunit.model.User;
import net.jsunit.model.UserRepository;

public class ProcessCreateAccountAction implements Action, UserRepositoryAware {

    private User newUser = new User();
    private UserRepository repository;
    private String password1;
    private String password2;

    public String execute() throws Exception {
        if (password1 != null && password1.equals(password2))
            newUser.setPassword(password1);
        if (newUser.isValid()) {
            repository.save(newUser);
            return SUCCESS;
        } else
            return INPUT;
    }

    public User getUser() {
        return newUser;
    }

    public void setUserRepository(UserRepository repository) {
        this.repository = repository;
    }

    public void setPassword1(String password1) {
        this.password1 = password1;
    }

    public void setPassword2(String password2) {
        this.password2 = password2;
    }
}
