package net.jsunit.test;

import junit.framework.TestCase;
import net.jsunit.JsUnitServer;
import net.jsunit.Configuration;

/**
 * @author Edward Hieatt, edward@jsunit.net
 */

public abstract class JsUnitTestCase extends TestCase {
    protected JsUnitServer server;

    public JsUnitTestCase(String name) {
        super(name);
    }

    public void setUp() throws Exception {
        super.setUp();
        setUpProperties();
        server = new JsUnitServer();
        server.initialize();
    }

    private void setUpProperties() {
        System.setProperty(Configuration.BROWSER_FILE_NAMES, "c:\\program files\\internet explorer\\iexplore.exe");
        System.setProperty(Configuration.URL, "file:///c:/jsunit/testRunner.html?testPage=c:\\jsunit\\tests\\jsUnitTestSuite.html&autoRun=true&submitresults=true");
    }

    public void tearDown() throws Exception {
        System.getProperties().remove(Configuration.BROWSER_FILE_NAMES);
        System.getProperties().remove(Configuration.URL);
        super.tearDown();
    }

}