package net.jsunit;

import junit.framework.TestCase;

public class StatusMessageTest extends TestCase {

    public void testSimple() throws Exception {
        StatusMessage message = new StatusMessage("a message");
        assertEquals("a message", message.getMessage());
        assertNotNull(message.getDate());
    }
}
