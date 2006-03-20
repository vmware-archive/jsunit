package net.jsunit;

import com.meterware.httpunit.HttpUnitOptions;
import junit.framework.TestCase;
import net.jsunit.configuration.Configuration;
import net.sourceforge.jwebunit.WebTester;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;

public abstract class FunctionalTestCase extends TestCase {

    static {
        HttpUnitOptions.setScriptingEnabled(false);
    }

    protected WebTester webTester;
    protected JsUnitStandardServer server;
    protected int port;

    public void setUp() throws Exception {
        super.setUp();
        port = new TestPortManager().newPort();
        Configuration configuration = new Configuration(new FunctionalTestConfigurationSource(port));
        server = new JsUnitStandardServer(configuration, new MockBrowserResultRepository(), true);
        if (shouldMockOutProcessStarter())
            server.setProcessStarter(new MockProcessStarter());
        server.start();
        webTester = new WebTester();
        webTester.getTestContext().setBaseUrl("http://localhost:" + webTesterPort() + "/jsunit");
    }

    protected boolean shouldMockOutProcessStarter() {
        return true;
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
}
