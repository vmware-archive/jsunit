package net.jsunit.test;

import junit.framework.TestCase;
import net.jsunit.JsUnitServer;

/**
 * @author Edward Hieatt, edward@jsunit.net
 */

public abstract class JsUnitTestCase extends TestCase {
    protected JsUnitServer server;

    public void setUp() throws Exception {
        super.setUp();
        server = JsUnitServer.instance();
    }

    public void tearDown() throws Exception {
        server.clearResults();
        super.tearDown();
    }

    public JsUnitTestCase(String name) {
        super(name);
    }
}
