package net.jsunit;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;

import org.jdom.Document;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;

import net.sourceforge.jwebunit.WebTester;
import junit.framework.TestCase;

public abstract class FunctionalTestCase extends TestCase {

	public static final int PORT = 8080;
	protected WebTester webTester;

	public void setUp() throws Exception {
		super.setUp();
		webTester = new WebTester();
		webTester.getTestContext().setBaseUrl("http://localhost:" + PORT + "/jsunit");
	}
	
	protected Document responseXmlDocument() throws JDOMException, IOException {
		String responseXml = webTester.getDialog().getResponseText();
		SAXBuilder saxBuilder = new SAXBuilder("org.apache.xerces.parsers.SAXParser");
		Reader stringReader = new StringReader(responseXml);
        return saxBuilder.build(stringReader);
	}

}
