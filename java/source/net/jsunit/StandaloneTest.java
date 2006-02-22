package net.jsunit;

import junit.framework.TestCase;
import net.jsunit.configuration.Configuration;
import net.jsunit.configuration.ConfigurationSource;
import net.jsunit.utility.XmlUtility;

public class StandaloneTest extends TestCase {

    protected JsUnitServer server;
    private TestRunManager testRunManager;

    public StandaloneTest(String name) {
        super(name);
    }

    public void setUp() throws Exception {
        super.setUp();
        server = new JsUnitServer(new Configuration(configurationSource()));
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

    public void testStandaloneRun() throws Exception {
        testRunManager.runTests();
        if (testRunManager.hadProblems()) {
            fail(XmlUtility.asPrettyString(testRunManager.getTestRunResult().asXml()));
        }
    }

}