package net.jsunit.test;

import junit.framework.TestCase;
import net.jsunit.TestCaseResult;
import net.jsunit.TestCaseResultWriter;
import org.jdom.Element;

/**
 * @author Edward Hieatt
 *         <p/>
 *         ***** BEGIN LICENSE BLOCK *****
 *         - Version: MPL 1.1/GPL 2.0/LGPL 2.1
 *         -
 *         - The contents of this file are subject to the Mozilla Public License Version
 *         - 1.1 (the "License"); you may not use this file except in compliance with
 *         - the License. You may obtain a copy of the License at
 *         - http://www.mozilla.org/MPL/
 *         -
 *         - Software distributed under the License is distributed on an "AS IS" basis,
 *         - WITHOUT WARRANTY OF ANY KIND, either express or implied. See the License
 *         - for the specific language governing rights and limitations under the
 *         - License.
 *         -
 *         - The Original Code is Edward Hieatt code.
 *         -
 *         - The Initial Developer of the Original Code is
 *         - Edward Hieatt, edward@jsunit.net.
 *         - Portions created by the Initial Developer are Copyright (C) 2003
 *         - the Initial Developer. All Rights Reserved.
 *         -
 *         - Author Edward Hieatt, edward@jsunit.net
 *         -
 *         - Alternatively, the contents of this file may be used under the terms of
 *         - either the GNU General Public License Version 2 or later (the "GPL"), or
 *         - the GNU Lesser General Public License Version 2.1 or later (the "LGPL"),
 *         - in which case the provisions of the GPL or the LGPL are applicable instead
 *         - of those above. If you wish to allow use of your version of this file only
 *         - under the terms of either the GPL or the LGPL, and not to allow others to
 *         - use your version of this file under the terms of the MPL, indicate your
 *         - decision by deleting the provisions above and replace them with the notice
 *         - and other provisions required by the LGPL or the GPL. If you do not delete
 *         - the provisions above, a recipient may use your version of this file under
 *         - the terms of any one of the MPL, the GPL or the LGPL.
 *         -
 *         - ***** END LICENSE BLOCK *****
 * @author Edward Hieatt
 */
public class TestCaseResultTest extends TestCase {
    public TestCaseResultTest(String name) {
        super(name);
    }

    public void testBuildSuccessfulResultFromString() {
        TestCaseResult result = TestCaseResult.fromString("file:///dummy/path/dummyPage.html:testFoo|1.3|S||");
        assertEquals("file:///dummy/path/dummyPage.html:testFoo", result.getName());
        assertEquals(1.3d, result.getTime(), .001d);
        assertFalse(result.hadError());
        assertFalse(result.hadFailure());
        assertTrue(result.hadSuccess());
        assertNull(result.getError());
        assertNull(result.getFailure());
        assertEquals("<testcase name=\"file:///dummy/path/dummyPage.html:testFoo\" time=\"1.3\" />", result.writeXmlFragment());
    }

    public void testProblemSummary() {
        TestCaseResult result = TestCaseResult.fromString("file:///dummy/path/dummyPage.html:testFoo|1.3|E|Test Error Message|");
        assertEquals("file:///dummy/path/dummyPage.html:testFoo had an error:\nTest Error Message", result.writeProblemSummary());
    }

    public void testBuildErrorResultFromString() {
        TestCaseResult result = TestCaseResult.fromString("file:///dummy/path/dummyPage.html:testFoo|1.3|E|Test Error Message|");
        assertEquals("file:///dummy/path/dummyPage.html:testFoo", result.getName());
        assertEquals(1.3d, result.getTime(), .001d);
        assertTrue(result.hadError());
        assertFalse(result.hadFailure());
        assertFalse(result.hadSuccess());
        assertEquals("Test Error Message", result.getError());
        assertNull(result.getFailure());
        assertEquals("<testcase name=\"file:///dummy/path/dummyPage.html:testFoo\" time=\"1.3\"><error message=\"Test Error Message\" /></testcase>", result.writeXmlFragment());
    }

    public void testBuildFailureResultFromString() {
        TestCaseResult result = TestCaseResult.fromString("file:///dummy/path/dummyPage.html:testFoo|1.3|F|Test Failure Message|");
        assertEquals("file:///dummy/path/dummyPage.html:testFoo", result.getName());
        assertEquals(1.3d, result.getTime(), .001d);
        assertFalse(result.hadError());
        assertTrue(result.hadFailure());
        assertFalse(result.hadSuccess());
        assertNull(result.getError());
        assertEquals("Test Failure Message", result.getFailure());
        assertEquals("<testcase name=\"file:///dummy/path/dummyPage.html:testFoo\" time=\"1.3\"><failure message=\"Test Failure Message\" /></testcase>", result.writeXmlFragment());
    }

    public void testBuildFromXmlFragment() {
        TestCaseResult result = TestCaseResult.fromString("file:///dummy/path/dummyPage.html:testFoo|1.3|F|Test Failure Message|");
        Element element = new TestCaseResultWriter(result).createTestCaseElement();
        TestCaseResult reconstitutedResult = TestCaseResult.fromXmlElement(element);
        assertEquals("file:///dummy/path/dummyPage.html:testFoo", reconstitutedResult.getName());
        assertEquals(1.3d, reconstitutedResult.getTime(), .001d);
        assertFalse(reconstitutedResult.hadError());
        assertTrue(reconstitutedResult.hadFailure());
        assertFalse(reconstitutedResult.hadSuccess());
        assertNull(reconstitutedResult.getError());
        assertEquals("Test Failure Message", reconstitutedResult.getFailure());
    }
}
