/**
 * 
 */
package net.jsunit;

import org.jdom.Document;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

public class MockRemoteRunnerHitter implements RemoteRunnerHitter {

    public List<String> urlsPassed = new ArrayList<String>();
    public Map<String, Document> urlToDocument = new HashMap<String, Document>();

    public synchronized Document hitURL(URL url) {
        String urlString = url.toString();
        urlsPassed.add(urlString);
        return urlToDocument.get(urlString);
    }
}