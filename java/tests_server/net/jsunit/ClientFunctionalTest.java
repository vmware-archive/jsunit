package net.jsunit;

import net.jsunit.client.TestRunClient;
import net.jsunit.model.DummyTestPageWriter;
import net.jsunit.model.DistributedTestRunResult;

import java.io.File;

public class ClientFunctionalTest extends AggregateServerFunctionalTestCase {
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
        TestRunClient client = new TestRunClient("http://localhost:" + port + "/axis/services/TestRunService");
        DistributedTestRunResult distributedTestRunResult = (DistributedTestRunResult) client.send(file);
        assertTrue(distributedTestRunResult.wasSuccessful());
        assertEquals(2, distributedTestRunResult._getTestRunResults().size());
    }

}
