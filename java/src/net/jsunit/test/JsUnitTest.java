package net.jsunit.test;

import junit.framework.TestCase;
import net.jsunit.JsUnitServer;

/**
 * @author Edward Hieatt, edward@jsunit.net
 */

public abstract class JsUnitTest extends TestCase {
    protected JsUnitServer acceptor;

    public void setUp() throws Exception {
        super.setUp();
        acceptor = JsUnitServer.instance();
    }

    public void tearDown() throws Exception {
        acceptor.clearResults();
        super.tearDown();
    }

    public JsUnitTest(String name) {
        super(name);
    }
}
