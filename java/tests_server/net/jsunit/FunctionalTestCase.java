package net.jsunit;

import com.meterware.httpunit.HttpUnitOptions;
import junit.framework.TestCase;
import net.jsunit.model.BrowserResultWriter;
import net.jsunit.model.ResultType;
import net.sourceforge.jwebunit.WebTester;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.util.List;

public abstract class FunctionalTestCase extends TestCase {

    static {
        HttpUnitOptions.setScriptingEnabled(false);
    }

    protected WebTester webTester;

    protected void createWebTester() {
        webTester = new WebTester();
        webTester.getTestContext().setBaseUrl(baseURL());
    }

    protected abstract String baseURL();

    protected abstract int port();

    protected Document responseXmlDocument() throws JDOMException, IOException {
        String responseXml = webTester.getDialog().getResponseText();
        SAXBuilder saxBuilder = new SAXBuilder("org.apache.xerces.parsers.SAXParser");
        Reader stringReader = new StringReader(responseXml);
        return saxBuilder.build(stringReader);
    }

    protected void assertConfigXml() throws JDOMException, IOException {
        Document result = responseXmlDocument();
        Element root = result.getRootElement();
        assertEquals("configuration", root.getName());
    }

    protected void assertErrorResponse(Element rootElement, String message) {
        assertEquals("error", rootElement.getName());
        assertEquals(message, rootElement.getText());
    }

    protected void assertRunResult(Document result, ResultType expectedResultType, String expectedUrl, int expectedBrowserResultCount) {
        Element root = result.getRootElement();
        assertEquals("testRunResult", root.getName());
        assertEquals(expectedBrowserResultCount, root.getChildren("browserResult").size());
        assertEquals(expectedResultType.name(), root.getAttribute("type").getValue());
        Element urlProperty = urlPropertyElement(root);
        if (expectedUrl != null)
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
