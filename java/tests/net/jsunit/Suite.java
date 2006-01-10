package net.jsunit;

import junit.framework.TestCase;
import junit.framework.TestSuite;
import net.jsunit.configuration.ArgumentsConfigurationTest;
import net.jsunit.configuration.ConfigurationResolutionTest;
import net.jsunit.configuration.EnvironmentVariablesConfigurationTest;
import net.jsunit.configuration.PropertiesConfigurationTest;
import net.jsunit.model.BrowserResultTest;
import net.jsunit.model.TestCaseResultTest;

/**
 * @author Edward Hieatt, edward@jsunit.net
 */

public class Suite extends TestCase {
	
    public static TestSuite suite() {
        TestSuite result = new TestSuite();
        result.addTestSuite(ResultAcceptorTest.class);
        result.addTestSuite(ArgumentsConfigurationTest.class);
        result.addTestSuite(BrowserResultTest.class);
        result.addTestSuite(ConfigurationResolutionTest.class);
        result.addTestSuite(EnvironmentVariablesConfigurationTest.class);
        result.addTestSuite(JsUnitServerTest.class);
        result.addTestSuite(PropertiesConfigurationTest.class);
        result.addTestSuite(TestCaseResultTest.class);
        result.addTestSuite(TestRunManagerTest.class);
        return result;
    }
    
}