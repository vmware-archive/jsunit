package net.jsunit.action;

import junit.framework.TestCase;

public class PageActionTest extends TestCase {

    private PageAction action;

    protected void setUp() throws Exception {
        super.setUp();
        action = new PageAction();
    }

    public void testIncludeReferencedJsFiles() throws Exception {
        action.setReferencedJsFileFieldCount(3);
        assertEquals(3, action.getReferencedJsFileFieldCount());
    }

}
