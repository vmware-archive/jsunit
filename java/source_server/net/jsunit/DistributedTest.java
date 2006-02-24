package net.jsunit;

import junit.framework.TestCase;
import net.jsunit.configuration.Configuration;
import net.jsunit.configuration.ConfigurationSource;
import net.jsunit.logging.NoOpStatusLogger;
import net.jsunit.model.FarmTestRunResult;
import net.jsunit.utility.XmlUtility;

public class DistributedTest extends TestCase {

    protected DistributedTestRunManager manager;
    private JsUnitServer server;

    public DistributedTest(String name) {
        super(name);
    }

    public void setUp() throws Exception {
        super.setUp();
        server = new JsUnitServer(new Configuration(configurationSource()));
        server.start();
        manager = createTestRunManager();
    }

    public void tearDown() throws Exception {
        if (server != null)
            server.dispose();
        super.tearDown();
    }

    public void testCollectResults() {
        manager.runTests();
        FarmTestRunResult result = manager.getFarmTestRunResult();
        if (!result.wasSuccessful())
            fail(XmlUtility.asPrettyString(result.asXml()));
    }

    protected DistributedTestRunManager createTestRunManager() {
        return new DistributedTestRunManager(new NoOpStatusLogger(), new Configuration(farmConfigurationSource()));
    }

    protected ConfigurationSource farmConfigurationSource() {
        return Configuration.resolveSource();
    }

    protected ConfigurationSource configurationSource() {
        return Configuration.resolveSource();
    }

}
