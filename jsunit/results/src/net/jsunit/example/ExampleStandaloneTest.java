package net.jsunit.example;
import java.util.*;
import net.jsunit.*;
public class ExampleStandaloneTest extends JsUnitStandaloneTest {
	public void setUp() throws Exception {
		super.setUp();
		JsUnitTestSuiteResult.setLogsDirectory("c:\\jsunit\\jsunit\\results\\logs\\");
	}
	protected List browserFileNames() {
		return Arrays.asList(new String[] {
			"c:\\program files\\internet explorer\\iexplore.exe", 
			"c:\\program files\\netscape\\netscape\\Netscp.exe" 
		});
	}
	protected String url() {
		return "file:///c:/jsunit/jsunit/testRunner.html?testPage=c:\\jsunit\\jsunit\\tests\\jsUnitTestSuite.html&autoRun=true&submitResults=true";
	}
}
