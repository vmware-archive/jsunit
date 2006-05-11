package net.jsunit.client;

import junit.framework.AssertionFailedError;
import junit.framework.Test;
import junit.framework.TestResult;
import junit.framework.TestSuite;
import net.jsunit.model.Result;

import java.io.File;

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
                    Result remoteResult = null;
                    try {
                        remoteResult = testRunClient.send(testPage);
                    } catch (Exception e) {
                        jUnitResult.addError(this, e);
                    }
                    if (!remoteResult.wasSuccessful())
                        jUnitResult.addFailure(this, new AssertionFailedError(remoteResult.displayString()));
                } catch (Exception e) {
                    jUnitResult.addError(this, e);
                }
            }
        });
        return suite;
    }

}
