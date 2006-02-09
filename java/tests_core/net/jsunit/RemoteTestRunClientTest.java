package net.jsunit;

import net.jsunit.model.BrowserResult;
import junit.framework.TestCase;

public class RemoteTestRunClientTest extends TestCase {

	private MockTestRunListener listener;
	private RemoteTestRunClient client;

	public void setUp() throws Exception {
		super.setUp();
		listener = new MockTestRunListener();
		client = new RemoteTestRunClient(listener, -1);
	}
	
	public void testTestRunStartedMessage() {
		client.messageReceived("testRunStarted");
		assertTrue(listener.testRunStartedCalled);
	}
	
	public void testTestRunFinishedMessage() {
		client.messageReceived("testRunFinished");
		assertTrue(listener.testRunFinishedCalled);
	}
	
	public void testBrowserTestRunStartedMessage() {
		client.messageReceived("browserTestRunStarted");
		client.messageReceived("mybrowser.exe");
		assertTrue(listener.browserTestRunStartedCalled);
		assertEquals("mybrowser.exe", listener.browserFileName);
	}
	
	public void testBrowserTestRunFinishedMessage() {
		BrowserResult result = new BrowserResult();
		result.setBaseURL("http://www.example.com");
		result.setId("1234329439824");
		result.setJsUnitVersion("905.43");
		result.setRemoteAddress("http://123.45.67.89");
		result.setTime(123.45);
		result.setUserAgent("my browser version 5.6");
		result.setTestCaseStrings(new String[] {"file:///dummy/path/dummyPage.html:testFoo|1.3|S||"});
		client.messageReceived("browserTestRunFinished");
		client.messageReceived("mybrowser.exe");
		String xml = Utility.asString(result.asXmlDocument());
		String[] lines = xml.split("\r\n");
		for (String line : lines)
			client.messageReceived(line);
		client.messageReceived("endXml");
		assertTrue(listener.browserTestRunFinishedCalled);
		assertEquals("mybrowser.exe", listener.browserFileName);
		assertEquals(xml, Utility.asString(listener.result.asXmlDocument()));
	}
	
}
