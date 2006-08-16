package net.jsunit;

import net.jsunit.configuration.ServerConfiguration;
import net.jsunit.utility.XmlUtility;

public class RemoteConfigurationSourceFunctionalTest extends ServerFunctionalTestCase {

    public void testSimple() throws Exception {
        String remoteMachineURL = "http://localhost:" + port() + "/jsunit";
        RemoteConfigurationSource source = new RemoteConfigurationSource(new RemoteMachineServerHitter(), remoteMachineURL);
        assertTrue(source.isInitialized());
        ServerConfiguration remoteConfiguration = new ServerConfiguration(source);
        assertEquals(
                XmlUtility.asString(server.getConfiguration().asXml()),
                XmlUtility.asString(remoteConfiguration.asXml())
        );
    }

}
