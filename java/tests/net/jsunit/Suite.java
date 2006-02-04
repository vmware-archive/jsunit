package net.jsunit;

import junit.framework.TestCase;
import junit.framework.TestSuite;
import net.jsunit.action.DisposeActionTest;
import net.jsunit.action.ResultAcceptorActionTest;
import net.jsunit.action.ResultDisplayerActionTest;
import net.jsunit.action.TestRunnerActionTest;
import net.jsunit.configuration.*;
import net.jsunit.interceptor.BrowserResultInterceptorTest;
import net.jsunit.interceptor.BrowserTestRunnerInterceptorTest;
import net.jsunit.model.BrowserResultTest;
import net.jsunit.model.FailedToLaunchBrowserResultTest;
import net.jsunit.model.TestCaseResultTest;
import net.jsunit.model.TestPageResultTest;
import net.jsunit.model.TimedOutBrowerResultTest;

/**
 * @author Edward Hieatt, edward@jsunit.net
 */

public class Suite extends TestCase {
	
    public static TestSuite suite() {
        TestSuite result = new TestSuite();
        result.addTestSuite(AcceptorFunctionalTest.class);
        result.addTestSuite(ArgumentsConfigurationTest.class);
        result.addTestSuite(BrowserResultInterceptorTest.class);
        result.addTestSuite(BrowserResultLogWriterTest.class);
        result.addTestSuite(BrowserResultTest.class);
        result.addTestSuite(BrowserTestRunnerInterceptorTest.class);
        result.addTestSuite(ClientServerInteractionTest.class);
        result.addTestSuite(ConfigurationResolutionTest.class);
        result.addTestSuite(ConfigurationTest.class);
        result.addTestSuite(DisplayerFunctionalTest.class);
        result.addTestSuite(DisposeActionTest.class);
        result.addTestSuite(EnvironmentVariablesConfigurationTest.class);
        result.addTestSuite(FailedToLaunchBrowserResultTest.class);
        result.addTestSuite(ServerConfigurationFunctionalTest.class);
        result.addTestSuite(JsUnitServerTest.class);
        result.addTestSuite(PropertiesConfigurationTest.class);
        result.addTestSuite(RemoteTestRunClientTest.class);
        result.addTestSuite(ResultAcceptorActionTest.class);
        result.addTestSuite(ResultAcceptorTest.class);
        result.addTestSuite(ResultDisplayerActionTest.class);
        result.addTestSuite(RunnerFunctionalTest.class);
        result.addTestSuite(TestRunNotifierServerTest.class);
        result.addTestSuite(TestCaseResultTest.class);
        result.addTestSuite(TestPageResultTest.class);
        result.addTestSuite(TestRunManagerTest.class);
        result.addTestSuite(TestRunnerActionTest.class);
        result.addTestSuite(TimeoutCheckerTest.class);
        result.addTestSuite(TimedOutBrowerResultTest.class);
        return result;
    }
    
}