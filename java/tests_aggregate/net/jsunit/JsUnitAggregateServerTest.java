package net.jsunit;

import junit.framework.TestCase;
import net.jsunit.configuration.AggregateConfiguration;
import net.jsunit.configuration.DummyConfigurationSource;
import net.jsunit.configuration.ServerType;
import org.jdom.Document;

public class JsUnitAggregateServerTest extends TestCase {

    private JsUnitAggregateServer server;

    protected void tearDown() throws Exception {
        if (server.isAlive())
            server.dispose();
        super.tearDown();
    }

    public void testStartTestRun() throws Exception {
        server = new JsUnitAggregateServer(new AggregateConfiguration(new DummyConfigurationSource()));
        assertEquals(ServerType.AGGREGATE, server.serverType());
    }

    public void testStartCachesRemoteConfigurations() throws Exception {
        MockRemoteServerHitter hitter = new MockRemoteServerHitter();
        server = new JsUnitAggregateServer(new AggregateConfiguration(new DummyConfigurationSource()), hitter);
        hitter.urlToDocument.put(DummyConfigurationSource.REMOTE_URL_1 + "/config", configuration1Document());
        hitter.urlToDocument.put(DummyConfigurationSource.REMOTE_URL_2 + "/config", configurationToDocument());
        server.preStart();
        assertEquals(2, hitter.urlsPassed.size());
        assertTrue(hitter.urlsPassed.contains(DummyConfigurationSource.REMOTE_URL_1 + "/config"));
        assertTrue(hitter.urlsPassed.contains(DummyConfigurationSource.REMOTE_URL_2 + "/config"));
        assertEquals(2, server.getCachedRemoteConfigurations().size());
    }

    public void testStartServerWithBlowingUpRemoteServer() throws Exception {
        BlowingUpRemoteServerHitter hitter = new BlowingUpRemoteServerHitter();
        server = new JsUnitAggregateServer(new AggregateConfiguration(new DummyConfigurationSource()), hitter);
        server.preStart();
        assertTrue(server.getCachedRemoteConfigurations().isEmpty());
    }

    private Document configuration1Document() {
        return new Document(new AggregateConfiguration(new DummyConfigurationSource() {
            public String browserFileNames() {
                return "/usr/bin/mozilla,/usr/bin/firefox";
            }
        }).asXml());
    }

    private Document configurationToDocument() {
        return new Document(new AggregateConfiguration(new DummyConfigurationSource() {
            public String browserFileNames() {
                return "c:\\program files\\iexplore.exe,c:\\program files\\netscape6.exe";
            }
        }).asXml());
    }

}