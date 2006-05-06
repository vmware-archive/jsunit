package net.jsunit;

import net.jsunit.model.Browser;
import net.jsunit.model.BrowserResult;
import net.jsunit.model.DistributedTestRunResult;
import net.jsunit.model.TestRunResult;
import net.jsunit.utility.XmlUtility;
import org.jdom.Element;

import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.List;

public class RunnerServiceAggregateServerFunctionalTest extends AggregateServerFunctionalTestCase {

    public void testHitAggregateRunner() throws Exception {
        String testUrl = URLEncoder.encode("http://localhost:8080/jsunit/tests/jsUnitUtilityTests.html", "UTF-8");
        mockHitter.urlToDocument.put(
                FunctionalTestAggregateConfigurationSource.REMOTE_SERVER_URL_1 + "/runner?url=" + testUrl,
                result1().asXmlDocument()
        );
        mockHitter.urlToDocument.put(
                FunctionalTestAggregateConfigurationSource.REMOTE_SERVER_URL_2 + "/runner?url=" + testUrl,
                result2().asXmlDocument()
        );

        webTester.beginAt("/runner?url=" + testUrl);
        DistributedTestRunResult expectedDistributedResult = new DistributedTestRunResult();
        expectedDistributedResult.addTestRunResult(result1());
        expectedDistributedResult.addTestRunResult(result2());
        Element expectedDistributedResultRootElement = expectedDistributedResult.asXml();
        Element actualRootElement = responseXmlDocument().getRootElement();
        detachIdAttributes(expectedDistributedResultRootElement);
        detachIdAttributes(actualRootElement);
        assertEquals(
                XmlUtility.asPrettyString(expectedDistributedResultRootElement),
                XmlUtility.asPrettyString(actualRootElement)
        );
    }

    private void detachIdAttributes(Element element) {
        element.removeAttribute("id");
        //noinspection unchecked
        for (Element child : (List<Element>) element.getChildren())
            detachIdAttributes(child);
    }

    private TestRunResult result1() throws MalformedURLException {
        TestRunResult testRunResult = new TestRunResult();
        BrowserResult browserResult0 = new BrowserResult();
        browserResult0.setBrowser(new Browser("browser0.exe", 0));
        testRunResult.addBrowserResult(browserResult0);
        BrowserResult browserResult1 = new BrowserResult();
        browserResult1.setBrowser(new Browser("browser1.exe", 1));
        testRunResult.addBrowserResult(browserResult1);
        testRunResult.setURL(new URL(FunctionalTestAggregateConfigurationSource.REMOTE_SERVER_URL_1));
        return testRunResult;
    }

    private TestRunResult result2() throws MalformedURLException {
        TestRunResult testRunResult = new TestRunResult();
        BrowserResult browserResult2 = new BrowserResult();
        browserResult2.setBrowser(new Browser("browser2.exe", 2));
        testRunResult.addBrowserResult(browserResult2);
        BrowserResult browserResult3 = new BrowserResult();
        browserResult3.setBrowser(new Browser("browser3.exe", 3));
        testRunResult.addBrowserResult(browserResult3);
        testRunResult.setURL(new URL(FunctionalTestAggregateConfigurationSource.REMOTE_SERVER_URL_2));
        return testRunResult;
    }

}
