package net.jsunit;
import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

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
public class TestSuiteResult {
	public static final String LOGS_DIRECTORY = "logs";
	private String remoteAddress, id, jsUnitVersion, userAgent;
	private List testCaseResults = new ArrayList();
	private double time;
	
	public TestSuiteResult() {
		this.id = String.valueOf(System.currentTimeMillis());
	}
	public static File logFileForId(String id) {
		return new File(LOGS_DIRECTORY + File.separator + id + ".xml");
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
	public static TestSuiteResult fromRequest(HttpServletRequest request) {
		TestSuiteResult result = new TestSuiteResult();
		String testId = request.getParameter(TestSuiteResultWriter.ID);
		if (!Utility.isEmpty(testId))
			result.setId(testId);
		result.setRemoteAddress(request.getRemoteAddr());
		result.setUserAgent(request.getParameter(TestSuiteResultWriter.USER_AGENT));
		String time = request.getParameter(TestSuiteResultWriter.TIME);
		if (!Utility.isEmpty(time))
			result.setTime(Double.parseDouble(time));
		result.setJsUnitVersion(request.getParameter(TestSuiteResultWriter.JSUNIT_VERSION));
		result.setTestCaseStrings(request.getParameterValues(TestSuiteResultWriter.TEST_CASES));
		return result;
	}
	public String getRemoteAddress() {
		return remoteAddress;
	}
	public void setRemoteAddress(String remoteAddress) {
		this.remoteAddress = remoteAddress;
	}
	private void buildTestCaseResults(String[] testCaseResultStrings) {
		if (testCaseResultStrings == null)
			return;
		for (int i = 0; i < testCaseResultStrings.length; i++)
			testCaseResults.add(TestCaseResult.fromString(testCaseResultStrings[i]));
	}
	public static TestSuiteResult findResultWithIdInResultLogs(String id) {
		File logFile = logFileForId(id);
		if (logFile.exists())
			return fromXmlFile(logFile);
		return null;
	}
	public int errorCount() {
		int result = 0;
		Iterator it = testCaseResults.iterator();
		while (it.hasNext()) {
			TestCaseResult next = (TestCaseResult) it.next();
			if (next.hadError())
				result++;
		}
		return result;
	}
	public int failureCount() {
		int result = 0;
		Iterator it = testCaseResults.iterator();
		while (it.hasNext()) {
			TestCaseResult next = (TestCaseResult) it.next();
			if (next.hadFailure())
				result++;
		}
		return result;
	}
	public int count() {
		return testCaseResults.size();
	}
	public String writeXml() {
		return new TestSuiteResultWriter(this).writeXml();
	}
	public String writeXmlFragment() {
		return new TestSuiteResultWriter(this).writeXmlFragment();
	}
	public void writeLog() {
		String xml = writeXml();
		Utility.writeFile(xml, logFileForId(getId()));
	}
	public boolean hadSuccess() {
		Iterator it = testCaseResults.iterator();
		while (it.hasNext()) {
			TestCaseResult next = (TestCaseResult) it.next();
			if (!next.hadSuccess())
				return false;
		}
		return true;
	}
	public static TestSuiteResult fromXmlFile(File aFile) {
		return new TestSuiteResultBuilder().build(aFile);
	}
	public void addTestCaseResult(TestCaseResult result) {
		testCaseResults.add(result);
	}
}
