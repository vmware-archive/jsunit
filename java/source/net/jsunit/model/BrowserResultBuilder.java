package net.jsunit.model;

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

	public BrowserResult build(Document document) {
        BrowserResult result = new BrowserResult();
        Element root = document.getRootElement();
        updateWithHeaders(result, root);
        updateWithProperties(root.getChild(BrowserResultWriter.PROPERTIES), result);
        updateWithTestCaseResults(root.getChildren(TestCaseResultWriter.TEST_CASE), result);
        return result;
    }

    private void updateWithHeaders(BrowserResult result, Element element) {
        String id = element.getAttributeValue(BrowserResultWriter.ID);
        double time = Double.parseDouble(element.getAttributeValue(BrowserResultWriter.TIME));
        result.setId(id);
        result.setTime(time);
    }

    private void updateWithProperties(Element element, BrowserResult result) {
        for (Object o : element.getChildren()) {
            Element next = (Element) o;
            if (BrowserResultWriter.JSUNIT_VERSION.equals(next.getAttributeValue(BrowserResultWriter.PROPERTY_KEY)))
                result.setJsUnitVersion(next.getAttributeValue(BrowserResultWriter.PROPERTY_VALUE));
            else
            if (BrowserResultWriter.USER_AGENT.equals(next.getAttributeValue(BrowserResultWriter.PROPERTY_KEY)))
                result.setUserAgent(next.getAttributeValue(BrowserResultWriter.PROPERTY_VALUE));
            else
            if (BrowserResultWriter.REMOTE_ADDRESS.equals(next.getAttributeValue(BrowserResultWriter.PROPERTY_KEY)))
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
