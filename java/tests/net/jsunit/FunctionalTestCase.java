package net.jsunit;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;

import org.jdom.Document;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;

import junit.framework.TestCase;
import net.jsunit.configuration.Configuration;
import net.jsunit.configuration.ConfigurationSource;
import net.sourceforge.jwebunit.WebTester;

public abstract class FunctionalTestCase extends TestCase {

	protected JsUnitServer server;
	protected WebTester webTester;

	public void setUp() throws Exception {
		super.setUp();
		Configuration configuration = new Configuration(new TestConfigurationSource());
		server = new JsUnitServer(configuration);
		server.start();
		webTester = new WebTester();
		webTester.getTestContext().setBaseUrl("http://localhost:8085/jsunit");		
	}
	
	public void tearDown() throws Exception {
		server.dispose();
		super.tearDown();
	}
	
	protected Document responseXmlDocument() throws JDOMException, IOException {
		String responseXml = webTester.getDialog().getResponseText();
		SAXBuilder saxBuilder = new SAXBuilder("org.apache.xerces.parsers.SAXParser");
		Reader stringReader = new StringReader(responseXml);
        return saxBuilder.build(stringReader);
	}

	static class TestConfigurationSource implements ConfigurationSource {

		public String resourceBase() {
			return "c:\\jsunit";
		}

		public String port() {
			return "8085";
		}

		public String logsDirectory() {
			return null;
		}

		public String browserFileNames() {
			return null;
		}

		public String url() {
			return "http://localhost:8085/jsunit/";
		}

		public String closeBrowsersAfterTestRuns() {
			return String.valueOf(Boolean.TRUE);
		}

		public String logStatus() {
			return String.valueOf(false);
		}

		public String timeoutSeconds() {
			return "60";
		}
		
	}

}
