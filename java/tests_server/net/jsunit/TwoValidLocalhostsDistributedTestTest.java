package net.jsunit;

import net.jsunit.configuration.ServerConfiguration;
import net.jsunit.configuration.StubConfigurationSource;
import net.jsunit.model.Browser;
import net.jsunit.utility.FileUtility;

public class TwoValidLocalhostsDistributedTestTest extends EndToEndTestCase {
    private JsUnitServer secondServer;
    private int otherPort;

    public void setUp() throws Exception {
        super.setUp();
        otherPort = new TestPortManager().newPort();
        secondServer = new JsUnitServer(new ServerConfiguration(secondServerSource()));
        secondServer.start();
    }

    protected void tearDown() throws Exception {
        if (secondServer != null)
            secondServer.dispose();
        super.tearDown();
    }

    protected StubConfigurationSource serverSource() {
        return new StubConfigurationSource() {

            public String browserFileNames() {
                return Browser.DEFAULT_SYSTEM_BROWSER;
            }

            public String remoteMachineURLs() {
                return "http://localhost:" + port + ", http://localhost:" + otherPort;
            }

            public String url() {
                return "http://localhost:" + port + "/jsunit/testRunner.html?"
                        + "testPage=http://localhost:" + port + "/jsunit/tests/jsUnitUtilityTests.html&autoRun=true&submitresults=true";
            }

            public String port() {
                return String.valueOf(port);
            }

            public String resourceBase() {
                return FileUtility.jsUnitPath().getAbsolutePath();
            }
        };
    }

    protected StubConfigurationSource secondServerSource() {
        return new StubConfigurationSource() {

            public String browserFileNames() {
                return Browser.DEFAULT_SYSTEM_BROWSER;
            }

            public String url() {
                return "http://localhost:" + port + "/jsunit/testRunner.html?"
                        + "testPage=http://localhost:" + port + "/jsunit/tests/jsUnitUtilityTests.html&autoRun=true&submitresults=true";
            }

            public String port() {
                return String.valueOf(otherPort);
            }
        };
    }

    public void testSuccessfulRun() {
        DistributedTest test = new DistributedTest(serverSource());
        assertSuccessful(test);
        assertEquals(2, test.getTestRunResults().size());
        assertNull(test.getTemporaryServer());
    }

}