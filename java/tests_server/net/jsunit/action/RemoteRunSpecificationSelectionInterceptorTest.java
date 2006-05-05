package net.jsunit.action;

import com.opensymphony.webwork.ServletActionContext;
import com.opensymphony.xwork.Action;
import junit.framework.TestCase;
import net.jsunit.DummyHttpRequest;
import net.jsunit.RemoteRunSpecification;
import net.jsunit.model.Browser;
import net.jsunit.configuration.DummyConfigurationSource;
import net.jsunit.configuration.RemoteConfiguration;
import net.jsunit.interceptor.MockActionInvocation;
import net.jsunit.interceptor.RemoteRunSpecificationSelectionInterceptor;

import javax.servlet.http.HttpServletRequest;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RemoteRunSpecificationSelectionInterceptorTest extends TestCase {

    private RemoteRunSpecificationSelectionInterceptor interceptor;

    private MockAction action;
    private MockActionInvocation invocation;

    protected void setUp() throws Exception {
        super.setUp();
        interceptor = new RemoteRunSpecificationSelectionInterceptor();
        action = new MockAction();
        invocation = new MockActionInvocation(action);
    }

    public void testValidSelection() throws Exception {
        setUpRequestWithSpecSpecs("0_0", "0_1", "2_1");
        assertEquals(Action.SUCCESS, interceptor.intercept(invocation));
        assertTrue(invocation.wasInvokeCalled);
        assertNull(action.invalidCombo);
        assertEquals(2, action.specs.size());
        RemoteRunSpecification spec0 = action.specs.get(0);
        assertEquals("http://www.example.com", spec0.getRemoteMachineBaseURL().toString());
        List<Browser> spec0remoteBrowsers = spec0.getRemoteBrowsers();
        assertEquals(2, spec0remoteBrowsers.size());
        assertEquals(new Browser("browser0.exe", 0), spec0remoteBrowsers.get(0));
        assertEquals(new Browser("browser1.exe", 1), spec0remoteBrowsers.get(1));

        RemoteRunSpecification spec1 = action.specs.get(1);
        assertEquals("http://www.example.org", spec1.getRemoteMachineBaseURL().toString());
        List<Browser> spec1remoteBrowsers = spec1.getRemoteBrowsers();
        assertEquals(1, spec1remoteBrowsers.size());
        assertEquals(new Browser("browser1.exe", 1), spec1remoteBrowsers.get(0));
    }

    public void testInvalidBrowserId() throws Exception {
        setUpRequestWithSpecSpecs("0_0", "0_8");
        assertEquals(Action.ERROR, interceptor.intercept(invocation));
        assertFalse(invocation.wasInvokeCalled);
        assertEquals("0, 8", action.invalidCombo.getDisplayString());
        assertNull(action.specs);
    }

    public void testInvalidMachineId() throws Exception {
        setUpRequestWithSpecSpecs("0_0", "96_1");
        assertEquals(Action.ERROR, interceptor.intercept(invocation));
        assertFalse(invocation.wasInvokeCalled);
        assertEquals("96, 1", action.invalidCombo.getDisplayString());
        assertNull(action.specs);
    }

    public void testNonIntegerBrowserId() throws Exception {
        setUpRequestWithSpecSpecs("1_1", "1_foobar", "0_1");
        assertEquals(Action.ERROR, interceptor.intercept(invocation));
        assertFalse(invocation.wasInvokeCalled);
        assertEquals("1, foobar", action.invalidCombo.getDisplayString());
        assertNull(action.specs);
    }

    public void testNonIntegerMachineId() throws Exception {
        setUpRequestWithSpecSpecs("1_1", "foobar_0", "0_1");
        assertEquals(Action.ERROR, interceptor.intercept(invocation));
        assertFalse(invocation.wasInvokeCalled);
        assertEquals("foobar, 0", action.invalidCombo.getDisplayString());
        assertNull(action.specs);
    }

    public void testMultipleInvalidSpecs() throws Exception {
        setUpRequestWithSpecSpecs("1_1", "foobar_0", "barfoo_1");
        assertEquals(Action.ERROR, interceptor.intercept(invocation));
        assertFalse(invocation.wasInvokeCalled);
        assertEquals("foobar, 0", action.invalidCombo.getDisplayString());
        assertNull(action.specs);
    }

    public void testNoSelection() throws Exception {
        setUpRequestWithSpecSpecs();
        assertEquals(Action.SUCCESS, interceptor.intercept(invocation));
        assertTrue(invocation.wasInvokeCalled);
        assertNull(action.invalidCombo);
        assertEquals(3, action.specs.size());
        for (RemoteRunSpecification specification : action.specs)
            assertTrue(specification.isForAllBrowsers());
    }

    private void setUpRequestWithSpecSpecs(String... urlIds) {
        Map<String, String[]> requestMap = new HashMap<String, String[]>();
        HttpServletRequest request = new DummyHttpRequest(requestMap);
        String[] values = new String[urlIds.length];
        System.arraycopy(urlIds, 0, values, 0, urlIds.length);
        requestMap.put("urlId_browserId", values);
        ServletActionContext.setRequest(request);
    }

    static class MockAction implements Action, RemoteRunSpecificationAware {
        private List<RemoteRunSpecification> specs;
        private InvalidRemoteMachineUrlBrowserCombination invalidCombo;
        private List<RemoteConfiguration> remoteConfigs;

        public MockAction() throws MalformedURLException {
            DummyConfigurationSource source = new DummyConfigurationSource() {
                public String browserFileNames() {
                    return "browser0.exe,browser1.exe";
                }
            };
            RemoteConfiguration configuration0 = new RemoteConfiguration(new URL("http://www.example.com"), source);
            RemoteConfiguration configuration1 = new RemoteConfiguration(new URL("http://www.example.net"), source);
            RemoteConfiguration configuration2 = new RemoteConfiguration(new URL("http://www.example.org"), source);
            remoteConfigs = Arrays.asList(configuration0, configuration1, configuration2);
        }

        public String execute() throws Exception {
            return SUCCESS;
        }

        public void setRemoteRunSpecifications(List<RemoteRunSpecification> specs) {
            this.specs = specs;
        }

        public void setInvalidRemoteMachineUrlBrowserCombination(InvalidRemoteMachineUrlBrowserCombination combination) {
            invalidCombo = combination;
        }

        public RemoteConfiguration getRemoteMachineConfigurationById(int id) {
            return remoteConfigs.get(id);
        }

        public List<RemoteConfiguration> getAllRemoteMachineConfigurations() {
            return remoteConfigs;
        }

    }

}
