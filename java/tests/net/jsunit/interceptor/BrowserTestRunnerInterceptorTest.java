package net.jsunit.interceptor;

import java.util.Date;
import java.util.List;

import junit.framework.TestCase;
import net.jsunit.BrowserTestRunner;
import net.jsunit.FailedToLaunchBrowserException;
import net.jsunit.XmlRenderable;
import net.jsunit.action.JsUnitAction;
import net.jsunit.model.BrowserResult;

import org.jdom.Element;

public class BrowserTestRunnerInterceptorTest extends TestCase {

    public void testSimple() throws Exception {
        MockJsUnitAction action = new MockJsUnitAction();
        final BrowserTestRunner mockRunner = new MockBrowserTestRunner();
        BrowserTestRunnerInterceptor.setBrowserTestRunnerSource(new BrowserTestRunnerSource() {
            public BrowserTestRunner getRunner() {
                return mockRunner;
            }

        });
        assertNull(action.getBrowserTestRunner());
        BrowserTestRunnerInterceptor interceptor = new BrowserTestRunnerInterceptor();

        MockActionInvocation mockInvocation = new MockActionInvocation(action);
        interceptor.intercept(mockInvocation);

        assertSame(mockRunner, action.getBrowserTestRunner());
        assertTrue(mockInvocation.wasInvokeCalled);
    }

    public void tearDown() throws Exception {
        BrowserTestRunnerInterceptor.setBrowserTestRunnerSource(new DefaultBrowserTestRunnerSource());
        super.tearDown();
    }

    static class MockBrowserTestRunner implements BrowserTestRunner {

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
        }

        public void dispose() {
        }

        public BrowserResult findResultWithId(String id) {
            return null;
        }

        public Element asXml() {
            return null;
        }

		public void startTestRun() {
		}

		public void finishTestRun() {
		}

		public void logStatus(String message) {
		}
    }

    static class MockJsUnitAction extends JsUnitAction {

        public String execute() throws Exception {
            return null;
        }

        public XmlRenderable getXmlRenderable() {
            return null;
        }

    }

}
