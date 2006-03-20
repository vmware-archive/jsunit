package net.jsunit;

import net.jsunit.model.BrowserResultWriter;
import net.jsunit.model.ResultType;
import org.jdom.Document;
import org.jdom.Element;

import java.net.URLEncoder;
import java.util.List;

public class RunnerFunctionalTest extends FunctionalTestCase {

    public void testSimple() throws Exception {
        webTester.beginAt("runner");
        Document result = responseXmlDocument();
        assertResult(
                result,
                ResultType.SUCCESS,
                "http://localhost:" + port + "/jsunit/tests/jsUnitUtilityTests.html", 2);
    }

    public void testOverrideUrl() throws Exception {
        webTester.beginAt("runner?url=" + URLEncoder.encode("http://127.0.0.1:" + port + "/jsunit/testRunner.html?testPage=http://127.0.0.1:" + port + "/jsunit/tests/jsUnitUtilityTests.html&autoRun=true&submitresults=true", "UTF-8"));
        Document result = responseXmlDocument();
        assertResult(
                result,
                ResultType.SUCCESS,
                "http://127.0.0.1:" + port + "/jsunit/tests/jsUnitUtilityTests.html", 2);
    }

    public void testSpecifyBrowser() throws Exception {
        webTester.beginAt("runner?browserId=0");
        Document result = responseXmlDocument();
        assertResult(
                result,
                ResultType.SUCCESS,
                "http://localhost:" + port + "/jsunit/tests/jsUnitUtilityTests.html", 1);
    }

    public void testSpecifyInvalidBrowser() throws Exception {
        webTester.beginAt("runner?browserId=23");
        Document result = responseXmlDocument();
        assertErrorResponse(result.getRootElement(), "Invalid browser ID: 23");
    }

    protected boolean shouldMockOutProcessStarter() {
        return false;
    }

    private void assertResult(Document result, ResultType expectedResultType, String expectedUrl, int expectedBrowserResultCount) {
        Element root = result.getRootElement();
        assertEquals("testRunResult", root.getName());
        assertEquals(expectedBrowserResultCount, root.getChildren("browserResult").size());
        assertEquals(expectedResultType.name(), root.getAttribute("type").getValue());
        Element urlProperty = urlPropertyElement(root);
        assertEquals(expectedUrl, urlProperty.getAttribute(BrowserResultWriter.PROPERTY_VALUE).getValue());
    }

    @SuppressWarnings("unchecked")
    private Element urlPropertyElement(Element root) {
        List<Element> children = root.getChild("browserResult").getChild(BrowserResultWriter.PROPERTIES).getChildren(BrowserResultWriter.PROPERTY);
        for (Element child : children) {
            if (child.getAttribute(BrowserResultWriter.PROPERTY_KEY).getValue().equals(BrowserResultWriter.URL))
                return child;
        }
        return null;
    }

}
