package net.jsunit.client;

import junit.framework.TestCase;
import net.jsunit.MockRemoteServerHitter;
import net.jsunit.model.BrowserResult;
import net.jsunit.model.TestCaseResult;
import net.jsunit.model.TestPage;
import net.jsunit.model.TestRunResult;
import net.jsunit.utility.XmlUtility;
import org.jdom.Document;

public class TestRunRequestTest extends TestCase {

    public void testBadServiceURL() throws Exception {
        try {
            new TestRunRequest("not a url");
            fail();
        } catch (RuntimeException e) {
        }
    }

    public void xtestSimple() throws Exception {
        MockRemoteServerHitter mockHitter = new MockRemoteServerHitter();
        mockHitter.urlToDocument.put("http://server.jsunit.net/runner", new Document(dummyResult().asXml()));
        TestRunRequest request = new TestRunRequest("http://server.jsunit.net/runner", mockHitter);
        TestPage page = new TestPage("<html>my stuff</html>", false);
        TestRunResult result = request.send(page);
        String expectedXML = XmlUtility.asString(dummyResult().asXml());
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
