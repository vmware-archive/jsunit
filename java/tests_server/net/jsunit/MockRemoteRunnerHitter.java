/**
 * 
 */
package net.jsunit;

import org.jdom.Document;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class MockRemoteRunnerHitter implements RemoteRunnerHitter {

    public List<String> urlsPassed = new ArrayList<String>();
    public List<Document> documents = new ArrayList<Document>();
    private int index = 0;

    public synchronized Document hitURL(URL url) {
        urlsPassed.add(url.toString());
        return documents.get(index++);
    }
}