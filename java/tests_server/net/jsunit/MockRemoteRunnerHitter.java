/**
 * 
 */
package net.jsunit;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.jdom.Document;

public class MockRemoteRunnerHitter implements RemoteRunnerHitter {

    public List<URL> urlsPassed;
    public List<Document> documents = new ArrayList<Document>();
    private int index = 0;

    public MockRemoteRunnerHitter() {
        urlsPassed = new ArrayList<URL>();
    }

    public Document hitURL(URL url) {
        urlsPassed.add(url);
        return documents.get(index++);
    }
}