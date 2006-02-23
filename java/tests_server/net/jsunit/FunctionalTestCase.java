package net.jsunit;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;

import org.jdom.Document;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;

import net.jsunit.configuration.Configuration;
import net.sourceforge.jwebunit.WebTester;
import junit.framework.TestCase;

public abstract class FunctionalTestCase extends TestCase {

	protected final static int PORT = 8080;
	
	protected WebTester webTester;
	protected JsUnitServer server;
	
	public void setUp() throws Exception {
		super.setUp();
		Configuration configuration = new Configuration(new FunctionalTestConfigurationSource(PORT));
		server = new JsUnitServer(configuration);
		server.start();
		webTester = new WebTester();
		webTester.getTestContext().setBaseUrl("http://localhost:" + webTesterPort() + "/jsunit");
	}
	
	protected int webTesterPort() {
		return PORT;
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

}
