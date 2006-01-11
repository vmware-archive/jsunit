package net.jsunit.action;

import net.jsunit.BrowserTestRunner;

import com.opensymphony.xwork.Action;

public abstract class JsUnitAction implements Action, XmlProducer {

	protected BrowserTestRunner runner;

	public void setBrowserTestRunner(BrowserTestRunner runner) {
		this.runner = runner;
	}
	
	public BrowserTestRunner getBrowserTestRunner() {
		return runner;
	}
	
}
