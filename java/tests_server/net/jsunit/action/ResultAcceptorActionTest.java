package net.jsunit.action;

import junit.framework.TestCase;
import net.jsunit.model.DummyBrowserResult;
import net.jsunit.MockBrowserTestRunner;
import net.jsunit.model.Browser;

public class ResultAcceptorActionTest extends TestCase {

    public void testSimple() throws Exception {
        ResultAcceptorAction action = new ResultAcceptorAction();
        DummyBrowserResult dummyResult = new DummyBrowserResult(false, 1, 2);
        dummyResult.setBrowser(new Browser("browser.exe", 4));
        action.setBrowserResult(dummyResult);
        MockBrowserTestRunner mockRunner = new MockBrowserTestRunner();
        action.setBrowserTestRunner(mockRunner);
        assertEquals(ResultAcceptorAction.SUCCESS, action.execute());
        assertSame(dummyResult, mockRunner.acceptedResult);
    }
}
