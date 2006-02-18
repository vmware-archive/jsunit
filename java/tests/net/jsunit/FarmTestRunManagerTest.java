package net.jsunit;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.jdom.Document;

import net.jsunit.configuration.Configuration;
import net.jsunit.model.BrowserResult;
import net.jsunit.model.TestRunResult;

import junit.framework.TestCase;

public class FarmTestRunManagerTest extends TestCase {

	private Configuration configuration;

	public void setUp() throws Exception {
		super.setUp();
		configuration = new Configuration(new DummyConfigurationSource());
	}
	
	public void testSimple() {
		FarmTestRunManager manager = new FarmTestRunManager(new MockRemoteServerHitter(), configuration.getRemoteMachineURLs());
		manager.runTests();
		TestRunResult result = manager.getTestRunResult();
		
		TestRunResult expectedResult = new TestRunResult();
		expectedResult.mergeWith(createResult1());
		expectedResult.mergeWith(createResult2());
		
		assertEquals(Utility.asString(expectedResult.asXml()), Utility.asString(result.asXml()));
	}
	
	public void xtestTimeout() {
/*		FarmTestRunManager manager = new FarmTestRunManager(new TimingOutRemoteServerHitter(), configuration);
		manager.runTests();
		TestRunResult result = manager.getTestRunResult();
		
		TestRunResult expectedResult = new TestRunResult();
		expectedResult.mergeWith(createResult1());
		expectedResult.mergeWith(createResult2());
*/	}
	
	private TestRunResult createResult1() {
		TestRunResult result = new TestRunResult();
		BrowserResult browserResult1 = new BrowserResult();
		browserResult1.setId("1");
		browserResult1.setBrowserFileName("mybrowser1.exe");
		browserResult1.setTime(123.45);
		result.addBrowserResult(browserResult1);

		BrowserResult browserResult2 = new BrowserResult();
		browserResult2.setId("2");
		browserResult2.setBrowserFileName("mybrowser2.exe");
		browserResult2.setFailedToLaunch();
		result.addBrowserResult(browserResult2);
		
		return result;
	}
	
	private TestRunResult createResult2() {
		TestRunResult result = new TestRunResult();
		BrowserResult browserResult1 = new BrowserResult();
		browserResult1.setBrowserFileName("mybrowser3.exe");
		browserResult1.setId("a");
		browserResult1.setTime(123.45);
		browserResult1.setBaseURL("http://www.example.com");
		browserResult1.setId("12345");
		browserResult1.setUserAgent("foo bar");
		result.addBrowserResult(browserResult1);

		BrowserResult browserResult2 = new BrowserResult();
		browserResult1.setId("b");
		browserResult2.setBrowserFileName("mybrowser4.exe");
		browserResult2.setTimedOut();
		result.addBrowserResult(browserResult2);
		
		return result;
	}
	
	class MockRemoteServerHitter implements RemoteRunnerHitter {

		private List<TestRunResult> results;
		
		private int index = 0;
		
		public MockRemoteServerHitter() {
			results = new ArrayList<TestRunResult>();
			results.add(createResult1());
			results.add(createResult2());
		}
		
		public Document hitRemoteRunner(URL url) {
			return new Document(results.get(index++).asXml());
		}
		
	}

}
