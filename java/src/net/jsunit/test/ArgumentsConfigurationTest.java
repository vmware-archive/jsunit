package net.jsunit.test;

import net.jsunit.ArgumentsConfiguration;
import net.jsunit.Utility;

import java.io.File;
import java.util.Arrays;
import java.util.List;

public class ArgumentsConfigurationTest extends JsUnitTestCase {
    public ArgumentsConfigurationTest(String name) {
        super(name);
    }

    public void testSimple() throws Exception {
        List args = Arrays.asList(new String[]{
            "-port", "12345",
            "-url", "http://www.jsunit.net/",
            "-resourceBase", "/foo/bar/",
            "-logsDirectory", "/bar/foo",
            "-remoteMachineNames", "foo,bar",
            "-browserFileNames", "fu,bar"
        });
        ArgumentsConfiguration configuration = new ArgumentsConfiguration(args);
        configuration.doConfigure(server);
        assertEquals(12345, server.getPort());
        assertEquals(new File("/foo/bar/"), server.getResourceBase());
        assertEquals(new File("/bar/foo/"), server.getLogsDirectory());
        assertEquals(Utility.listWith("fu", "bar"), server.getLocalBrowserFileNames());
        assertEquals("http://www.jsunit.net/", server.getTestURL().toString());
    }

}
