package net.jsunit;
import java.util.*;
import junit.framework.*;
public abstract class JsUnitStandaloneTest extends TestCase {
	protected JsUnitResultAcceptor acceptor = JsUnitResultAcceptor.instance();
	protected List browserProcesses = new ArrayList();
	protected abstract List browserFileNames();
	protected abstract String url();
	protected int maxSecondsToWait() {
		return 2 * 60;
	}
	public void setUp() throws Exception {
		super.setUp();
		JsUnitResultAcceptor.startServer();
	}
	public void tearDown() throws Exception {
		super.tearDown();
		Iterator it = browserProcesses.iterator();
		while (it.hasNext()) {
			Process next = (Process) it.next();
			next.destroy();
		}
		JsUnitResultAcceptor.stopServer();
	}
	public void testStandaloneRun() throws Exception {
		Iterator it = browserFileNames().iterator();
		while (it.hasNext()) {
			String next = (String) it.next();
			browserProcesses.add(Runtime.getRuntime().exec("\"" + next + "\" \"" + url()+"\""));
		}
		waitForResultsToBeSubmitted();
		verifyResults();
	}
	protected void waitForResultsToBeSubmitted() throws Exception {
		long secondsWaited = 0;
		while (acceptor.getResults().size() != browserFileNames().size()) {
			Thread.sleep(1000);
			secondsWaited += 1;
			if (secondsWaited > maxSecondsToWait())
				fail("Waited more than " + maxSecondsToWait() + " seconds");
		}
	}
	protected void verifyResults() {
		Iterator it = acceptor.getResults().iterator();
		while (it.hasNext()) {
			JsUnitTestSuiteResult result = (JsUnitTestSuiteResult) it.next();
			if (!result.hadSuccess()) {
				fail("Result with ID " + result.getId() + " failed");
			}
		}
	}
}
