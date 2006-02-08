package net.jsunit.action;

import junit.framework.TestCase;
import net.jsunit.BrowserTestRunner;
import net.jsunit.Utility;
import net.jsunit.model.BrowserResult;
import org.jdom.Element;

import java.util.List;
import java.util.Arrays;

public class TestRunnerActionTest extends TestCase {

    private TestRunnerAction action;

    public void setUp() throws Exception {
        super.setUp();
        action = new TestRunnerAction();
    }

    public void testSuccess() throws Exception {
        action.setBrowserTestRunner(new MockBrowserTestRunner(true));
        assertEquals(TestRunnerAction.SUCCESS, action.execute());
        assertEquals("<result>success</result>", Utility.asString(action.getXmlRenderable().asXml()));
    }

    public void testFailure() throws Exception {
        action.setBrowserTestRunner(new MockBrowserTestRunner(false));
        assertEquals(TestRunnerAction.SUCCESS, action.execute());
        assertEquals("<result>failure</result>", Utility.asString(action.getXmlRenderable().asXml()));
    }

    static class MockBrowserTestRunner implements BrowserTestRunner {
        private boolean shouldSucceed;

        public MockBrowserTestRunner(boolean shouldSucceed) {
            this.shouldSucceed = shouldSucceed;
        }

        public void startTestRun() {
        }

        public void finishTestRun() {
        }

        public long launchTestRunForBrowserWithFileName(String browserFileName) {
            return 0;
        }

        public void accept(BrowserResult result) {
        }

        public boolean hasReceivedResultSince(long launchTime) {
            return true;
        }

        public BrowserResult lastResult() {
            return new BrowserResult() {
                public int failureCount() {
                    return shouldSucceed ? 0 : 1;
                }
            };
        }

        public void dispose() {
        }

        public BrowserResult findResultWithId(String id) {
            return null;
        }

        public void logStatus(String message) {
        }

        public List<String> getBrowserFileNames() {
            return Arrays.asList(new String[] {"mybrowser.exe"});
        }

        public int timeoutSeconds() {
            return 0;
        }

        public Element asXml() {
            return null;
        }
    }
}