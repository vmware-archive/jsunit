package net.jsunit;

import net.jsunit.model.ResultType;

import org.jdom.Document;
import org.jdom.Element;

public class RunnerFunctionalTest extends FunctionalTestCase {

	public void testSuccess() throws Exception {
		webTester.beginAt("runner");
		Document result = responseXmlDocument();
		Element root = result.getRootElement();
		assertEquals("testRunResult", root.getName());
		assertEquals(ResultType.SUCCESS.name(), root.getAttribute("type").getValue());
	}

}
