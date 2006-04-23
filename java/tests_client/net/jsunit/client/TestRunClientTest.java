package net.jsunit.client;

import junit.framework.TestCase;
import net.jsunit.MockRemoteServerHitter;
import net.jsunit.model.BrowserResult;
import net.jsunit.model.Result;
import net.jsunit.model.TestCaseResult;
import net.jsunit.model.TestRunResult;
import net.jsunit.utility.XmlUtility;
import org.jdom.Document;

import java.io.File;

public class TestRunClientTest extends TestCase {

    public void testBadServiceURL() throws Exception {
        try {
            new TestRunClient("not a url");
            fail();
        } catch (RuntimeException e) {
        }
    }

    public void testSimple() throws Exception {
        MockRemoteServerHitter mockHitter = new MockRemoteServerHitter();
        TestRunResult testRunResult = dummyResult();
        mockHitter.urlToDocument.put("http://server.jsunit.net/runner", new Document(testRunResult.asXml()));
        TestRunClient client = new TestRunClient("http://server.jsunit.net/runner", mockHitter);
        File page = new File("myPage.html");
        Result result = client.send(page);
        String expectedXML = XmlUtility.asString(testRunResult.asXml());
        assertEquals(expectedXML, XmlUtility.asString(result.asXml()));
    }

    private TestRunResult dummyResult() {
        TestRunResult result = new TestRunResult();
        result.addBrowserResult(successfulBrowserResult());
        result.addBrowserResult(errorBrowserResult());
        return result;
    }

    private BrowserResult successfulBrowserResult() {
        BrowserResult browserResult = new BrowserResult();
        TestCaseResult testCaseResult = TestCaseResult.fromString("file:///dummy/path/dummyPage.html:testBar|1.3|S||");
        browserResult.addTestCaseResult(testCaseResult);
        return browserResult;
    }

    private BrowserResult errorBrowserResult() {
        BrowserResult browserResult = new BrowserResult();
        TestCaseResult testCaseResult = TestCaseResult.fromString("file:///dummy/path/dummyPage.html:testFoo|1.3|E|Test Error Message|");
        browserResult.addTestCaseResult(testCaseResult);
        return browserResult;
    }

}
