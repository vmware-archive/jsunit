package net.jsunit.test;
import java.util.*;
import javax.servlet.http.*;
import net.jsunit.*;
/**
 * @author Edward Hieatt
 * 
 * ***** BEGIN LICENSE BLOCK *****
   - Version: MPL 1.1/GPL 2.0/LGPL 2.1
   -
   - The contents of this file are subject to the Mozilla Public License Version
   - 1.1 (the "License"); you may not use this file except in compliance with
   - the License. You may obtain a copy of the License at
   - http://www.mozilla.org/MPL/
   -
   - Software distributed under the License is distributed on an "AS IS" basis,
   - WITHOUT WARRANTY OF ANY KIND, either express or implied. See the License
   - for the specific language governing rights and limitations under the
   - License.
   -
   - The Original Code is Edward Hieatt code.
   -
   - The Initial Developer of the Original Code is
   - Edward Hieatt, edward@jsunit.net.
   - Portions created by the Initial Developer are Copyright (C) 2003
   - the Initial Developer. All Rights Reserved.
   -
   - Author Edward Hieatt, edward@jsunit.net
   -
   - Alternatively, the contents of this file may be used under the terms of
   - either the GNU General Public License Version 2 or later (the "GPL"), or
   - the GNU Lesser General Public License Version 2.1 or later (the "LGPL"),
   - in which case the provisions of the GPL or the LGPL are applicable instead
   - of those above. If you wish to allow use of your version of this file only
   - under the terms of either the GPL or the LGPL, and not to allow others to
   - use your version of this file under the terms of the MPL, indicate your
   - decision by deleting the provisions above and replace them with the notice
   - and other provisions required by the LGPL or the GPL. If you do not delete
   - the provisions above, a recipient may use your version of this file under
   - the terms of any one of the MPL, the GPL or the LGPL.
   -
   - ***** END LICENSE BLOCK *****
   
   @author Edward Hieatt
 */
public class JsUnitResultAcceptorTest extends JsUnitTest {
	protected Map requestMap;
	public JsUnitResultAcceptorTest(String name) {
		super(name);
	}
	public void setUp() throws Exception {
		super.setUp();
		requestMap = new HashMap();
		requestMap.put(JsUnitTestSuiteResult.TEST_ID, "ID foo");		
		requestMap.put(JsUnitTestSuiteResult.OS, "Windows 2000");
		requestMap.put(JsUnitTestSuiteResult.BROWSER, "IE 6.0");
		requestMap.put(JsUnitTestSuiteResult.TIME, "4.3");
		requestMap.put(JsUnitTestSuiteResult.JSUNIT_VERSION, "2.5");
		requestMap.put(JsUnitTestSuiteResult.TEST_CASES, dummyTestCaseStrings());
	}
	protected String[] dummyTestCaseStrings() {
		return new String[] {
			"testFoo|1.3|S||",
			"testFoo|1.3|E|Error Message|",
			"testFoo|1.3|F|Failure Message|"
		};
	}
	protected void submit() {
		HttpServletRequest request = new DummyHttpRequest(requestMap);
		acceptor.accept(request);		
	}
	public void testSubmitResults() {
		assertEquals(0, acceptor.getResults().size());
		submit();
		assertEquals(1, acceptor.getResults().size());
		submit();
		assertEquals(2, acceptor.getResults().size());
	}
	public void testSubmittedResultHeaders() {
		submit();
		JsUnitTestSuiteResult result = (JsUnitTestSuiteResult) acceptor.getResults().get(0);
		assertEquals("ID foo", result.getId());		
		assertEquals("Windows 2000", result.getOs());
		assertEquals("IE 6.0", result.getBrowser());
		assertEquals("2.5", result.getJsUnitVersion());
		assertEquals(1, result.errorCount());
		assertEquals(1, result.failureCount());
		assertEquals(3, result.count());
		assertEquals(4.3d, result.getTime(), .001d);
	}
	public void testSubmittedTestCaseResults() {
		submit();
		JsUnitTestSuiteResult result = (JsUnitTestSuiteResult) acceptor.getResults().get(0);
		assertEquals(3, result.getTestCaseResults().size());
	}
	public void testFindResultById() {
		submit();
		assertNotNull(acceptor.findResultWithId("ID foo"));
		assertNull(acceptor.findResultWithId("Invalid ID"));
	}
}
