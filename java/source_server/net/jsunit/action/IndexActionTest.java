package net.jsunit.action;

import junit.framework.TestCase;

public class IndexActionTest extends TestCase {

    private IndexAction action;

    protected void setUp() throws Exception {
        super.setUp();
        action = new IndexAction();
    }

    public void testSimple() throws Exception {
        assertEquals(IndexAction.SUCCESS, action.execute());
        assertEquals(0, action.getReferencedJsFileFieldCount());
    }

    public void testIncludeReferencedJsFiles() throws Exception {
        action.setReferencedJsFileFieldCount(3);
        assertEquals(3, action.getReferencedJsFileFieldCount());
    }

}
