package net.jsunit;

import junit.framework.TestCase;
import net.jsunit.client.DummyTestPageWriter;
import net.jsunit.client.TestRunClient;
import net.jsunit.model.DistributedTestRunResult;
import net.jsunit.model.Result;
import net.jsunit.utility.XmlUtility;

import java.io.File;

public class ExperimentalClientTest extends TestCase {
    private String directory;
    private DummyTestPageWriter writer;

    protected void setUp() throws Exception {
        super.setUp();
        directory = String.valueOf(System.currentTimeMillis());
        writer = new DummyTestPageWriter(directory, "foobar.html");
        writer.writeFiles();
    }

    protected void tearDown() throws Exception {
        writer.removeFiles();
        super.tearDown();
    }

    public void testSimple() throws Exception {
        File testPage = new File(directory, "foobar.html");
        TestRunClient client = new TestRunClient("http://69.181.237.145/jsunit/runner");
        client.setUsername("username");
        client.setPassword("password");
        Result result = client.send(testPage);
        System.out.println(XmlUtility.asPrettyString(result.asXml()));
        assertTrue(result.wasSuccessful());
        DistributedTestRunResult distributedResult = (DistributedTestRunResult) result;
        assertEquals(3, distributedResult.getTestRunResults().size());
    }

}
