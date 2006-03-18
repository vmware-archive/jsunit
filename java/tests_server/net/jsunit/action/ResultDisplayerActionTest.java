package net.jsunit.action;

import junit.framework.TestCase;
import net.jsunit.MockBrowserTestRunner;
import net.jsunit.model.BrowserResult;
import net.jsunit.utility.XmlUtility;

public class ResultDisplayerActionTest extends TestCase {

    private ResultDisplayerAction action;
    private MockBrowserTestRunner mockRunner;

    public void setUp() throws Exception {
        super.setUp();
        action = new ResultDisplayerAction();
        mockRunner = new MockBrowserTestRunner();
        action.setBrowserTestRunner(mockRunner);
        action.setId("12345");
        action.setBrowserId(8);
    }

    public void testResultFound() throws Exception {
        mockRunner.resultToReturn = new BrowserResult();
        assertEquals(ResultDisplayerAction.SUCCESS, action.execute());
        assertEquals("12345", mockRunner.idPassed);
        assertEquals(8, mockRunner.browserIdPassed.intValue());
        assertEquals(mockRunner.resultToReturn, action.getXmlRenderable());
    }

    public void testResultNotFound() throws Exception {
        assertEquals(ResultDisplayerAction.SUCCESS, action.execute());
        assertEquals("12345", mockRunner.idPassed);
        assertEquals(8, mockRunner.browserIdPassed.intValue());
        assertEquals("<error>No Test Result has been submitted with ID '12345' for browser ID '8'</error>", XmlUtility.asString(action.getXmlRenderable().asXml()));
    }

    public void testIdNotGiven() throws Exception {
        action.setId(null);
        action.setBrowserId(null);
        assertEquals(ResultDisplayerAction.SUCCESS, action.execute());
        assertNull(mockRunner.idPassed);
        assertNull(mockRunner.browserIdPassed);
        assertEquals("<error>A Test Result ID and a browser ID must both be given</error>", XmlUtility.asString(action.getXmlRenderable().asXml()));
    }
}
