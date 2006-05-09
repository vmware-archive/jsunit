package net.jsunit;

import net.jsunit.client.DummyTestPageWriter;
import net.jsunit.client.TestRunClient;
import net.jsunit.model.BrowserResult;
import net.jsunit.model.TestRunResult;

import java.io.File;

public class ClientFunctionalTest extends StandardServerFunctionalTestCase {
    private String directory;
    private DummyTestPageWriter writer;

    protected boolean shouldMockOutProcessStarter() {
        return false;
    }

    public void setUp() throws Exception {
        super.setUp();
        directory = String.valueOf(System.currentTimeMillis());
        writer = new DummyTestPageWriter(directory, "a test page.html");
        writer.writeFiles();
    }

    public void tearDown() throws Exception {
        writer.removeFiles();
        super.tearDown();
    }

    public void testSimple() throws Exception {
        File file = new File(directory, "a test page.html");
        TestRunClient client = new TestRunClient(baseURL() + "/runner");
        TestRunResult testRunResult = (TestRunResult) client.send(file);
        assertTrue(testRunResult.wasSuccessful());
        assertEquals(2, testRunResult.getBrowserResults().size());
        BrowserResult browserResult = testRunResult.getBrowserResults().get(0);
        assertEquals(2, browserResult.getTestCaseResults().size());
    }

}
