package net.jsunit.plugin.eclipse.resultsui.action;

import net.jsunit.plugin.eclipse.resultsui.ActiveTabSource;
import net.jsunit.plugin.eclipse.resultsui.TestResultsTab;

public class MockActiveTabSource implements ActiveTabSource {

	MockTestResultsTab tab;

	public MockActiveTabSource() {
		this.tab = new MockTestResultsTab(); 
	}
	
	public TestResultsTab getActiveTab() {
		return tab;
	}

}
