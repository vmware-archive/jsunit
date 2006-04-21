package net.jsunit;

import junit.framework.TestCase;
import net.jsunit.configuration.Configuration;
import net.jsunit.configuration.ServerType;
import org.jdom.Document;

public class JsUnitAggregateServerTest extends TestCase {

    private JsUnitAggregateServer server;

    protected void tearDown() throws Exception {
        server.dispose();
        super.tearDown();
    }

    public void testStartTestRun() throws Exception {
        server = new JsUnitAggregateServer(new Configuration(new DummyAggregateConfigurationSource()));
        assertEquals(ServerType.AGGREGATE, server.serverType());
    }

    public void testStartCachesRemoteConfigurations() throws Exception {
        MockRemoteServerHitter hitter = new MockRemoteServerHitter();
        server = new JsUnitAggregateServer(new Configuration(new DummyAggregateConfigurationSource()), hitter);
        hitter.urlToDocument.put(DummyAggregateConfigurationSource.REMOTE_URL_1 + "/jsunit/config", configuration1Document());
        hitter.urlToDocument.put(DummyAggregateConfigurationSource.REMOTE_URL_2 + "/jsunit/config", configuration2Document());
        server.start();
        assertEquals(2, hitter.urlsPassed.size());
        assertTrue(hitter.urlsPassed.contains(DummyAggregateConfigurationSource.REMOTE_URL_1 + "/jsunit/config"));
        assertTrue(hitter.urlsPassed.contains(DummyAggregateConfigurationSource.REMOTE_URL_2 + "/jsunit/config"));
        assertEquals(2, server.getCachedRemoteConfigurations().size());
    }

    public void testStartServerWithBlowingUpRemoteServer() throws Exception {
        BlowingUpRemoteServerHitter hitter = new BlowingUpRemoteServerHitter();
        server = new JsUnitAggregateServer(new Configuration(new DummyAggregateConfigurationSource()), hitter);
        server.start();
        assertTrue(server.getCachedRemoteConfigurations().isEmpty());
    }

    private Document configuration1Document() {
        return new Document(new Configuration(new DummyConfigurationSource() {
            public String browserFileNames() {
                return "/usr/bin/mozilla,/usr/bin/firefox";
            }
        }).asXml(ServerType.STANDARD));
    }

    private Document configuration2Document() {
        return new Document(new Configuration(new DummyConfigurationSource() {
            public String browserFileNames() {
                return "c:\\program files\\iexplore.exe,c:\\program files\\netscape6.exe";
            }
        }).asXml(ServerType.STANDARD));
    }

}