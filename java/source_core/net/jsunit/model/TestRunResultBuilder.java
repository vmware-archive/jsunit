package net.jsunit.model;

import java.util.List;

import org.jdom.Document;
import org.jdom.Element;

public class TestRunResultBuilder {

	@SuppressWarnings("unchecked")
	public TestRunResult build(Document document) {
		
		BrowserResultBuilder browserBuilder = new BrowserResultBuilder();
		
		List<Element> children = document.getRootElement().getChildren();
		
		TestRunResult result = new TestRunResult();
		for (Element element : children) {
			BrowserResult browserResult = browserBuilder.build(element);
			result.addBrowserResult(browserResult);
		}
		
		return result;		
	}
	
}
