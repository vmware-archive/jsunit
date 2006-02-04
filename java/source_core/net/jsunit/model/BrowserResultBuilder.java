package net.jsunit.model;

import net.jsunit.Utility;

import org.jdom.Attribute;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.input.SAXBuilder;

import java.io.File;
import java.util.List;
 
/**
 * @author Edward Hieatt, edward@jsunit.net
 */

public class BrowserResultBuilder {

    public BrowserResult build(File file) {
        try {
            Document document = new SAXBuilder().build(file);
            return build(document);
        } catch (Exception e) {
            System.err.println("Failed to read file " + file + ": " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }
    
    public BrowserResult build(String string) {
		Document document = Utility.asXmlDocument(string);
		return build(document);
    }

	public BrowserResult build(Document document) {
        Element root = document.getRootElement();
        BrowserResult result;
        if (failedToLaunch(root))
        	result = new FailedToLaunchBrowserResult();
        else if (timedOut(root))
        	result = new TimedOutBrowserResult();
        else 
        	result = new BrowserResult();
        updateWithHeaders(result, root);
        updateWithProperties(root.getChild(BrowserResultWriter.PROPERTIES), result);
        Element testCasesElement = root.getChild(BrowserResultWriter.TEST_CASES);
        if (testCasesElement != null)
        	updateWithTestCaseResults(testCasesElement.getChildren(TestCaseResultWriter.TEST_CASE), result);
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
        if (id!=null)
        	result.setId(id);
        String time = element.getAttributeValue(BrowserResultWriter.TIME);
        if (time!=null)
        	result.setTime(Double.parseDouble(time));
    }

    private void updateWithProperties(Element element, BrowserResult result) {
        for (Object child : element.getChildren()) {
            Element next = (Element) child;
            if (BrowserResultWriter.JSUNIT_VERSION.equals(next.getAttributeValue(BrowserResultWriter.PROPERTY_KEY)))
                result.setJsUnitVersion(next.getAttributeValue(BrowserResultWriter.PROPERTY_VALUE));
            else if (BrowserResultWriter.BROWSER_FILE_NAME.equals(next.getAttributeValue(BrowserResultWriter.PROPERTY_KEY)))
                result.setBrowserFileName(next.getAttributeValue(BrowserResultWriter.PROPERTY_VALUE));
            else if (BrowserResultWriter.USER_AGENT.equals(next.getAttributeValue(BrowserResultWriter.PROPERTY_KEY)))
                result.setUserAgent(next.getAttributeValue(BrowserResultWriter.PROPERTY_VALUE));
            else if (BrowserResultWriter.REMOTE_ADDRESS.equals(next.getAttributeValue(BrowserResultWriter.PROPERTY_KEY)))
                result.setRemoteAddress(next.getAttributeValue(BrowserResultWriter.PROPERTY_VALUE));
            else if (BrowserResultWriter.BASE_URL.equals(next.getAttributeValue(BrowserResultWriter.PROPERTY_KEY)))
                result.setBaseURL(next.getAttributeValue(BrowserResultWriter.PROPERTY_VALUE));
        }
    }

    private void updateWithTestCaseResults(List<Element> testCaseElements, BrowserResult result) {
        TestCaseResultBuilder testCaseBuilder = new TestCaseResultBuilder();
        for (Element element : testCaseElements) {
            result.addTestCaseResult(testCaseBuilder.build(element));
        }
    }
}
