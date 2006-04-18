package net.jsunit;

import junit.framework.Test;
import junit.framework.TestSuite;
import net.jsunit.action.*;
import net.jsunit.configuration.ArgumentsConfigurationSourceTest;
import net.jsunit.interceptor.BrowserTestRunnerInterceptorTest;
import net.jsunit.interceptor.RemoteRunnerHitterInterceptorTest;
import net.jsunit.interceptor.VersionGrabberInterceptorTest;
import net.jsunit.model.BrowserTest;
import net.jsunit.model.TestCaseResultTest;
import net.jsunit.model.TestPageResultTest;
import net.jsunit.results.SkinTest;
import net.jsunit.upload.ReferencedJsFileTest;

public class PureUnitTestSuite {

    public static Test suite() {
        TestSuite result = new TestSuite();
        result.addTestSuite(AdminActionTest.class);
        result.addTestSuite(ArgumentsConfigurationSourceTest.class);
        result.addTestSuite(BrowserLaunchSpecificationTest.class);
        result.addTestSuite(BrowserResultLogWriterTest.class);
        result.addTestSuite(BrowserTest.class);
        result.addTestSuite(BrowserTestRunnerInterceptorTest.class);
        result.addTestSuite(ErrorXmlRenderableTest.class);
        result.addTestSuite(DistributedTestRunResultTest.class);
        result.addTestSuite(IndexActionTest.class);
        result.addTestSuite(LatestVersionActionTest.class);
        result.addTestSuite(ReferencedJsFileTest.class);
        result.addTestSuite(RemoteRunnerHitterInterceptorTest.class);
        result.addTestSuite(ResultAcceptorActionTest.class);
        result.addTestSuite(ResultDisplayerActionTest.class);
        result.addTestSuite(SkinTest.class);
        result.addTestSuite(ServerStatusActionTest.class);
        result.addTestSuite(StatusMessageTest.class);
        result.addTestSuite(TestCaseResultTest.class);
        result.addTestSuite(TestPageResultTest.class);
        result.addTestSuite(VersionGrabberInterceptorTest.class);
        return result;
    }
}
