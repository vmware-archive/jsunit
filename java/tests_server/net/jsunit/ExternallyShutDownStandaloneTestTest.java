package net.jsunit;

import net.jsunit.configuration.ConfigurationSource;
import net.jsunit.model.Browser;
import net.jsunit.model.ResultType;

public class ExternallyShutDownStandaloneTestTest extends EndToEndTestCase {

    protected ConfigurationSource configurationSource() {
        return new StubConfigurationSource() {
            public String browserFileNames() {
                return Browser.DEFAULT_SYSTEM_BROWSER;
            }

            public String url() {
                return "http://localhost:8080/jsunit/testRunner.html?" +
                        "testPage=http://localhost:8080/jsunit/tests/jsUnitTestSuite.html" +
                        "&autoRun=true&submitresults=true&resultId=foobar";
            }
        };
    }

    public void testBrowsersExternallyShutDown() throws Exception {
        final StandaloneTest test = new StandaloneTest(configurationSource());
        new Thread() {
            public void run() {
                try {
                    while (test.getServer() == null)
                        Thread.sleep(100);
                    while (test.getServer().getBrowserProcess() == null)
                        Thread.sleep(100);
                } catch (InterruptedException e) {
                    fail();
                }
                Process process = test.getServer().getBrowserProcess();
                process.destroy();
                try {
                    process.waitFor();
                } catch (InterruptedException e) {
                    fail();
                }
            }
        }.start();

        assertFailure(test, ResultType.EXTERNALLY_SHUT_DOWN);
    }

}
