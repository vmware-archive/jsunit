package net.jsunit.configuration;

import junit.framework.TestCase;
import net.jsunit.Utility;

import java.io.File;
import java.util.Arrays;
import java.util.List;

public class ArgumentsConfigurationTest extends TestCase {
    public ArgumentsConfigurationTest(String name) {
        super(name);
    }

    public void testSimple() throws Exception {
        List<String> args = Arrays.asList(new String[] {
            "-port", "12345",
            "-url", "http://www.jsunit.net/",
            "-resourceBase", "foo",
            "-logsDirectory", "bar",
            "-remoteMachineNames", "foo,bar",
            "-browserFileNames", "fu,bar",
            "-closeBrowsersAfterTestRuns", "true"
        });
        ArgumentsConfigurationSource source = new ArgumentsConfigurationSource(args);
        Configuration configuration = new Configuration(source);
        assertEquals(12345, configuration.getPort());
        assertEquals(new File("foo"), configuration.getResourceBase());
        assertEquals(new File("bar"), configuration.getLogsDirectory());
        assertEquals(Utility.listWith("fu", "bar"), configuration.getBrowserFileNames());
        assertEquals("http://www.jsunit.net/", configuration.getTestURL().toString());
        assertTrue(configuration.shouldCloseBrowsersAfterTestRuns());
    }

    public void tearDown() throws Exception {
        File createdLogsDirectory = new File("bar");
        if (createdLogsDirectory.exists())
            createdLogsDirectory.delete();
        super.tearDown();
    }

}