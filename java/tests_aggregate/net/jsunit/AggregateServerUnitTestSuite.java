package net.jsunit;

import junit.framework.Test;
import junit.framework.TestSuite;
import net.jsunit.action.AggregateConfigurationActionTest;
import net.jsunit.action.DistributedTestRunnerActionTest;
import net.jsunit.interceptor.AggregateServerInterceptorTest;

public class AggregateServerUnitTestSuite {

    public static Test suite() {
        TestSuite result = new TestSuite("Aggregate server unit tests");
        result.addTestSuite(AggregateConfigurationActionTest.class);
        result.addTestSuite(DistributedTestRunnerActionTest.class);
        result.addTestSuite(AggregateServerInterceptorTest.class);
        result.addTestSuite(JsUnitAggregateServerTest.class);
        return result;
    }
}
