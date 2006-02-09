package net.jsunit.action;

import junit.framework.TestCase;
import net.jsunit.MockBrowserTestRunner;
import net.jsunit.Utility;
import net.jsunit.model.BrowserResult;

public class ResultDisplayerActionTest extends TestCase {

    private ResultDisplayerAction action;
    private MockBrowserTestRunner mockRunner;

    public void setUp() throws Exception {
        super.setUp();
        action = new ResultDisplayerAction();
        mockRunner = new MockBrowserTestRunner();
        action.setBrowserTestRunner(mockRunner);
        action.setId("12345");
    }

    public void testResultFound() throws Exception {
        mockRunner.resultToReturn = new BrowserResult();
        assertEquals(ResultDisplayerAction.SUCCESS, action.execute());
        assertEquals("12345", mockRunner.idPassed);
        assertEquals(mockRunner.resultToReturn, action.getXmlRenderable());
    }

    public void testResultNotFound() throws Exception {
        assertEquals(ResultDisplayerAction.SUCCESS, action.execute());
        assertEquals("12345", mockRunner.idPassed);
        assertEquals("<error>No Test Result has been submitted with ID 12345</error>", Utility.asString(action.getXmlRenderable().asXml()));
    }

    public void testIdNotGiven() throws Exception {
        action.setId(null);
        assertEquals(ResultDisplayerAction.SUCCESS, action.execute());
        assertNull(mockRunner.idPassed);
        assertEquals("<error>No ID given</error>", Utility.asString(action.getXmlRenderable().asXml()));
    }
}
