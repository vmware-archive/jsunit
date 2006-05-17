package net.jsunit.action;

import junit.framework.TestCase;
import net.jsunit.repository.MockUserRepository;

public class ProcessSignInActionTest extends TestCase {
    private ProcessSignInAction action;
    private MockUserRepository repository;

    protected void setUp() throws Exception {
        super.setUp();
        action = new ProcessSignInAction();
        repository = new MockUserRepository();
        action.setUserRepository(repository);
    }

    public void testInvalid() throws Exception {
        action.setEmailAddress("joe@example.com");
        action.setPassword("bad password");
        assertEquals(ProcessSignInAction.INPUT, action.execute());
    }

    public void testValid() throws Exception {
        action.setEmailAddress(MockUserRepository.VALID_EMAIL_ADDRESS);
        action.setPassword(MockUserRepository.VALID_PASSWORD);
        assertEquals(ProcessSignInAction.SUCCESS, action.execute());
    }

}
