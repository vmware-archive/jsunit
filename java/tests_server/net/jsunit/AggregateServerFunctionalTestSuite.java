package net.jsunit;

import junit.framework.TestSuite;
import net.jsunit.interceptor.FragmentInterceptorTest;

public class AggregateServerFunctionalTestSuite {

    public static TestSuite suite() {
        TestSuite result = new TestSuite("Aggregate Server Functional Test Suite");
        result.addTestSuite(RunnerServiceAggregateServerFunctionalTest.class);
        result.addTestSuite(ClientFunctionalTest.class);
        result.addTestSuite(FragmentRunnerPageAggregateServerFunctionalTest.class);
        result.addTestSuite(FragmentInterceptorTest.class);
        result.addTestSuite(FragmentRunnerPageFunctionalTest.class);
        result.addTestSuite(HelpPageFunctionalTest.class);
        result.addTestSuite(CaptchaFunctionalTest.class);
        result.addTestSuite(ServerAdminPageFunctionalTest.class);
        result.addTestSuite(ServerDefaultPageFunctionalTest.class);
        result.addTestSuite(MyAccountFunctionalTest.class);
        result.addTestSuite(TestRunServiceClientTest.class);
        result.addTestSuite(UploadRunnerPageFunctionalTest.class);
        result.addTestSuite(UrlRunnerPageFunctionalTest.class);
        return result;
    }

}
