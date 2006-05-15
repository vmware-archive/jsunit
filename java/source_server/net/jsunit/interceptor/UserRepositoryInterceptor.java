package net.jsunit.interceptor;

import com.opensymphony.xwork.Action;
import net.jsunit.action.UserRepositoryAware;
import net.jsunit.model.UserRepository;
import net.jsunit.model.DefaultUserRepository;

public class UserRepositoryInterceptor extends JsUnitInterceptor {

    public static UserRepositoryFactory factory = new UserRepositoryFactory() {
        public UserRepository create() {
            return new DefaultUserRepository();
        }
    };

    protected void execute(Action targetAction) throws Exception {
        UserRepositoryAware aware = (UserRepositoryAware) targetAction;
        aware.setUserRepository(factory.create());
    }

    public interface UserRepositoryFactory {
        UserRepository create();
    }
}
