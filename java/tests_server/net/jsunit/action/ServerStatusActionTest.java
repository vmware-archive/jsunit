package net.jsunit.action;

import junit.framework.TestCase;
import net.jsunit.JsUnitServer;
import net.jsunit.configuration.Configuration;
import net.jsunit.configuration.ServerType;
import net.jsunit.results.Skin;
import org.jdom.Element;

import java.util.Date;
import java.util.List;

public class ServerStatusActionTest extends TestCase {

    public void testSimple() throws Exception {
        ServerStatusAction action = new ServerStatusAction();
        MockServer mockServer = new MockServer();
        action.setJsUnitServer(mockServer);
        assertNull(action.getStatus());
        mockServer.status = "foo bar";
        assertEquals("foo bar", action.getStatus());
    }

    static class MockServer implements JsUnitServer {
        public String status;

        public Configuration getConfiguration() {
            return null;
        }

        public ServerType serverType() {
            return null;
        }

        public boolean isFarmServer() {
            return false;
        }

        public Date getStartDate() {
            return null;
        }

        public long getTestRunCount() {
            return 0;
        }

        public List<Skin> getSkins() {
            return null;
        }

        public Element asXml() {
            return null;
        }

        public String getStatus() {
            return status;
        }
    }
}
