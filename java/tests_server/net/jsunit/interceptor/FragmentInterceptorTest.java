package net.jsunit.interceptor;

import com.opensymphony.webwork.ServletActionContext;
import com.opensymphony.xwork.Action;
import junit.framework.TestCase;
import net.jsunit.BrowserTestRunner;
import net.jsunit.DummyConfigurationSource;
import net.jsunit.DummyHttpRequest;
import net.jsunit.MockBrowserTestRunner;
import net.jsunit.action.TestPageURLAware;
import net.jsunit.configuration.Configuration;
import net.jsunit.utility.SystemUtility;

import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

public class FragmentInterceptorTest extends TestCase {

    private MockAction action;
    private FragmentInterceptor interceptor;

    protected void setUp() throws Exception {
        super.setUp();
        action = new MockAction();
        interceptor = new FragmentInterceptor();
    }

    public void testSimple() throws Exception {
        String fragment = "assertTrue(true)";
        Map<String, String[]> map = new HashMap<String, String[]>();
        map.put("fragment", new String[]{fragment});
        DummyHttpRequest request = new DummyHttpRequest(map);
        ServletActionContext.setRequest(request);
        MockActionInvocation invocation = new MockActionInvocation(action);
        interceptor.intercept(invocation);
        assertTrue(invocation.wasInvokeCalled);

        String equalsSignEncoded = URLEncoder.encode("=", "UTF-8");
        int idIndex = action.url.lastIndexOf(equalsSignEncoded);
        long id = Long.parseLong(action.url.substring(idIndex + equalsSignEncoded.length()));
        assertEquals(
                "http://" + SystemUtility.hostname() + ":" + DummyConfigurationSource.PORT + "/jsunit/testRunner.html?testPage=" +
                        URLEncoder.encode("http://" + SystemUtility.hostname() + ":" + DummyConfigurationSource.PORT + "/jsunit/uploaded/generated_" + id + ".html&resultId=" + id, "UTF-8"),
                action.url
        );
    }

    static class MockAction implements Action, TestPageURLAware {
        private String url;

        public String execute() throws Exception {
            return null;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public void setBrowserTestRunner(BrowserTestRunner runner) {
        }

        public BrowserTestRunner getBrowserTestRunner() {
            MockBrowserTestRunner mockBrowserTestRunner = new MockBrowserTestRunner();
            mockBrowserTestRunner.configuration = new Configuration(new DummyConfigurationSource());
            return mockBrowserTestRunner;
        }
    }

}
