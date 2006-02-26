package net.jsunit;

import junit.framework.TestCase;
import junit.framework.Test;
import junit.framework.TestSuite;
import net.jsunit.configuration.Configuration;
import net.jsunit.configuration.ConfigurationSource;
import net.jsunit.configuration.DelegatingConfigurationSource;
import net.jsunit.utility.XmlUtility;
import net.jsunit.model.TestRunResult;

public class StandaloneTest extends TestCase {

    protected StandardJsUnitServer server;
    private TestRunManager testRunManager;
    private ConfigurationSource configurationSource;

    public StandaloneTest(String name) {
        super(name);
        this.configurationSource = configurationSource();
    }

    public StandaloneTest(ConfigurationSource source) {
        this(source.browserFileNames());
        this.configurationSource = source;
    }

    public static Test suite() {
        TestSuite suite = new TestSuite();
        ConfigurationSource originalSource = Configuration.resolveSource();
        Configuration configuration = new Configuration(originalSource);
        for (final String browserFileName : configuration.getBrowserFileNames())
            suite.addTest(new StandaloneTest(new DelegatingConfigurationSource(originalSource) {
                public String browserFileNames() {
                    return browserFileName;
                }
            }));
        return suite;
    }

    public void setUp() throws Exception {
        super.setUp();
        server = new StandardJsUnitServer(new Configuration(configurationSource));
        server.start();
        testRunManager = createTestRunManager();
    }

    protected ConfigurationSource configurationSource() {
        return Configuration.resolveSource();
    }

    protected TestRunManager createTestRunManager() {
        return new TestRunManager(server);
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
        TestRunResult testRunResult = testRunManager.getTestRunResult();
        if (!testRunResult.wasSuccessful()) {
            StringBuffer buffer = new StringBuffer();
            buffer.append(testRunResult.displayString());
            buffer.append("\n");
            String xml = XmlUtility.asPrettyString(testRunManager.getTestRunResult().asXml());
            buffer.append(xml);
            fail(buffer.toString());
        }
    }

}