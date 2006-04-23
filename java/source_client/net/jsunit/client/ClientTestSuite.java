package net.jsunit.client;

import junit.framework.AssertionFailedError;
import junit.framework.Test;
import junit.framework.TestResult;
import junit.framework.TestSuite;
import net.jsunit.model.Result;

import java.io.File;
import java.io.IOException;

public class ClientTestSuite {

    public static TestSuite forTestPageAndRunnerServiceUrl(final File testPage, final String serviceURL) {
        TestSuite suite = new TestSuite();
        suite.addTest(new Test() {
            public int countTestCases() {
                return 1;
            }

            public void run(TestResult jUnitResult) {
                TestRunClient testRunClient = new TestRunClient(serviceURL);
                try {
                    Result remoteResult = testRunClient.send(testPage);
                    if (!remoteResult.wasSuccessful())
                        jUnitResult.addFailure(this, new AssertionFailedError(remoteResult.displayString()));
                } catch (IOException e) {
                    jUnitResult.addError(this, e);
                }
            }
        });
        return suite;
    }

}
