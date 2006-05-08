package net.jsunit;

import junit.framework.Test;
import junit.framework.TestSuite;
import net.jsunit.action.*;
import net.jsunit.captcha.SecurityViolationTest;
import net.jsunit.client.TestRunClientTest;
import net.jsunit.configuration.ArgumentsConfigurationSourceTest;
import net.jsunit.configuration.RemoteConfigurationTest;
import net.jsunit.interceptor.BrowserTestRunnerInterceptorTest;
import net.jsunit.interceptor.RemoteRunnerHitterInterceptorTest;
import net.jsunit.interceptor.VersionGrabberInterceptorTest;
import net.jsunit.model.*;
import net.jsunit.results.SkinTest;
import net.jsunit.server.RemoteRunSpecificationBuilderTest;
import net.jsunit.uploaded.UploadedTestPageTest;

public class PureUnitTestSuite {

    public static Test suite() {
        TestSuite result = new TestSuite();
        result.addTestSuite(AdminActionTest.class);
        result.addTestSuite(ArgumentsConfigurationSourceTest.class);
        result.addTestSuite(HeterogenousBrowserGroupTest.class);
        result.addTestSuite(BrowserLaunchSpecificationTest.class);
        result.addTestSuite(BrowserResultLogWriterTest.class);
        result.addTestSuite(BrowserResultTest.class);
        result.addTestSuite(BrowserTest.class);
        result.addTestSuite(BrowserTestRunnerInterceptorTest.class);
        result.addTestSuite(ErrorXmlRenderableTest.class);
        result.addTestSuite(DistributedTestRunResultTest.class);
        result.addTestSuite(PageActionTest.class);
        result.addTestSuite(LatestVersionActionTest.class);
        result.addTestSuite(PlatformTypeTest.class);
        result.addTestSuite(ReferencedJsFileTest.class);
        result.addTestSuite(RemoteConfigurationSourceTest.class);
        result.addTestSuite(RemoteConfigurationTest.class);
        result.addTestSuite(RemoteRunnerHitterInterceptorTest.class);
        result.addTestSuite(RemoteRunSpecificationBuilderTest.class);
        result.addTestSuite(RemoteRunSpecificationTest.class);
        result.addTestSuite(ResultAcceptorActionTest.class);
        result.addTestSuite(ResultBuilderTest.class);
        result.addTestSuite(ResultDisplayerActionTest.class);
        result.addTestSuite(SecurityViolationTest.class);
        result.addTestSuite(ServerStatusActionTest.class);
        result.addTestSuite(SkinTest.class);
        result.addTestSuite(StatusMessageTest.class);
        result.addTestSuite(TestCaseResultTest.class);
        result.addTestSuite(UploadedTestPageTest.class);
        result.addTestSuite(TestPageResultTest.class);
        result.addTestSuite(TestRunClientTest.class);
        result.addTestSuite(TestRunResultTest.class);
        result.addTestSuite(VersionGrabberInterceptorTest.class);
        return result;
    }
}
