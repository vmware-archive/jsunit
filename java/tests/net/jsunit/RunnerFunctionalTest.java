package net.jsunit;

import org.jdom.Document;
import org.jdom.Element;

public class RunnerFunctionalTest extends FunctionalTestCase {

	public void testSuccess() throws Exception {
		webTester.beginAt("runner");
		Document result = responseXmlDocument();
		Element root = result.getRootElement();
		assertEquals("result", root.getName());
		assertEquals("success", root.getText());
	}

}
