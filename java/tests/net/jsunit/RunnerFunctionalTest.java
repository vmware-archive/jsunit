package net.jsunit;

import java.net.URLEncoder;
import java.util.List;

import net.jsunit.model.BrowserResultWriter;
import net.jsunit.model.ResultType;

import org.jdom.Document;
import org.jdom.Element;

public class RunnerFunctionalTest extends ServerFunctionalTestCase {

	public void testSimple() throws Exception {
		webTester.beginAt("runner");
		Document result = responseXmlDocument();
		assertResult(
			result, 
			ResultType.SUCCESS, 
			"http://localhost:"+PORT+"/jsunit/tests/jsUnitUtilityTests.html"
		);
	}

	public void testOverrideUrl() throws Exception {
		webTester.beginAt("runner?url="+URLEncoder.encode("http://127.0.0.1:"+PORT+"/jsunit/testRunner.html?testPage=http://127.0.0.1:"+PORT+"/jsunit/tests/jsUnitUtilityTests.html&autoRun=true&submitresults=true", "UTF-8"));
		Document result = responseXmlDocument();
		assertResult(
			result, 
			ResultType.SUCCESS, 
			"http://127.0.0.1:"+PORT+"/jsunit/tests/jsUnitUtilityTests.html"
		);
	}

	private void assertResult(Document result, ResultType expectedResultType, String expectedUrl) {
		Element root = result.getRootElement();
		assertEquals("testRunResult", root.getName());
		assertEquals(ResultType.SUCCESS.name(), root.getAttribute("type").getValue());
		Element urlProperty = urlPropertyElement(root);
		assertEquals(expectedUrl, urlProperty.getAttribute(BrowserResultWriter.PROPERTY_VALUE).getValue());
		
	}
	
	@SuppressWarnings("unchecked")
	private Element urlPropertyElement(Element root) {
		List<Element> children = root.getChild("browserResult").getChild(BrowserResultWriter.PROPERTIES).getChildren(BrowserResultWriter.PROPERTY);
		for (Element child : children){
			if (child.getAttribute(BrowserResultWriter.PROPERTY_KEY).getValue().equals(BrowserResultWriter.URL))
				return child;
		}
		return null;
	}
	
}
