package net.jsunit.model;

import net.jsunit.utility.XmlUtility;
import org.jdom.Attribute;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.input.SAXBuilder;

import java.io.File;
import java.util.List;

public class BrowserResultBuilder {

    public BrowserResult build(File file) {
        try {
            Document document = new SAXBuilder().build(file);
            return build(document);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public BrowserResult build(String string) {
        Document document = XmlUtility.asXmlDocument(string);
        return build(document);
    }

    @SuppressWarnings("unchecked")
    public BrowserResult build(Document document) {
        Element root = document.getRootElement();
        return build(root);
    }

    @SuppressWarnings("unchecked")
    public BrowserResult build(Element root) {
        BrowserResult result = new BrowserResult();
        if (failedToLaunch(root))
            result.setFailedToLaunch();
        else if (timedOut(root))
            result.setTimedOut();
        updateWithHeaders(result, root);
        updateWithProperties(root.getChild(BrowserResultWriter.PROPERTIES), result);
        Element testCasesElement = root.getChild(BrowserResultWriter.TEST_CASES);
        if (testCasesElement != null) {
            List children = testCasesElement.getChildren(TestCaseResultWriter.TEST_CASE);
            updateWithTestCaseResults(children, result);
        }
        return result;
    }

    private boolean failedToLaunch(Element root) {
        Attribute failedToLaunchAttribute = root.getAttribute(BrowserResultWriter.FAILED_TO_LAUNCH);
        return failedToLaunchAttribute != null && failedToLaunchAttribute.getValue().equals(String.valueOf(true));
    }

    private boolean timedOut(Element root) {
        Attribute timedOutAttribute = root.getAttribute(BrowserResultWriter.TIMED_OUT);
        return timedOutAttribute != null && timedOutAttribute.getValue().equals(String.valueOf(true));
    }

    private void updateWithHeaders(BrowserResult result, Element element) {
        String id = element.getAttributeValue(BrowserResultWriter.ID);
        if (id != null)
            result.setId(id);
        String time = element.getAttributeValue(BrowserResultWriter.TIME);
        if (time != null)
            result.setTime(Double.parseDouble(time));
    }

    private void updateWithProperties(Element element, BrowserResult result) {
        Integer browserId = null;
        String browserFileName = null;
        for (Object child : element.getChildren()) {
            Element next = (Element) child;
            String key = next.getAttributeValue(BrowserResultWriter.PROPERTY_KEY);
            String value = next.getAttributeValue(BrowserResultWriter.PROPERTY_VALUE);

            if (BrowserResultWriter.JSUNIT_VERSION.equals(key))
                result.setJsUnitVersion(value);
            else if (BrowserResultWriter.USER_AGENT.equals(key))
                result.setUserAgent(value);
            else if (BrowserResultWriter.REMOTE_ADDRESS.equals(key))
                result.setRemoteAddress(value);
            else if (BrowserResultWriter.URL.equals(key))
                result.setBaseURL(value);
            else if (BrowserResultWriter.SERVER_SIDE_EXCEPTION_STACK_TRACE.equals(key)) {
                String stackTrace = next.getText();
                result.setServerSideExceptionStackTrace(stackTrace);
            } else if (BrowserResultWriter.BROWSER_ID.equals(key))
                browserId = Integer.parseInt(value);
            else if (BrowserResultWriter.BROWSER_FILE_NAME.equals(key))
                browserFileName = value;
        }
        if (browserId != null && browserFileName != null) {
            Browser remoteBrowser = new Browser(browserFileName, browserId);
            result.setBrowser(remoteBrowser);
        }
    }

    private void updateWithTestCaseResults(List<Element> testCaseElements, BrowserResult result) {
        TestCaseResultBuilder testCaseBuilder = new TestCaseResultBuilder();
        for (Element element : testCaseElements) {
            result.addTestCaseResult(testCaseBuilder.build(element));
        }
    }
}
