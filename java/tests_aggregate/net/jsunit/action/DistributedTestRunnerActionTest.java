package net.jsunit.action;

import junit.framework.TestCase;
import net.jsunit.DistributedTestRunManager;
import net.jsunit.JsUnitAggregateServer;
import net.jsunit.RemoteRunSpecification;
import net.jsunit.SuccessfulRemoteServerHitter;
import net.jsunit.configuration.AggregateConfiguration;
import net.jsunit.configuration.DummyConfigurationSource;
import net.jsunit.model.Browser;
import net.jsunit.model.DistributedTestRunResult;
import net.jsunit.utility.XmlUtility;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;
import java.util.List;

public class DistributedTestRunnerActionTest extends TestCase {

    private DistributedTestRunnerAction action;

    public void setUp() throws Exception {
        super.setUp();
        action = new DistributedTestRunnerAction();
        action.setAggregateServer(new JsUnitAggregateServer(new AggregateConfiguration(new DummyConfigurationSource())));
        action.setRemoteServerHitter(new SuccessfulRemoteServerHitter());
    }

    public void testSimple() throws Exception {
        action.setRemoteRunSpecifications(someRemoteRunSpecs());
        assertEquals(DistributedTestRunnerAction.SUCCESS, action.execute());
        DistributedTestRunResult distributedTestRunResult = action.getTestRunManager().getDistributedTestRunResult();
        assertTrue(distributedTestRunResult.wasSuccessful());
    }

    public void testOverrideURL() throws Exception {
        action.setRemoteRunSpecifications(someRemoteRunSpecs());
        String overrideURL = "  http://overrideurl.com:1234?foo=bar&bar=fo   ";
        action.setUrl(overrideURL);
        assertEquals(DistributedTestRunnerAction.SUCCESS, action.execute());
        assertEquals(overrideURL.trim(), action.getTestRunManager().getOverrideURL());
    }

    public void testLimitToSpecificRemoteMachinesAndBrowsers() throws Exception {
        action.setRemoteRunSpecifications(someRemoteRunSpecs());
        action.execute();
        DistributedTestRunManager testRunManager = action.getTestRunManager();
        assertEquals(2, testRunManager.getRemoteRunSpecs().size());
    }

    public void testErrorMessage() throws Exception {
        action.setErrorMessage("foo bar baz");
        assertEquals(
                "<error>foo bar baz</error>",
                XmlUtility.asString(action.getXmlRenderable().asXml())
        );
    }

    private List<RemoteRunSpecification> someRemoteRunSpecs() throws MalformedURLException {
        RemoteRunSpecification spec0 = new RemoteRunSpecification(new URL("http://www.example.com"));
        spec0.addBrowser(new Browser("browser0.exe", 0));
        RemoteRunSpecification spec1 = new RemoteRunSpecification(new URL("http://www.example.net"));
        spec1.addBrowser(new Browser("browser0.exe", 0));
        return Arrays.asList(new RemoteRunSpecification[]{spec0, spec1});
    }

}
