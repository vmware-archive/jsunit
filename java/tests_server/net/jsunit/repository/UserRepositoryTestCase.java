package net.jsunit.repository;

import junit.framework.TestCase;
import net.jsunit.model.User;

public abstract class UserRepositoryTestCase extends TestCase {
    private UserRepository userRepository;

    protected abstract UserRepository repository();

    protected void setUp() throws Exception {
        super.setUp();
        userRepository = repository();
    }

    public void testSaveAndFind() throws Exception {
        User val = val();
        User tom = tom();
        userRepository.save(val);
        userRepository.save(tom);

        assertEquals(val, userRepository.find("val@example.com", "password123"));
        assertNull(userRepository.find("val@example.com", "password321"));
        assertNull(userRepository.find("val2@example.com", "password123"));
    }

    private User val() {
        User user = new User();
        user.setEmailAddress("val@example.com");
        user.setPassword("password123");
        user.setFirstName("Val");
        user.setLastName("Kilmer");
        return user;
    }

    private User tom() {
        User user = new User();
        user.setEmailAddress("tom@example.com");
        user.setPassword("password123");
        user.setFirstName("Tom");
        user.setLastName("Cruise");
        return user;
    }

}
