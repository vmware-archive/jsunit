package net.jsunit;
import java.util.*;
import junit.framework.*;
public class JsUnitStandaloneTest extends TestCase {
	private Process ie, netscape;
	public final static String ieExecutable = "\"c:\\program files\\internet explorer\\iexplore.exe\"";
	public final static String netscapeExecutable = "\"c:\\program files\\netscape\\netscape\\Netscp.exe\"";
	public final static String url = "file:///c:/jsunit/jsunit/testRunner.html?testPage=c:\\jsunit\\jsunit\\tests\\jsUnitTestSuite.html&autoRun=true&submitResults=true";
	
	public void setUp() throws Exception {
		super.setUp();
		JsUnitResultAcceptor.startServer();
		JsUnitTestSuiteResult.setLogsDirectory("c:\\jsunit\\jsunit\\results\\logs\\");
	}
	public void tearDown() throws Exception {
		super.tearDown();
		if (ie != null)
			ie.destroy();
		if (netscape != null)
			netscape.destroy();
		JsUnitResultAcceptor.stopServer();
	}
	public void testExample() throws Exception {
		ie = Runtime.getRuntime().exec(ieExecutable + " " + url);
		netscape = Runtime.getRuntime().exec(netscapeExecutable + " " + url);
		JsUnitResultAcceptor acceptor = JsUnitResultAcceptor.instance();
		long timeWaited = 0;
		while (acceptor.getResults().size() != 2)
			Thread.sleep(1000);
		Iterator it = acceptor.getResults().iterator();
		while (it.hasNext()) {
			JsUnitTestSuiteResult result = (JsUnitTestSuiteResult) it.next();
			if (!result.hadSuccess()) {
				fail("Result with ID " + result.getId() + " failed");
			}
		}
	}
}
