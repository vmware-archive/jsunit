package net.jsunit.action;

import junit.framework.TestCase;
import net.jsunit.BrowserLaunchSpecification;
import net.jsunit.MockBrowserTestRunner;
import net.jsunit.captcha.SecurityViolation;
import net.jsunit.model.ResultType;
import net.jsunit.results.Skin;
import net.jsunit.utility.XmlUtility;

import java.io.File;

public class TestRunnerActionTest extends TestCase {

    private TestRunnerAction action;
    private MockBrowserTestRunner mockRunner;

    public void setUp() throws Exception {
        super.setUp();
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
        String overrideUrl = "http://www.example.com:8954/jsunit/testRunner.html?testPage=http://www.example.com:8954/tests/myTests.html?autoRun=true&submitResults=http://www.example.com:8954/tests";
        action.setUrl(overrideUrl);
        assertEquals(TestRunnerAction.SUCCESS, action.execute());
        assertEquals(2, mockRunner.launchSpecs.size());
        BrowserLaunchSpecification spec1 = mockRunner.launchSpecs.get(0);
        BrowserLaunchSpecification spec2 = mockRunner.launchSpecs.get(1);
        assertTrue(spec1.hasOverrideUrl());
        assertEquals(overrideUrl, spec1.getOverrideUrl());
        assertTrue(spec2.hasOverrideUrl());
        assertEquals(overrideUrl, spec2.getOverrideUrl());
    }

    public void testRequestIpAddressAndHostLogged() throws Exception {
        action.execute();
        assertEquals("Received request to run tests on URL '<default>'", mockRunner.logMessages.get(0));

        mockRunner.logMessages.clear();
        action.setRequestIPAddress("123.456.78.9");
        action.execute();
        assertEquals("Received request to run tests from 123.456.78.9 on URL '<default>'", mockRunner.logMessages.get(0));

        mockRunner.logMessages.clear();
        action.setRequestHost("www.example.com");
        action.execute();
        assertEquals("Received request to run tests from www.example.com (123.456.78.9) on URL '<default>'", mockRunner.logMessages.get(0));

        mockRunner.logMessages.clear();
        action.setRequestIPAddress("");
        action.execute();
        assertEquals("Received request to run tests from www.example.com on URL '<default>'", mockRunner.logMessages.get(0));

        mockRunner.logMessages.clear();
        action.setRequestIPAddress("12.34.56.78");
        action.setRequestHost("12.34.56.78");
        action.setUrl("http://www.example.com");
        action.execute();
        assertEquals("Received request to run tests from 12.34.56.78 on URL 'http://www.example.com'", mockRunner.logMessages.get(0));
    }

    public void testLimitToParticularBrowser() throws Exception {
        action.setBrowserId("1");
        assertEquals(TestRunnerAction.SUCCESS, action.execute());
        assertEquals(1, mockRunner.launchSpecs.size());
        assertEquals("mybrowser2.exe", mockRunner.launchSpecs.get(0).getBrowser().getStartCommand());
    }

    public void testLimitToBrowserWithBadId() throws Exception {
        action.setBrowserId("34");
        action.execute();
        assertEquals(TestRunnerAction.ERROR, action.execute());
        assertTrue(mockRunner.launchSpecs.isEmpty());
        assertEquals("<error>Invalid browser ID: 34</error>", XmlUtility.asString(action.getXmlRenderable().asXml()));
    }

    public void testLimitToBrowserWithNonIntegerId() throws Exception {
        action.setBrowserId("foo");
        action.execute();
        assertEquals(TestRunnerAction.ERROR, action.execute());
        assertTrue(mockRunner.launchSpecs.isEmpty());
        assertEquals("<error>Invalid browser ID: foo</error>", XmlUtility.asString(action.getXmlRenderable().asXml()));
    }

    public void testSkin() throws Exception {
        assertNull(action.getSkin());
        Skin skinFile = new Skin(2, new File("foo.xsl"));
        action.setSkin(skinFile);
        assertEquals(skinFile, action.getSkin());
        action.setBrowserTestRunner(mockRunner);
        assertEquals(ResultDisplayerAction.TRANSFORM, action.execute());
    }

    public void testSecurityFailure() throws Exception {
        action.setSecurityViolation(SecurityViolation.FAILED_CAPTCHA);
        assertEquals(
                XmlUtility.asString(SecurityViolation.FAILED_CAPTCHA.asXml()),
                XmlUtility.asString(action.getXmlRenderable().asXml())
        );
    }

}
