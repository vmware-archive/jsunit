package net.jsunit;
import java.util.StringTokenizer;

import org.jdom.Element;

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
public class TestCaseResult {
	public static final String DELIMITER = "|", ERROR_INDICATOR = "E", FAILURE_INDICATOR = "F";
	private String name;
	private double time;
	private String failure, error;
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getError() {
		return error;
	}
	public void setError(String error) {
		this.error = error;
	}
	public String getFailure() {
		return failure;
	}
	public void setFailure(String failure) {
		this.failure = failure;
	}
	public double getTime() {
		return time;
	}
	public void setTime(double time) {
		this.time = time;
	}
	public boolean hadError() {
		return error != null;
	}
	public boolean hadFailure() {
		return failure != null;
	}
	public boolean hadSuccess() {
		return !hadError() && !hadFailure();
	}
	public static TestCaseResult fromString(String string) {
		TestCaseResult result = new TestCaseResult();
		StringTokenizer toker = new StringTokenizer(string, DELIMITER);
		result.setName(toker.nextToken());
		result.setTime(Double.parseDouble(toker.nextToken()));
		String successString = toker.nextToken();
		if (successString.equals(ERROR_INDICATOR))
			result.setError(toker.nextToken());
		else if (successString.equals(FAILURE_INDICATOR))
			result.setFailure(toker.nextToken());
		return result;
	}
	public static TestCaseResult fromXmlElement(Element testCaseElement) {
		return new TestCaseResultBuilder().build(testCaseElement);
	}
	public String writeXmlFragment() {
		return new TestCaseResultWriter(this).writeXmlFragment();
	}
}
