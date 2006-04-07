package net.jsunit.action;

import junit.framework.TestCase;
import net.jsunit.JsUnitServerStub;

public class AdminActionTest extends TestCase {

    public void testSimple() throws Exception {
        AdminAction action = new AdminAction();
        MockJsUnitServer mock = new MockJsUnitServer();
        action.setJsUnitServer(mock);
        assertSame(mock, action.getServer());
    }

    static class MockJsUnitServer extends JsUnitServerStub {

    }

}
