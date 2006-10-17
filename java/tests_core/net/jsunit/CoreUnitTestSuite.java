package net.jsunit;

import junit.framework.Test;
import junit.framework.TestSuite;
import net.jsunit.configuration.ConfigurationTest;
import net.jsunit.configuration.RemoteConfigurationTest;
import net.jsunit.model.*;
import net.jsunit.utility.JsUnitURLTest;
import net.jsunit.utility.StringUtilityTest;

public class CoreUnitTestSuite {

    public static Test suite() {
        TestSuite result = new TestSuite("Core unit tests");
        result.addTestSuite(AbstractResultTest.class);
        result.addTestSuite(ClientServerConnectionTest.class);
        result.addTestSuite(ConfigurationTest.class);
        result.addTestSuite(RemoteConfigurationTest.class);
        result.addTestSuite(RemoteTestRunClientTest.class);
        result.addTestSuite(BrowserResultBuilderTest.class);
        result.addTestSuite(BrowserResultTest.class);
        result.addTestSuite(BrowserTest.class);
        result.addTestSuite(DistributedTestRunResultBuilderTest.class);
        result.addTestSuite(DistributedTestRunResultTest.class);
        result.addTestSuite(FailedToLaunchBrowserResultTest.class);
        result.addTestSuite(ResultBuilderTest.class);
        result.addTestSuite(SecurityViolationTest.class);
        result.addTestSuite(TestCaseResultTest.class);
        result.addTestSuite(TestPageResultTest.class);
        result.addTestSuite(TestRunResultBuilderTest.class);
        result.addTestSuite(TestRunResultTest.class);
        result.addTestSuite(TimedOutBrowerResultTest.class);

        result.addTestSuite(JsUnitURLTest.class);
        result.addTestSuite(StringUtilityTest.class);
        return result;
    }

}
