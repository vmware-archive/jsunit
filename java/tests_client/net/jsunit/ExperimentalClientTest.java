package net.jsunit;

import junit.framework.TestCase;
import net.jsunit.client.TestRunClient;
import net.jsunit.model.DummyTestPageWriter;
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
//        TestRunClient client = new TestRunClient("http://server.jsunit.net/axis/services/TestRunService");
        TestRunClient client = new TestRunClient("http://localhost:8090/axis/services/TestRunService");
        client.setUsername("username");
        client.setPassword("password");
        Result result = client.send(testPage);
        System.out.println(XmlUtility.asPrettyString(result.asXml()));
        assertTrue(result.wasSuccessful());
    }

}
