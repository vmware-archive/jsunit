package net.jsunit.configuration;

import junit.framework.TestCase;
import net.jsunit.RemoteConfigurationSource;

import java.net.URL;

public class RemoteConfigurationTest extends TestCase {

    public void testSimple() throws Exception {
        URL url = new URL("http://www.example.com");
        RemoteConfigurationSource source = new DummyRemoteConfigurationSource(url.toString());
        RemoteConfiguration configuration = new RemoteConfiguration(url, source);

        assertEquals(url.toString(), configuration.getRemoteURL().toString());
        assertTrue(new ServerConfiguration(source).equals(configuration));
    }

}
