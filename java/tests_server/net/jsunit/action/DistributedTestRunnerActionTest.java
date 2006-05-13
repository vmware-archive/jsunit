package net.jsunit.action;

import junit.framework.TestCase;
import net.jsunit.DistributedTestRunManager;
import net.jsunit.JsUnitAggregateServer;
import net.jsunit.RemoteRunSpecification;
import net.jsunit.SuccessfulRemoteServerHitter;
import net.jsunit.configuration.Configuration;
import net.jsunit.configuration.DummyConfigurationSource;
import net.jsunit.model.Browser;
import net.jsunit.model.SecurityViolation;
import net.jsunit.results.Skin;
import net.jsunit.utility.XmlUtility;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;
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
        action.setRemoteRunSpecifications(someRemoteRunSpecs());
        assertEquals(DistributedTestRunnerAction.SUCCESS, action.execute());
        assertTrue(action.getTestRunManager().getDistributedTestRunResult().wasSuccessful());
        assertNull(action.getTestRunManager().getOverrideURL());
    }

    public void testSimpleWithSkin() throws Exception {
        action.setRemoteRunSpecifications(someRemoteRunSpecs());
        action.setSkin(new Skin(3, new File("aSkin.xsl")));
        assertEquals(DistributedTestRunnerAction.TRANSFORM, action.execute());
    }

    public void testOverrideURL() throws Exception {
        action.setRemoteRunSpecifications(someRemoteRunSpecs());
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
        action.setRemoteRunSpecifications(someRemoteRunSpecs());
        action.execute();
        DistributedTestRunManager testRunManager = action.getTestRunManager();
        assertEquals(2, testRunManager.getRemoteRunSpecs().size());
    }

    private List<RemoteRunSpecification> someRemoteRunSpecs() throws MalformedURLException {
        RemoteRunSpecification spec0 = new RemoteRunSpecification(new URL("http://www.example.com"));
        spec0.addBrowser(new Browser("browser0.exe", 0));
        spec0.addBrowser(new Browser("browser1.exe", 1));
        RemoteRunSpecification spec1 = new RemoteRunSpecification(new URL("http://www.example.net"));
        spec1.addBrowser(new Browser("browser0.exe", 0));
        return Arrays.asList(new RemoteRunSpecification[] {spec0, spec1});
    }

    public void testInvalidURLId() throws Exception {
        action.setInvalidRemoteMachineUrlBrowserCombination(new InvalidRemoteMachineUrlBrowserCombination("bad URL ID", "bad browser ID"));
        assertEquals(
                "<error>Invalid Remote Machine ID/Browser ID: bad URL ID, bad browser ID</error>",
                XmlUtility.asString(action.getXmlRenderable().asXml())
        );
    }

    public void testSkin() throws Exception {
        assertNull(action.getSkin());
        Skin skinFile = new Skin(2, new File("foo.xsl"));
        action.setSkin(skinFile);
        action.setRemoteRunSpecifications(Arrays.asList(new RemoteRunSpecification[] {}));
        assertEquals(skinFile, action.getSkin());
        assertEquals(ResultDisplayerAction.TRANSFORM, action.execute());
    }

}
