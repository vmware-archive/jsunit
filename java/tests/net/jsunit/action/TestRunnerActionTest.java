package net.jsunit.action;

import junit.framework.TestCase;
import net.jsunit.MockBrowserTestRunner;
import net.jsunit.Utility;

public class TestRunnerActionTest extends TestCase {

    private TestRunnerAction action;
	private MockBrowserTestRunner mockRunner;

    public void setUp() throws Exception {
        super.setUp();
        action = new TestRunnerAction();
        mockRunner = new MockBrowserTestRunner();
        mockRunner.hasReceivedResult = true;
        action.setBrowserTestRunner(mockRunner);
    }

    public void testSuccess() throws Exception {
    	mockRunner.shouldSucceed = true;
        assertEquals(TestRunnerAction.SUCCESS, action.execute());
        assertEquals("<result>success</result>", Utility.asString(action.getXmlRenderable().asXml()));
    }

    public void testFailure() throws Exception {
    	mockRunner.shouldSucceed = false;
        assertEquals(TestRunnerAction.SUCCESS, action.execute());
        assertEquals("<result>failure</result>", Utility.asString(action.getXmlRenderable().asXml()));
    }

}