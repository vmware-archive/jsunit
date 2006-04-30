package net.jsunit.action;

import junit.framework.TestCase;
import net.jsunit.JsUnitAggregateServer;
import net.jsunit.SuccessfulRemoteServerHitter;
import net.jsunit.configuration.Configuration;
import net.jsunit.configuration.DummyConfigurationSource;
import net.jsunit.results.Skin;

import java.io.File;

public class DistributedTestRunnerActionTest extends TestCase {

    private DistributedTestRunnerAction action;

    public void setUp() throws Exception {
        super.setUp();
        action = new DistributedTestRunnerAction();
        action.setAggregateServer(new JsUnitAggregateServer(new Configuration(new DummyConfigurationSource())));
        action.setRemoteServerHitter(new SuccessfulRemoteServerHitter());
    }

    public void testSimple() throws Exception {
        assertEquals(DistributedTestRunnerAction.SUCCESS, action.execute());
        assertTrue(action.getTestRunManager().getDistributedTestRunResult().wasSuccessful());
        assertNull(action.getTestRunManager().getOverrideURL());
    }

    public void testSimpleWithSkin() throws Exception {
        action.setSkin(new Skin(3, new File("aSkin.xsl")));
        assertEquals(DistributedTestRunnerAction.TRANSFORM, action.execute());
    }

    public void testOverrideURL() throws Exception {
        String overrideURL = "http://overrideurl.com:1234?foo=bar&bar=fo";
        action.setUrl(overrideURL);
        assertEquals(DistributedTestRunnerAction.SUCCESS, action.execute());
        assertEquals(overrideURL, action.getTestRunManager().getOverrideURL());
    }

    public void testIsIpAddressTrusted() throws Exception {
        action.setAggregateServer(new JsUnitAggregateServer(new Configuration(new DummyConfigurationSource() {
            public String trustedIpAddresses() {
                return "123.456.786,987.654.32.10";
            }
        })));
        action.setRequestIPAddress("111.22.33.44");
        assertFalse(action.isIpAddressesTrusted());
        action.setRequestIPAddress("123.456.786");
        assertTrue(action.isIpAddressesTrusted());
    }

}
