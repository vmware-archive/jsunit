package net.jsunit.test;

import junit.framework.TestCase;
import net.jsunit.JsUnitServer;

/**
 * @author Edward Hieatt, edward@jsunit.net
 */

public abstract class JsUnitTest extends TestCase {
    protected JsUnitServer server;

    public void setUp() throws Exception {
        super.setUp();
        server = JsUnitServer.instance();
    }

    public void tearDown() throws Exception {
        server.clearResults();
        super.tearDown();
    }

    public JsUnitTest(String name) {
        super(name);
    }
}
