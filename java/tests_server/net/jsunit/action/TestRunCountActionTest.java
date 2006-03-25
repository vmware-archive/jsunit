package net.jsunit.action;

import junit.framework.TestCase;
import net.jsunit.JsUnitServerStub;

public class TestRunCountActionTest extends TestCase {

    public void testSimple() throws Exception {
        TestRunCountAction action = new TestRunCountAction();
        MockServer mockServer = new MockServer();
        action.setJsUnitServer(mockServer);
        assertEquals(7, action.getTestRunCount());
    }

    static class MockServer extends JsUnitServerStub {

        public long getTestRunCount() {
            return 7;
        }
    }
}        
