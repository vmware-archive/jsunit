package net.jsunit;

import org.jdom.Document;
import org.jdom.Element;

import net.jsunit.model.BrowserResult;

public class DummyBrowserResult extends BrowserResult {

	private final boolean success;
	private final int failureCount;
	private final int errorCount;

	public DummyBrowserResult(boolean success, int failureCount, int errorCount) {
		this.success = success;
		this.failureCount = failureCount;
		this.errorCount = errorCount;
	}
	
	public boolean wasSuccessful() {
		return success;
	}
	
	public int failureCount() {
		return failureCount;
	}
	
	public int errorCount() {
		return errorCount;
	}

	public Document asXmlDocument() {
		return new Document(asXml());
	}

}
