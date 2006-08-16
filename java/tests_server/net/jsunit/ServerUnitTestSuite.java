package net.jsunit;

import junit.framework.Test;
import junit.framework.TestSuite;
import net.jsunit.action.*;
import net.jsunit.configuration.*;
import net.jsunit.interceptor.*;
import net.jsunit.model.BrowserLaunchSpecificationTest;
import net.jsunit.model.HeterogenousBrowserGroupTest;
import net.jsunit.server.RemoteRunSpecificationBuilderTest;

public class ServerUnitTestSuite {

    public static Test suite() {
        TestSuite result = new TestSuite("Server unit tests");

        result.addTestSuite(ArgumentsConfigurationSourceTest.class);
        result.addTestSuite(ConfigurationSourceResolutionTest.class);
        result.addTestSuite(CompositeConfigurationSourceTest.class);
        result.addTestSuite(EnvironmentVariablesConfigurationSourceTest.class);
        result.addTestSuite(PropertiesFileConfigurationSourceTest.class);

        result.addTestSuite(ErrorXmlRenderableTest.class);
        result.addTestSuite(ResultAcceptorActionTest.class);
        result.addTestSuite(ResultDisplayerActionTest.class);
        result.addTestSuite(TestRunnerActionSimultaneousRunBlockingTest.class);
        result.addTestSuite(TestRunnerActionTest.class);

        result.addTestSuite(BrowserResultInterceptorTest.class);
        result.addTestSuite(BrowserSelectionInterceptorTest.class);
        result.addTestSuite(BrowserTestRunnerInterceptorTest.class);
        result.addTestSuite(LocalhostOnlyInterceptorTest.class);
        result.addTestSuite(RemoteServerHitterInterceptorTest.class);
        result.addTestSuite(RequestSourceInterceptorTest.class);

        result.addTestSuite(BrowserLaunchSpecificationTest.class);
        result.addTestSuite(HeterogenousBrowserGroupTest.class);

        result.addTestSuite(RemoteRunSpecificationBuilderTest.class);

        result.addTestSuite(BrowserResultLogWriterTest.class);
        result.addTestSuite(ClientServerInteractionTest.class);
        result.addTestSuite(DistributedTestRunManagerTest.class);
        result.addTestSuite(DistributedTestSuiteBuilderTest.class);
        result.addTestSuite(JsUnitServerTest.class);
        result.addTestSuite(PlatformTypeTest.class);
        result.addTestSuite(RemoteConfigurationFetcherTest.class);
        result.addTestSuite(RemoteConfigurationSourceTest.class);
        result.addTestSuite(RemoteMachineRunnerHitterTest.class);
        result.addTestSuite(RemoteRunSpecificationTest.class);
        result.addTestSuite(ResultAcceptorTest.class);
        result.addTestSuite(TestRunManagerTest.class);
        result.addTestSuite(TestRunNotifierServerTest.class);
        result.addTestSuite(TimeoutCheckerTest.class);

        return result;
    }

}
