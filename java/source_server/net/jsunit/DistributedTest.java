package net.jsunit;

import junit.framework.TestCase;
import net.jsunit.configuration.Configuration;
import net.jsunit.configuration.ConfigurationSource;
import net.jsunit.logging.NoOpStatusLogger;
import net.jsunit.model.TestRunResult;
import net.jsunit.utility.XmlUtility;

public class DistributedTest extends TestCase {

    protected DistributedTestRunManager manager;

    public DistributedTest(String name) {
        super(name);
    }

    protected ConfigurationSource configurationSource() {
        return Configuration.resolveSource();
    }

    public void testCollectResults() {
        manager = createTestRunManager();
        manager.runTests();
        TestRunResult result = manager.getTestRunResult();
        if (!result.wasSuccessful())
            fail(XmlUtility.asPrettyString(result.asXml()));
    }

    protected DistributedTestRunManager createTestRunManager() {
        return new DistributedTestRunManager(new NoOpStatusLogger(), new Configuration(configurationSource()));
    }
}
