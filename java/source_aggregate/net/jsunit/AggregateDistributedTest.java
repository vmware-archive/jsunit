package net.jsunit;

import junit.extensions.ActiveTestSuite;
import junit.framework.Test;
import junit.framework.TestSuite;
import net.jsunit.configuration.CompositeConfigurationSource;

public class AggregateDistributedTest {

    public static Test suite() {
        TestSuite suite = new ActiveTestSuite();
        new DistributedTestSuiteBuilder(CompositeConfigurationSource.resolve()).addTestsTo(suite);
        return suite;
    }

}
