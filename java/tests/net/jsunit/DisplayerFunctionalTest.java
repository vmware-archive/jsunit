package net.jsunit;


import net.jsunit.model.BrowserResult;

import org.jdom.Document;
import org.jdom.Element;

public class DisplayerFunctionalTest extends ServerFunctionalTestCase {

	public void testNoId() throws Exception {
		webTester.beginAt("displayer");
		Document responseDocument = responseXmlDocument();

		Element rootElement = responseDocument.getRootElement();
		assertEquals("error", rootElement.getName());
		assertEquals("No ID given", rootElement.getText());
	}

	public void testInvalidId() throws Exception {
		String id = String.valueOf(System.currentTimeMillis());
		webTester.beginAt("displayer?id="+id);
		Document responseDocument = responseXmlDocument();

		Element rootElement = responseDocument.getRootElement();
		assertEquals("error", rootElement.getName());
		assertEquals("No Test Result has been submitted with ID "+id, rootElement.getText());
	}
	
	public void testValid() throws Exception {
		BrowserResult browserResult = new BrowserResult();
		String id = String.valueOf(System.currentTimeMillis());
		browserResult.setId(id);
		server.accept(browserResult);
		webTester.beginAt("displayer?id="+id);
		assertEquals(Utility.asString(new Document(browserResult.asXml())), webTester.getDialog().getResponseText());
	}
	
}