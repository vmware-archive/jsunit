package net.jsunit.action;

import junit.framework.TestCase;
import net.jsunit.JsUnitServerStub;
import net.jsunit.StatusMessage;

import java.util.ArrayList;
import java.util.List;

public class ServerStatusActionTest extends TestCase {

    public void testSimple() throws Exception {
        ServerStatusAction action = new ServerStatusAction();
        MockServer mockServer = new MockServer();
        action.setJsUnitServer(mockServer);
        assertTrue(action.getStatusMessages().isEmpty());
        mockServer.messages.add(new StatusMessage("foo bar"));
        assertEquals(1, action.getStatusMessages().size());
    }

    static class MockServer extends JsUnitServerStub {
        public List<StatusMessage> messages = new ArrayList<StatusMessage>();

        public List<StatusMessage> getStatusMessages() {
            return messages;
        }
    }
}
