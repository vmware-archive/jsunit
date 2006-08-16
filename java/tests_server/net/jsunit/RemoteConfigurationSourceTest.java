package net.jsunit;

import junit.framework.TestCase;
import net.jsunit.configuration.DummyConfigurationSource;
import net.jsunit.configuration.ServerConfiguration;
import net.jsunit.utility.XmlUtility;
import org.jdom.Document;

import java.io.IOException;

public class RemoteConfigurationSourceTest extends TestCase {
    private String baseURL;

    protected void setUp() throws Exception {
        super.setUp();
        baseURL = "http://www.example.com:1234/jsunit";
    }

    public void testSimple() throws Exception {
        ServerConfiguration configuration = new ServerConfiguration(new DummyConfigurationSource());
        MockRemoteServerHitter mockHitter = new MockRemoteServerHitter();
        mockHitter.urlToDocument.put(baseURL + "/config", new Document(configuration.asXml()));

        RemoteConfigurationSource remoteSource = new RemoteConfigurationSource(mockHitter, baseURL);
        assertTrue(remoteSource.isInitialized());

        ServerConfiguration remoteConfiguration = new ServerConfiguration(remoteSource);
        assertEquals(XmlUtility.asString(configuration.asXml()),
                XmlUtility.asString(remoteConfiguration.asXml())
        );
    }

    public void testBlowingUpURL() throws Exception {
        try {
            new RemoteConfigurationSource(new BlowingUpRemoteServerHitter(), baseURL);
            fail();
        } catch (IOException e) {
        }
    }

}
