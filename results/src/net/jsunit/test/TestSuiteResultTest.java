package net.jsunit.test;
import java.io.File;
import java.util.Iterator;

import net.jsunit.TestCaseResult;
import net.jsunit.TestSuiteResult;
import net.jsunit.Utility;
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
public class TestSuiteResultTest extends JsUnitTest {
	private TestSuiteResult result;
	private String expectedXmlFragment =
		"<testsuite id=\"An ID\" errors=\"1\" failures=\"1\" name=\"JsUnitTest\" tests=\"3\" time=\"4.3\">"
			+ "<properties>"
			+ "<property name=\"JsUnitVersion\" value=\"2.5\" />"
			+ "<property name=\"userAgent\" value=\"Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1)\" />"
			+ "<property name=\"remoteAddress\" value=\"Dummy Remote Address\" />"
			+ "</properties>"
			+ "<testcase name=\"testFoo\" time=\"1.3\" />"
			+ "<testcase name=\"testFoo\" time=\"1.3\">"
			+ "<error message=\"Test Error Message\" />"
			+ "</testcase>"
			+ "<testcase name=\"testFoo\" time=\"1.3\">"
			+ "<failure message=\"Test Failure Message\" />"
			+ "</testcase>"
			+ "</testsuite>";
	private String xmlHeader = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>";
	public TestSuiteResultTest(String name) {
		super(name);
	}
	public void setUp() throws Exception {
		super.setUp();
		result = new TestSuiteResult();
		result.setJsUnitVersion("2.5");
		result.setId("An ID");
		result.setUserAgent("Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1)");
		result.setRemoteAddress("Dummy Remote Address");
		result.setTime(4.3);
		result.setTestCaseStrings(new String[] { "testFoo|1.3|S||", "testFoo|1.3|E|Test Error Message|", "testFoo|1.3|F|Test Failure Message|" });
	}
	public void testId() {
		assertNotNull(result.getId());
		result = new TestSuiteResult();
		result.setId("foo");
		assertEquals("foo", result.getId());
	}
	public void testFields() {
		assertFields(result);
	}
	private void assertFields(TestSuiteResult aResult) {
		assertEquals("2.5", aResult.getJsUnitVersion());
		assertEquals("An ID", aResult.getId());
		assertEquals("Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1)", aResult.getUserAgent());
		assertEquals("Dummy Remote Address", aResult.getRemoteAddress());
		assertEquals(4.3d, aResult.getTime(), 0.001d);
		assertEquals(3, aResult.getTestCaseResults().size());
		Iterator it = aResult.getTestCaseResults().iterator();
		while (it.hasNext()) {
			TestCaseResult next = (TestCaseResult) it.next();
			assertNotNull(next);
		}
	}
	public void testXml() {
		assertEquals(expectedXmlFragment, result.writeXmlFragment());
	}
	public void testSuccess() {
		assertFalse(result.hadSuccess());
	}
	public void testBuildFromXml() {
		Utility.writeFile(result.writeXml(), "resultXml.xml");
		File file = new File("resultXml.xml");
		TestSuiteResult reconstitutedResult = TestSuiteResult.fromXmlFile(file);
		assertFields(reconstitutedResult);
		file.delete();
	}
}
