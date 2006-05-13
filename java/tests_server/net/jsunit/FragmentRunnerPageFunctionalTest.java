package net.jsunit;

import net.jsunit.model.DistributedTestRunResult;
import net.jsunit.model.DistributedTestRunResultBuilder;
import net.jsunit.model.TestRunResult;
import org.jdom.Document;

import java.net.URL;

public class FragmentRunnerPageFunctionalTest extends AggregateServerFunctionalTestCase {

    public void setUp() throws Exception {
        super.setUp();
        webTester.beginAt("/fragmentRunnerPage");
        mockHitter.setDocumentRetrievalStrategy(new DocumentRetrievalStrategy() {
            public Document get(URL url) {
                return new TestRunResult().asXmlDocument();
            }
        });
        mockHitter.urlsPassed.clear();
    }

    public void testInitialConditions() throws Exception {
        assertOnFragmentRunnerPage();
    }

    public void testPasteInFragment() throws Exception {
        webTester.setFormElement("fragment", "assertTrue(true);");
        webTester.selectOption("skinId", "None (raw XML)");
        webTester.submit();
        Document document = responseXmlDocument();
        DistributedTestRunResult result = new DistributedTestRunResultBuilder().build(document);
        assertEquals(2, result._getTestRunResults().size());
        assertEquals(2, mockHitter.urlsPassed.size());
        for (String url : mockHitter.urlsPassed)
            assertTrue(url.indexOf("uploaded") != -1);
    }

    public void testPasteFragmentSingleBrowser() throws Exception {
        webTester.setFormElement("fragment", "assertTrue(false);");
        webTester.setFormElement("urlId_browserId", "0_0");
        webTester.selectOption("skinId", "None (raw XML)");
        webTester.submit();
        assertEquals(1, mockHitter.urlsPassed.size());
        assertTrue(mockHitter.urlsPassed.get(0).indexOf("uploaded") != -1);
    }

}
