package net.jsunit;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import net.jsunit.configuration.CompositeConfigurationSource;
import net.jsunit.configuration.ConfigurationSource;
import net.jsunit.configuration.DelegatingConfigurationSource;
import net.jsunit.configuration.ServerConfiguration;
import net.jsunit.model.Browser;
import net.jsunit.model.TestRunResult;

public class StandaloneTest extends TestCase {

    protected JsUnitServer server;
    private TestRunManager testRunManager;
    private ConfigurationSource configurationSource;
    private String overrideURL;

    public StandaloneTest(String name) {
        super(name);
        this.configurationSource = configurationSource();
    }

    public StandaloneTest(ConfigurationSource source) {
        super(source.browserFileNames());
        this.configurationSource = source;
    }

    public static Test suite() {
        TestSuite suite = new TestSuite();
        ConfigurationSource originalSource = CompositeConfigurationSource.resolve();
        ServerConfiguration configuration = new ServerConfiguration(originalSource);
        for (final Browser browser : configuration.getBrowsers())
            suite.addTest(new StandaloneTest(new DelegatingConfigurationSource(originalSource) {
                public String browserFileNames() {
                    return browser.getDisplayName();
                }
            }));
        return suite;
    }

    public void setUp() throws Exception {
        super.setUp();
        server = new JsUnitServer(new ServerConfiguration(configurationSource));
        server.start();
        testRunManager = createTestRunManager();
    }

    protected ConfigurationSource configurationSource() {
        return CompositeConfigurationSource.resolve();
    }

    protected TestRunManager createTestRunManager() {
        return new TestRunManager(server, overrideURL);
    }

    public void tearDown() throws Exception {
        if (server != null)
            server.dispose();
        super.tearDown();
    }

    public void runTest() throws Exception {
        testStandaloneRun();
    }

    public void testStandaloneRun() throws Exception {
        testRunManager.runTests();
        TestRunResult result = testRunManager.getTestRunResult();
        if (!result.wasSuccessful()) {
            StringBuffer buffer = new StringBuffer();
            result.addErrorStringTo(buffer);
            System.err.println(buffer.toString());
            fail(result.displayString());
        }
    }

    public JsUnitServer getServer() {
        return server;
    }

    public void setOverrideURL(String url) {
        this.overrideURL = url;
    }
}