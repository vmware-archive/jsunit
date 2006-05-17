package net.jsunit.action;

import junit.framework.TestCase;
import net.jsunit.repository.MockUserRepository;
import net.jsunit.model.User;

public class ProcessCreateAccountActionTest extends TestCase {
    private MockUserRepository mockUserRepository;
    private ProcessCreateAccountAction action;

    protected void setUp() throws Exception {
        super.setUp();
        mockUserRepository = new MockUserRepository();
        action = new ProcessCreateAccountAction();
        action.setUserRepository(mockUserRepository);
    }

    public void testValid() throws Exception {
        User user = action.getUser();
        assertNotNull(user);
        makeUserValidExceptForPassword(user);
        action.setPassword1("password123");
        action.setPassword2("password123");
        assertEquals(ProcessCreateAccountAction.SUCCESS, action.execute());
        assertEquals(user, mockUserRepository.savedUser);
    }

    public void testPasswordMismatch() throws Exception {
        User user = action.getUser();
        assertNotNull(user);
        makeUserValidExceptForPassword(user);
        action.setPassword1("password123");
        action.setPassword2("password321");
        assertEquals(ProcessCreateAccountAction.INPUT, action.execute());
        assertNull(mockUserRepository.savedUser);
    }

    public void testInvalid() throws Exception {
        assertEquals(ProcessCreateAccountAction.INPUT, action.execute());
        assertNull(mockUserRepository.savedUser);
    }

    private void makeUserValidExceptForPassword(User user) {
        user.setFirstName("John");
        user.setLastName("Smith");
        user.setEmailAddress("johnsmith@example.com");
    }

}
