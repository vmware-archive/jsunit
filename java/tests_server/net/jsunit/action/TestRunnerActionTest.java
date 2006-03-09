package net.jsunit.action;

import junit.framework.TestCase;
import net.jsunit.MockBrowserTestRunner;
import net.jsunit.model.ResultType;
import net.jsunit.utility.XmlUtility;

public class TestRunnerActionTest extends TestCase {

    private TestRunnerAction action;
    private MockBrowserTestRunner mockRunner;

    public void setUp() throws Exception {
        super.setUp();
        action = new TestRunnerAction();
        mockRunner = new MockBrowserTestRunner();
        mockRunner.hasReceivedResult = true;
        action.setBrowserTestRunner(mockRunner);
    }

    public void testSuccess() throws Exception {
        mockRunner.shouldSucceed = true;
        assertEquals(TestRunnerAction.SUCCESS, action.execute());
        String xmlString = XmlUtility.asString(action.getXmlRenderable().asXml());
        assertTrue(xmlString.startsWith("<testRunResult type=\""+ResultType.SUCCESS.name()));
    }

    public void testFailure() throws Exception {
        mockRunner.shouldSucceed = false;
        assertEquals(TestRunnerAction.SUCCESS, action.execute());
        String xmlString = XmlUtility.asString(action.getXmlRenderable().asXml());
        assertTrue(xmlString.startsWith("<testRunResult type=\""+ResultType.FAILURE.name()));
    }

    public void testOverrideUrl() throws Exception {
        String overrideUrl = "http://www.example.com:8954/jsunit/testRunner.html?testPage=http://www.example.com:8954/tests/myTests.html?autoRun=true&submitResults=http://www.example.com:8954/tests";
        action.setUrl(overrideUrl);
        assertEquals(TestRunnerAction.SUCCESS, action.execute());
        assertTrue(mockRunner.launchSpec.hasOverrideUrl());
        assertEquals(overrideUrl, mockRunner.launchSpec.getOverrideUrl());
    }

    public void testRemoteAddressLogged() throws Exception {
        action.setRemoteAddress("123.456.78.9");
        action.execute();
        assertTrue(mockRunner.logMessages.get(0).indexOf("123.456.78.9") !=-1);
    }

}