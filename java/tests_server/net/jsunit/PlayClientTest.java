package net.jsunit;

import junit.framework.TestCase;
import net.jsunit.captcha.CaptchaSpec;
import net.jsunit.client.DummyTestPageWriter;
import net.jsunit.client.TestRunClient;
import net.jsunit.model.DistributedTestRunResult;
import net.jsunit.model.Result;
import net.jsunit.utility.XmlUtility;

import java.io.File;
import java.net.URLEncoder;

public class PlayClientTest extends TestCase {
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
        CaptchaSpec spec = new CaptchaSpec("1234567890123456", "foobar", System.currentTimeMillis());
        client.addParameter("captchaKey", URLEncoder.encode(spec.getEncryptedKey(), "UTF-8"));
        client.addParameter("attemptedCaptchaAnswer", "foobar");
        Result result = client.send(testPage);
        assertTrue(result.wasSuccessful());
        DistributedTestRunResult distributedResult = (DistributedTestRunResult) result;
        System.out.println(XmlUtility.asPrettyString(result.asXml()));
        assertEquals(3, distributedResult.getTestRunResults().size());
    }

}
