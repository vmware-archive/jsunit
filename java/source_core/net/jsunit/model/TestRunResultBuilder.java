package net.jsunit.model;

import org.jdom.Document;
import org.jdom.Element;

import java.net.URL;
import java.util.List;

public class TestRunResultBuilder {

	@SuppressWarnings("unchecked")
	public TestRunResult build(URL url, Document document) {
		
		BrowserResultBuilder browserBuilder = new BrowserResultBuilder();
		
		List<Element> children = document.getRootElement().getChildren();
		
		TestRunResult result = new TestRunResult(url);
		for (Element element : children) {
			BrowserResult browserResult = browserBuilder.build(element);
			result.addBrowserResult(browserResult);
		}
		
		return result;		
	}
	
}
