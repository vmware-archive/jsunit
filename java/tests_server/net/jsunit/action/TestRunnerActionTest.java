package net.jsunit.action;

import junit.framework.TestCase;
import net.jsunit.MockBrowserTestRunner;
import net.jsunit.model.Browser;
import net.jsunit.model.BrowserLaunchSpecification;
import net.jsunit.model.ResultType;
import net.jsunit.utility.XmlUtility;

import java.util.Arrays;

public class TestRunnerActionTest extends TestCase {

    private TestRunnerAction action;
    private MockBrowserTestRunner mockRunner;

    public void setUp() throws Exception {
        super.setUp();
        createAction();
    }

    private void createAction() {
        action = new TestRunnerAction();
        mockRunner = new MockBrowserTestRunner();
        action.setBrowserTestRunner(mockRunner);
    }

    public void testSuccess() throws Exception {
        mockRunner.shouldSucceed = true;
        assertEquals(TestRunnerAction.SUCCESS, action.execute());
        String xmlString = XmlUtility.asString(action.getXmlRenderable().asXml());
        assertTrue(xmlString.startsWith("<testRunResult type=\"" + ResultType.SUCCESS.name()));
    }

    public void testFailure() throws Exception {
        mockRunner.shouldSucceed = false;
        assertEquals(TestRunnerAction.SUCCESS, action.execute());
        String xmlString = XmlUtility.asString(action.getXmlRenderable().asXml());
        assertTrue(xmlString.startsWith("<testRunResult type=\"" + ResultType.FAILURE.name()));
    }

    public void testOverrideUrl() throws Exception {
        String overrideUrl = "  http://www.example.com:8954/jsunit/testRunner.html?testPage=http://www.example.com:8954/tests/myTests.html?autoRun=true&submitResults=http://www.example.com:8954/tests   ";
        action.setUrl(overrideUrl);
        assertEquals(TestRunnerAction.SUCCESS, action.execute());
        assertEquals(2, mockRunner.launchSpecs.size());
        BrowserLaunchSpecification spec1 = mockRunner.launchSpecs.get(0);
        BrowserLaunchSpecification spec2 = mockRunner.launchSpecs.get(1);
        assertTrue(spec1.hasOverrideUrl());
        assertEquals(overrideUrl.trim(), spec1.getOverrideUrl());
        assertTrue(spec2.hasOverrideUrl());
        assertEquals(overrideUrl.trim(), spec2.getOverrideUrl());
    }

    public void testLimitBrowsers() throws Exception {
        action.setSelectedBrowsers(Arrays.asList(new Browser[]{new Browser("mybrowser2.exe", 1)}));
        assertEquals(TestRunnerAction.SUCCESS, action.execute());
        assertEquals(1, mockRunner.launchSpecs.size());
        assertEquals("mybrowser2.exe", mockRunner.launchSpecs.get(0).getBrowser().getStartCommand());

        createAction();
        action.setSelectedBrowsers(Arrays.asList(new Browser[]{new Browser("mybrowser1.exe", 0), new Browser("mybrowser2.exe", 1)}));
        assertEquals(TestRunnerAction.SUCCESS, action.execute());
        assertEquals(2, mockRunner.launchSpecs.size());
        assertEquals("mybrowser1.exe", mockRunner.launchSpecs.get(0).getBrowser().getStartCommand());
        assertEquals("mybrowser2.exe", mockRunner.launchSpecs.get(1).getBrowser().getStartCommand());

    }

    public void testErrorMessage() throws Exception {
        action.setErrorMessage("foobar barfoo");
        assertEquals("<error>foobar barfoo</error>", XmlUtility.asString(action.getXmlRenderable().asXml()));
    }

}
