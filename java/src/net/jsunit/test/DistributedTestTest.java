package net.jsunit.test;

import net.jsunit.DistributedTest;
import net.jsunit.JsUnitServer;

/**
 * @author Edward Hieatt, edward@jsunit.net
 */

public class DistributedTestTest extends DistributedTest {
    public DistributedTestTest(String name) {
        super(name);
    }

    public void setUp() throws Exception {
        super.setUp();
        JsUnitServer.instance().start();
    }

    public void tearDown() throws Exception {
        JsUnitServer.instance().stop();
        super.tearDown();
    }
}
