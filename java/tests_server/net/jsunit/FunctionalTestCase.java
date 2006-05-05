package net.jsunit;

import com.meterware.httpunit.HttpUnitOptions;
import junit.framework.TestCase;
import net.jsunit.configuration.Configuration;
import net.jsunit.configuration.ConfigurationSource;
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
    protected JsUnitServer server;
    protected int port;
    protected Configuration configuration;

    public void setUp() throws Exception {
        super.setUp();
        port = new TestPortManager().newPort();
        configuration = new Configuration(createConfigurationSource());
        server = createServer();
        server.start();
        webTester = new WebTester();
        webTester.getTestContext().setBaseUrl(baseURL());
    }

    protected abstract JsUnitServer createServer();

    protected ConfigurationSource createConfigurationSource() {
        return new FunctionalTestConfigurationSource(port);
    }

    protected String baseURL() {
        return "http://localhost:" + webTesterPort() + "/jsunit";
    }

    protected int webTesterPort() {
        return port;
    }

    public void tearDown() throws Exception {
        if (server != null)
            server.dispose();
        super.tearDown();
    }

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

    protected void assertOnLogDisplayerPage() {
        webTester.assertTitleEquals("JsUnit Server - LogDisplayer");
    }

    protected void assertOnUrlRunnerPage() {
        webTester.assertTitleEquals("JsUnit Server - URLRunner");
    }

    protected void assertOnUploadRunnerPage() {
        webTester.assertTitleEquals("JsUnit Server - UploadRunner");
    }

    protected void assertOnFragmentRunnerPage() {
        webTester.assertTitleEquals("JsUnit Server - FragmentRunner");
    }
}
