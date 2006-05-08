package net.jsunit;

import junit.framework.Test;
import junit.framework.TestSuite;
import net.jsunit.action.*;
import net.jsunit.captcha.CaptchaGeneratorTest;
import net.jsunit.captcha.CaptchaSpecTest;
import net.jsunit.client.TestPageTest;
import net.jsunit.client.TestRunClientTest;
import net.jsunit.configuration.ConfigurationSourceResolutionTest;
import net.jsunit.configuration.ConfigurationTest;
import net.jsunit.configuration.EnvironmentVariablesConfigurationSourceTest;
import net.jsunit.configuration.PropertiesConfigurationSourceTest;
import net.jsunit.interceptor.*;
import net.jsunit.model.FailedToLaunchBrowserResultTest;
import net.jsunit.model.TestRunResultBuilderTest;
import net.jsunit.model.TimedOutBrowerResultTest;
import net.jsunit.uploaded.UploadedTestPageFactoryTest;

public class ImpureUnitTestSuite {

    public static Test suite() {
        TestSuite result = new TestSuite();
        result.addTestSuite(AggregateServerInterceptorTest.class);
        result.addTestSuite(BrowserResultInterceptorTest.class);
        result.addTestSuite(BrowserSelectionInterceptorTest.class);
        result.addTestSuite(CaptchaActionTest.class);
        result.addTestSuite(CaptchaGeneratorTest.class);
        result.addTestSuite(CaptchaSpecTest.class);
        result.addTestSuite(ClientServerInteractionTest.class);
        result.addTestSuite(ClientServerConnectionTest.class);
        result.addTestSuite(ConfigurationSourceResolutionTest.class);
        result.addTestSuite(ConfigurationTest.class);
        result.addTestSuite(DefaultSkinSourceTest.class);
        result.addTestSuite(DistributedTestSuiteBuilderTest.class);
        result.addTestSuite(EnvironmentVariablesConfigurationSourceTest.class);
        result.addTestSuite(FailedToLaunchBrowserResultTest.class);
        result.addTestSuite(FragmentInterceptorTest.class);
        result.addTestSuite(DistributedTestRunManagerTest.class);
        result.addTestSuite(DistributedTestRunnerActionTest.class);
        result.addTestSuite(JsUnitAggregateServerTest.class);
        result.addTestSuite(JsUnitStandardServerTest.class);
        result.addTestSuite(PropertiesConfigurationSourceTest.class);
        result.addTestSuite(RemoteConfigurationFetcherTest.class);
        result.addTestSuite(RemoteMachineRunnerHitterTest.class);
        result.addTestSuite(RemoteRunSpecificationSelectionInterceptorTest.class);
        result.addTestSuite(RemoteTestRunClientTest.class);
        result.addTestSuite(RequestSourceInterceptorTest.class);
        result.addTestSuite(ResultAcceptorTest.class);
        result.addTestSuite(SecurityInterceptorTest.class);
        result.addTestSuite(SkinInterceptorTest.class);
        result.addTestSuite(UploadedTestPageFactoryTest.class);
        result.addTestSuite(TestRunClientTest.class);
        result.addTestSuite(TestPageTest.class);
        result.addTestSuite(TestRunnerActionSimultaneousRunBlockingTest.class);
        result.addTestSuite(TestRunNotifierServerTest.class);
        result.addTestSuite(TestRunResultBuilderTest.class);
        result.addTestSuite(TestRunManagerTest.class);
        result.addTestSuite(TestRunnerActionTest.class);
        result.addTestSuite(TimeoutCheckerTest.class);
        result.addTestSuite(TimedOutBrowerResultTest.class);
        result.addTestSuite(UploadedTestPageInterceptorTest.class);
        return result;
    }
}
