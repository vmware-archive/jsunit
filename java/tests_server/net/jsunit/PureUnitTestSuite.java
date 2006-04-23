package net.jsunit;

import junit.framework.Test;
import junit.framework.TestSuite;
import net.jsunit.action.*;
import net.jsunit.client.TestRunClientTest;
import net.jsunit.configuration.ArgumentsConfigurationSourceTest;
import net.jsunit.interceptor.BrowserTestRunnerInterceptorTest;
import net.jsunit.interceptor.RemoteRunnerHitterInterceptorTest;
import net.jsunit.interceptor.VersionGrabberInterceptorTest;
import net.jsunit.model.*;
import net.jsunit.results.SkinTest;

public class PureUnitTestSuite {

    public static Test suite() {
        TestSuite result = new TestSuite();
        result.addTestSuite(AdminActionTest.class);
        result.addTestSuite(ArgumentsConfigurationSourceTest.class);
        result.addTestSuite(BrowserGroupTest.class);
        result.addTestSuite(BrowserLaunchSpecificationTest.class);
        result.addTestSuite(BrowserResultLogWriterTest.class);
        result.addTestSuite(BrowserTest.class);
        result.addTestSuite(BrowserTestRunnerInterceptorTest.class);
        result.addTestSuite(ErrorXmlRenderableTest.class);
        result.addTestSuite(DistributedTestRunResultTest.class);
        result.addTestSuite(PageActionTest.class);
        result.addTestSuite(LatestVersionActionTest.class);
        result.addTestSuite(ReferencedJsFileTest.class);
        result.addTestSuite(RemoteRunnerHitterInterceptorTest.class);
        result.addTestSuite(ResultAcceptorActionTest.class);
        result.addTestSuite(ResultBuilderTest.class);
        result.addTestSuite(ResultDisplayerActionTest.class);
        result.addTestSuite(SkinTest.class);
        result.addTestSuite(ServerStatusActionTest.class);
        result.addTestSuite(StatusMessageTest.class);
        result.addTestSuite(TestCaseResultTest.class);
        result.addTestSuite(TestPageResultTest.class);
        result.addTestSuite(TestRunClientTest.class);
        result.addTestSuite(VersionGrabberInterceptorTest.class);
        return result;
    }
}
