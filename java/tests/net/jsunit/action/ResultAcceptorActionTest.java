package net.jsunit.action;

import java.util.Date;
import java.util.List;

import net.jsunit.BrowserTestRunner;
import net.jsunit.DummyBrowserResult;
import net.jsunit.FailedToLaunchBrowserException;
import net.jsunit.model.BrowserResult;
import junit.framework.TestCase;
import org.jdom.Element;

public class ResultAcceptorActionTest extends TestCase {

    public void testSimple() throws Exception {
        ResultAcceptorAction action = new ResultAcceptorAction();
        DummyBrowserResult dummyResult = new DummyBrowserResult(false, 1, 2);
        action.setBrowserResult(dummyResult);
        MockBrowserTestRunner mockRunner = new MockBrowserTestRunner();
        action.setBrowserTestRunner(mockRunner);
        assertEquals(ResultAcceptorAction.SUCCESS, action.execute());
        assertSame(dummyResult, mockRunner.acceptedResult);
    }

    static class MockBrowserTestRunner implements BrowserTestRunner {

        public BrowserResult acceptedResult;

        public List<String> getBrowserFileNames() {
            return null;
        }

        public void launchTestRunForBrowserWithFileName(String browserFileName) throws FailedToLaunchBrowserException {
        }

        public boolean hasReceivedResultSince(Date dateBrowserLaunched) {
            return false;
        }

        public BrowserResult lastResult() {
            return null;
        }

        public void accept(BrowserResult result) {
            this.acceptedResult = result;
        }

        public void dispose() {
        }

        public BrowserResult findResultWithId(String id) {
            return null;
        }

        public Element asXml() {
            return null;
        }
    }

}
