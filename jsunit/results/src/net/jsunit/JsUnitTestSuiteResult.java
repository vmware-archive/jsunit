package net.jsunit;
import java.util.*;
import javax.servlet.http.*;
import org.jdom.*;
import org.jdom.output.*;
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
public class JsUnitTestSuiteResult {
	protected String id, jsUnitVersion, userAgent;
	protected List testCaseResults = new ArrayList();
	protected double time;
	public static final String TEST_ID = "testId", USER_AGENT = "userAgent", TIME = "time", TEST_CASES = "testCases", JSUNIT_VERSION = "jsUnitVersion";
	public JsUnitTestSuiteResult() {
		this.id = "" + System.currentTimeMillis();
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		if (id != null)
			this.id = id;
	}
	public boolean hasId(String id) {
		return this.id.equals(id);
	}
	public String getJsUnitVersion() {
		return jsUnitVersion;
	}
	public void setJsUnitVersion(String jsUnitVersion) {
		this.jsUnitVersion = jsUnitVersion;
	}
	public String getUserAgent() {
		return userAgent;
	}
	public void setUserAgent(String userAgent) {
		this.userAgent = userAgent;
	}
	public double getTime() {
		return time;
	}
	public void setTime(double time) {
		this.time = time;
	}
	public List getTestCaseResults() {
		return testCaseResults;
	}
	public void setTestCaseStrings(String[] testCaseResultStrings) {
		buildTestCaseResults(testCaseResultStrings);
	}
	public static JsUnitTestSuiteResult fromRequest(HttpServletRequest request) {
		JsUnitTestSuiteResult result = new JsUnitTestSuiteResult();
		String testId = request.getParameter(TEST_ID);
		if (!JsUnitUtility.isEmpty(testId))
			result.setId(testId);
		result.setUserAgent(request.getParameter(USER_AGENT));
		String time = request.getParameter(TIME);
		if (!JsUnitUtility.isEmpty(time))
			result.setTime(Double.parseDouble(time));
		result.setJsUnitVersion(request.getParameter(JSUNIT_VERSION));
		result.setTestCaseStrings(request.getParameterValues(TEST_CASES));
		return result;
	}
	protected void buildTestCaseResults(String[] testCaseResultStrings) {
		if (testCaseResultStrings == null)
			return;
		for (int i = 0; i < testCaseResultStrings.length; i++)
			testCaseResults.add(JsUnitTestCaseResult.fromString(testCaseResultStrings[i]));
	}
	public int errorCount() {
		int result = 0;
		Iterator it = testCaseResults.iterator();
		while (it.hasNext()) {
			JsUnitTestCaseResult next = (JsUnitTestCaseResult) it.next();
			if (next.hadError())
				result++;
		}
		return result;
	}
	public int failureCount() {
		int result = 0;
		Iterator it = testCaseResults.iterator();
		while (it.hasNext()) {
			JsUnitTestCaseResult next = (JsUnitTestCaseResult) it.next();
			if (next.hadFailure())
				result++;
		}
		return result;
	}
	public int count() {
		return testCaseResults.size();
	}
	public String writeXml() {
		Element root = createRootElement();
		Document document = new Document(root);
		return new XMLOutputter().outputString(document);
	}
	public String writeXmlFragment() {
		return new XMLOutputter().outputString(createRootElement());
	}
	protected Element createRootElement() {
		Element result = new Element("testsuite");
		result.setAttribute("id", id);
		result.setAttribute("errors", "" + errorCount());
		result.setAttribute("failures", "" + failureCount());
		result.setAttribute("name", "JsUnitTest");
		result.setAttribute("tests", "" + count());
		result.setAttribute("time", "" + getTime());
		addPropertiesElementTo(result);
		addTestResultElementsTo(result);
		return result;
	}
	protected void addPropertiesElementTo(Element element) {
		Element properties = new Element("properties");
		element.addContent(properties);
		Element jsUnitVersionProperty = new Element("property");
		jsUnitVersionProperty.setAttribute("name", "JsUnitVersion");
		jsUnitVersionProperty.setAttribute("value", jsUnitVersion == null ? "" : jsUnitVersion);
		properties.addContent(jsUnitVersionProperty);
		Element userAgentProperty = new Element("property");
		userAgentProperty.setAttribute("name", "userAgent");
		userAgentProperty.setAttribute("value", userAgent == null ? "" : userAgent);
		properties.addContent(userAgentProperty);
	}
	protected void addTestResultElementsTo(Element element) {
		Iterator it = testCaseResults.iterator();
		while (it.hasNext()) {
			JsUnitTestCaseResult next = (JsUnitTestCaseResult) it.next();
			next.addXmlTo(element);
		}
	}
}
