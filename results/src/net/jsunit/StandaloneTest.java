package net.jsunit;
import java.util.*;
import junit.framework.*;
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
public class StandaloneTest extends TestCase {
	private boolean shouldStartAndStopServer = true;
	public static final String PROPERTY_URL = "url";
	public static final String PROPERTY_BROWSER_FILE_NAMES = "browserFileNames";
	private JsUnitServer server = JsUnitServer.instance();
	private Properties properties;
	public StandaloneTest(String name) {
		super(name);
	}
	protected List browserFileNames() {
		return Utility.listFromCommaDelimitedString(
			properties.getProperty(PROPERTY_BROWSER_FILE_NAMES));
	}
	protected String url() {
		return properties.getProperty(PROPERTY_URL);
	}
	private int maxSecondsToWait() {
		return 2 * 60;
	}
	public void setUp() throws Exception {
		super.setUp();
		this.properties = server.jsUnitProperties();
		if (shouldStartAndStopServer)
			JsUnitServer.start();
	}
	public void tearDown() throws Exception {
		super.tearDown();
		if (shouldStartAndStopServer)
			JsUnitServer.stop();
	}
	public void testStandaloneRun() throws Exception {
		Iterator it = browserFileNames().iterator();
		while (it.hasNext()) {
			String next = (String) it.next();
			int currentResultCount = server.resultsCount();
			Process process = null;
			Utility.log("StandaloneTest: launching " + next);
			try {
				try {
					process =
						Runtime.getRuntime().exec(
							"\"" + next + "\" \"" + url() + "\"");
				} catch (Throwable t) {
					fail(
						"All browser processes should start, but the following did not: "
							+ next);
					t.printStackTrace();
				}
				waitForResultToBeSubmitted(next, currentResultCount);
				verifyLastResult();
			} finally {
				if (process != null)
					process.destroy();
			}
		}
	}
	private void waitForResultToBeSubmitted(
		String browserProcess,
		int initialResultCount)
		throws Exception {
		Utility.log(
			"StandaloneTest: waiting for "
				+ browserProcess
				+ " to submit result");
		long secondsWaited = 0;
		while (server.getResults().size() == initialResultCount) {
			Thread.sleep(1000);
			secondsWaited += 1;
			if (secondsWaited > maxSecondsToWait())
				fail(
					"Waited more than "
						+ maxSecondsToWait()
						+ " seconds for browser "
						+ browserProcess);
		}
	}
	private void verifyLastResult() {
		TestSuiteResult result = server.lastResult();
		if (!result.hadSuccess()) {
			StringBuffer buffer = new StringBuffer();
			buffer.append("Result with ID ");
			buffer.append(result.getId());
			buffer.append(" had problems: ");
			buffer.append(result.errorCount()+" errors, ");
			buffer.append(result.failureCount()+" failures ");
			fail(buffer.toString());
		}
	}
	public void setStartAndStopServer(boolean b) {
		this.shouldStartAndStopServer = b;
	}
}
