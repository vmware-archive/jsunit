package net.jsunit.model;

import junit.framework.TestCase;

public class UserTest extends TestCase {
    private User user;

    protected void setUp() throws Exception {
        super.setUp();
        user = new User();
    }

    public void testSimple() throws Exception {
        fillOutAllFields();

        assertEquals("Tom", user.getFirstName());
        assertEquals("Cruise", user.getLastName());
        assertEquals("tom@example.com", user.getEmailAddress());
        assertEquals("katie", user.getPassword());
    }

    private void fillOutAllFields() {
        user.setFirstName("Tom");
        user.setLastName("Cruise");
        user.setEmailAddress("tom@example.com");
        user.setPassword("katie");
    }

    public void testIsValid() throws Exception {
        assertFalse(user.isValid());
        fillOutAllFields();
        assertTrue(user.isValid());
    }

}
