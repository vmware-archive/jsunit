package net.jsunit.test;

import java.io.File;
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
public class ResultAcceptorTest extends JsUnitTest {
	protected Map requestMap;
	public ResultAcceptorTest(String name) {
		super(name);
	}
	public void setUp() throws Exception {
		super.setUp();
		requestMap = new HashMap();
		requestMap.put(TestSuiteResultWriter.ID, "ID_foo");
		requestMap.put(TestSuiteResultWriter.USER_AGENT, "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1)");
		requestMap.put(TestSuiteResultWriter.TIME, "4.3");
		requestMap.put(TestSuiteResultWriter.JSUNIT_VERSION, "2.5");
		requestMap.put(TestSuiteResultWriter.TEST_CASES, dummyTestCaseStrings());
	}
	public void tearDown() throws Exception {
		File logFile = TestSuiteResult.logFileForId("ID_foo");
		if (logFile.exists())
			logFile.delete();
		super.tearDown();
	}
	protected String[] dummyTestCaseStrings() {
		return new String[] { "testFoo|1.3|S||", "testFoo|1.3|E|Error Message|", "testFoo|1.3|F|Failure Message|" };
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
		assertEquals(1, acceptor.getResults().size());
	}
	public void testSubmittedResultHeaders() {
		submit();
		TestSuiteResult result = (TestSuiteResult) acceptor.getResults().get(0);
		assertEquals("ID_foo", result.getId());
		assertEquals("Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1)", result.getUserAgent());
		assertEquals("2.5", result.getJsUnitVersion());
		assertEquals(1, result.errorCount());
		assertEquals(1, result.failureCount());
		assertEquals(3, result.count());
		assertEquals(4.3d, result.getTime(), .001d);
	}
	public void testSubmittedTestCaseResults() {
		submit();
		TestSuiteResult result = (TestSuiteResult) acceptor.getResults().get(0);
		assertEquals(3, result.getTestCaseResults().size());
	}
	public void testFindResultById() {
		assertNull(acceptor.findResultWithId("ID_foo"));
		submit();
		assertNotNull(acceptor.findResultWithId("ID_foo"));
		assertNull(acceptor.findResultWithId("Invalid ID"));
		acceptor.clearResults();
		//should look on disk when not in memory
		assertNotNull(acceptor.findResultWithId("ID_foo"));
		assertNull(acceptor.findResultWithId("Invalid ID"));
	}
	public void testLog() {
		File logFile = TestSuiteResult.logFileForId("ID_foo");
		assertFalse(logFile.exists());
		submit();
		assertTrue(logFile.exists());
	}
}
