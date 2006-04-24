package net.jsunit.configuration;

import junit.framework.TestCase;

import java.net.URL;

public class RemoteConfigurationTest extends TestCase {

    public void testSimple() throws Exception {
        URL url = new URL("http://www.example.com");
        ConfigurationSource source = new DummyConfigurationSource();
        RemoteConfiguration configuration = new RemoteConfiguration(url, source);

        assertEquals(url.toString(), configuration.getRemoteURL().toString());
        assertTrue(new Configuration(source).equalsForServerType(configuration, ServerType.STANDARD));
    }
}
