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
        server = new JsUnitServer();
        server.initialize();
    }

    public JsUnitTestCase(String name) {
        super(name);
    }
}
