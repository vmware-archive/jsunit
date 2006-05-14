package net.jsunit.client;

import junit.extensions.ActiveTestSuite;
import junit.framework.Test;
import junit.framework.TestResult;
import junit.framework.TestSuite;

import java.io.File;

public class ClientTestSuite extends ActiveTestSuite {

    public static TestSuite forTestPageAndRunnerServiceUrl(final File testPage, final String serviceURL) {
        TestSuite suite = new ClientTestSuite();
        TestRunClient testRunClient = new TestRunClient(serviceURL);
        suite.addTest(new RemoteTestRun());
        return suite;
    }

    static class RemoteTestRun implements Test {
        public int countTestCases() {
            return 1;
        }

        public void run(TestResult jUnitResult) {
/*
            try {
                Result remoteResult = testRunClient.send(testPage);
                if (!remoteResult.wasSuccessful())
                    jUnitResult.addFailure(this, new AssertionFailedError(remoteResult.displayString()));
            } catch (Exception e) {
                jUnitResult.addError(this, e);
            }
*/
        }
    }

}
