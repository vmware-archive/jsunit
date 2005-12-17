package net.jsunit;

import junit.framework.TestCase;
import junit.framework.TestSuite;
import net.jsunit.configuration.ArgumentsConfigurationTest;
import net.jsunit.configuration.ConfigurationTest;
import net.jsunit.configuration.EnvironmentVariablesConfigurationTest;
import net.jsunit.configuration.PropertiesConfigurationTest;

/**
 * @author Edward Hieatt, edward@jsunit.net
 */

public class Suite extends TestCase {
    public static TestSuite suite() {
        TestSuite result = new TestSuite();
        result.addTestSuite(TestCaseResultTest.class);
        result.addTestSuite(TestSuiteResultTest.class);
        result.addTestSuite(ResultAcceptorTest.class);
        result.addTestSuite(ConfigurationTest.class);
        result.addTestSuite(ArgumentsConfigurationTest.class);
        result.addTestSuite(PropertiesConfigurationTest.class);
        result.addTestSuite(EnvironmentVariablesConfigurationTest.class);
        return result;
    }
}