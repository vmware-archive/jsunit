package net.jsunit.action;

import junit.framework.TestCase;
import net.jsunit.DistributedTestRunManager;
import net.jsunit.JsUnitAggregateServer;
import net.jsunit.MultipleMachineBrowserDistributedTestRunManager;
import net.jsunit.SuccessfulRemoteServerHitter;
import net.jsunit.captcha.SecurityViolation;
import net.jsunit.configuration.Configuration;
import net.jsunit.configuration.DummyConfigurationSource;
import net.jsunit.results.Skin;
import net.jsunit.utility.XmlUtility;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class DistributedTestRunnerActionTest extends TestCase {

    private DistributedTestRunnerAction action;

    public void setUp() throws Exception {
        super.setUp();
        action = new DistributedTestRunnerAction();
        action.setAggregateServer(new JsUnitAggregateServer(new Configuration(new DummyConfigurationSource())));
        action.setRemoteServerHitter(new SuccessfulRemoteServerHitter());
    }

    public void testSimple() throws Exception {
        action.setSelectedRemoteMachineURLs(urls("http://www.example.com"));
        assertEquals(DistributedTestRunnerAction.SUCCESS, action.execute());
        assertTrue(action.getTestRunManager().getDistributedTestRunResult().wasSuccessful());
        assertNull(action.getTestRunManager().getOverrideURL());
    }

    public void testSimpleWithSkin() throws Exception {
        action.setSelectedRemoteMachineURLs(urls("http://www.example.com"));        
        action.setSkin(new Skin(3, new File("aSkin.xsl")));
        assertEquals(DistributedTestRunnerAction.TRANSFORM, action.execute());
    }

    public void testOverrideURL() throws Exception {
        action.setSelectedRemoteMachineURLs(urls("http://www.example.com"));
        String overrideURL = "http://overrideurl.com:1234?foo=bar&bar=fo";
        action.setUrl(overrideURL);
        assertEquals(DistributedTestRunnerAction.SUCCESS, action.execute());
        assertEquals(overrideURL, action.getTestRunManager().getOverrideURL());
    }

    public void testSecurityFailure() throws Exception {
        action.setSecurityViolation(SecurityViolation.OUTDATED_CAPTCHA);
        assertEquals(
                XmlUtility.asString(SecurityViolation.OUTDATED_CAPTCHA.asXml()),
                XmlUtility.asString(action.getXmlRenderable().asXml())
        );
    }

    public void testLimitToSpecificRemoteMachinesAndBrowsers() throws Exception {
        List<URL> urls = urls("http://www.example.com", "http://www.example.net", "http://www.example.org");
        action.setSelectedRemoteMachineURLs(urls);
        action.execute();
        DistributedTestRunManager testRunManager = action.getTestRunManager();
        assertTrue(testRunManager instanceof MultipleMachineBrowserDistributedTestRunManager);
        assertEquals(3, testRunManager.remoteMachineURLs().size());
    }

    private List<URL> urls(String... urls) throws MalformedURLException {
        List<URL> result = new ArrayList<URL>();
        for (String url : urls) {
            result.add(new URL(url));
        }
        return result;
    }

    public void testInvalidURLId() throws Exception {
        action.setInvalidRemoteMachineURLId("foobar");
        assertEquals(
                "<error>Invalid URL ID: foobar</error>",
                XmlUtility.asString(action.getXmlRenderable().asXml())
        );
    }

}
