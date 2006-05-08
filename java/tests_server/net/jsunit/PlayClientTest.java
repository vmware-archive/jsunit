package net.jsunit;

import junit.framework.TestCase;
import net.jsunit.model.DistributedTestRunResult;
import net.jsunit.model.Result;
import net.jsunit.client.TestRunClient;
import net.jsunit.client.DummyTestPageWriter;
import net.jsunit.utility.XmlUtility;

import java.io.File;
import java.net.URLEncoder;

public class PlayClientTest extends TestCase {
    private String directory;
    private DummyTestPageWriter writer;

//    public static Test suite() {
//        File testPage = new File("tests" + File.separator + "jsUnitUtilityTests.html");
//        return ClientTestSuite.forTestPageAndRunnerServiceUrl(testPage, "http://69.181.237.145/jsunit/runner");
//    }
//

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
        client.addParameter("captchaKey", URLEncoder.encode("Bi0a02YbLpBI09dY0MfqLq0Fy0GodjGw5nX1zMdrUuA=", "UTF-8"));
        client.addParameter("attemptedCaptchaAnswer", "rhrbrp");
        Result result = client.send(testPage);
        assertTrue(result.wasSuccessful());
        DistributedTestRunResult distributedResult = (DistributedTestRunResult) result;
        assertEquals(3, distributedResult.getTestRunResults().size());
        System.out.println(XmlUtility.asPrettyString(result.asXml()));
    }

}
