package net.jsunit.test;

import net.jsunit.ArgumentsConfiguration;

import java.util.List;
import java.util.Arrays;

public class ArgumentsConfigurationTest extends JsUnitTestCase {
    public ArgumentsConfigurationTest(String name) {
        super(name);
    }

    public void testSimple() throws Exception {
        List args = Arrays.asList(new String[] {
            "port",
            "12345",
            "url",
            "http://www.jsunit.net/"
        });
        ArgumentsConfiguration configuration = new ArgumentsConfiguration(args);
        configuration.configure(server);
        assertEquals(12345, server.getPort());
    }
}
