package net.jsunit;

import junit.framework.TestCase;
import net.jsunit.configuration.DummyConfigurationSource;
import net.jsunit.configuration.ServerConfiguration;
import org.jdom.Document;

import java.net.URL;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class RemoteConfigurationFetcherTest extends TestCase {

    public void testSimple() throws Exception {
        String remoteMachineURL = "http://www.example.com/jsunit";
        MockRemoteServerHitter hitter = new MockRemoteServerHitter();
        ServerConfiguration configuration = new ServerConfiguration(new DummyConfigurationSource());
        Document configurationDocument = new Document(configuration.asXml());
        hitter.urlToDocument.put(remoteMachineURL + "/config", configurationDocument);
        RemoteConfigurationFetcher fetcher = new RemoteConfigurationFetcher(hitter, new URL(remoteMachineURL));
        fetcher.start();
        fetcher.join();
        assertEquals(remoteMachineURL, fetcher.getRemoteMachineURL().toString());
        assertTrue(configuration.equals(fetcher.getRetrievedRemoteConfiguration()));
    }

    public void testSorting() throws Exception {
        RemoteServerHitter hitter = new MockRemoteServerHitter();
        RemoteConfigurationFetcher fetcher1 = new RemoteConfigurationFetcher(hitter, new URL("http://aaaa.com"));
        RemoteConfigurationFetcher fetcher2 = new RemoteConfigurationFetcher(hitter, new URL("http://bbbb.com"));
        RemoteConfigurationFetcher fetcher3 = new RemoteConfigurationFetcher(hitter, new URL("http://cccc.com"));
        List<RemoteConfigurationFetcher> list = Arrays.asList(new RemoteConfigurationFetcher[]{fetcher2, fetcher3, fetcher1});
        Collections.sort(list);
        assertEquals(fetcher1, list.get(0));
        assertEquals(fetcher2, list.get(1));
        assertEquals(fetcher3, list.get(2));
    }

}
