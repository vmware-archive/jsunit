package net.jsunit;

import net.jsunit.model.DistributedTestRunResult;
import net.jsunit.model.DistributedTestRunResultBuilder;
import net.jsunit.model.DummyBrowserResult;
import net.jsunit.model.TestRunResult;
import org.jdom.Document;
import org.jdom.JDOMException;

import java.io.IOException;
import java.util.List;

public class RunnerAggregateServerFunctionalTest extends AggregateServerFunctionalTestCase {

    protected MockRemoteServerHitter createMockHitter() {
        MockRemoteServerHitter mockHitter = super.createMockHitter();
        mockHitter.urlToDocument.put(FunctionalTestConfigurationSource.REMOTE_SERVER_URL_1 + "/runner?url=foo.html", successfulResult());
        mockHitter.urlToDocument.put(FunctionalTestConfigurationSource.REMOTE_SERVER_URL_2 + "/runner?url=foo.html", unsuccessfulResult());
        return mockHitter;
    }

    private Document successfulResult() {
        TestRunResult testRunResult = new TestRunResult();
        testRunResult.addBrowserResult(new DummyBrowserResult(true, 0, 0));
        return testRunResult.asXmlDocument();
    }

    private Document unsuccessfulResult() {
        return new DummyFailedTestRunResult().asXmlDocument();
    }

    public void testSimple() throws IOException, JDOMException {
        webTester.beginAt("runner?url=foo.html");
        Document document = responseXmlDocument();
        DistributedTestRunResult distributedTestRunResult = new DistributedTestRunResultBuilder().build(document);
        List<TestRunResult> results = distributedTestRunResult._getTestRunResults();
        assertEquals(2, results.size());
        TestRunResult result0 = results.get(0);
        TestRunResult result1 = results.get(1);
        assertTrue(result0.wasSuccessful());
        assertEquals(1, result0._getBrowserResults().size());
        assertEquals(FunctionalTestConfigurationSource.REMOTE_SERVER_URL_1, result0.getUrl());
        assertFalse(result1.wasSuccessful());
        assertEquals(2, result1._getBrowserResults().size());
        assertEquals(FunctionalTestConfigurationSource.REMOTE_SERVER_URL_2, result1.getUrl());
    }

}
