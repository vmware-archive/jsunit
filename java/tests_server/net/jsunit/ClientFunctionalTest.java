package net.jsunit;

import net.jsunit.model.TestRunResult;
import net.jsunit.model.BrowserResult;
import net.jsunit.client.TestRunClient;

import java.io.File;

public class ClientFunctionalTest extends FunctionalTestCase {

    protected boolean shouldMockOutProcessStarter() {
        return false;
    }

    public void testSimple() throws Exception {
        File file = new File("tests" + File.separatorChar + "jsUnitUtilityTests.html");
        TestRunResult testRunResult = new TestRunClient(baseURL() + "/runner").send(file);
        assertTrue(testRunResult.wasSuccessful());
        assertEquals(2, testRunResult.getBrowserResults().size());
        BrowserResult browserResult = testRunResult.getBrowserResults().get(0);
        assertEquals(4, browserResult.getTestCaseResults().size());
    }

}
