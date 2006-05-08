/**
 * 
 */
package net.jsunit;

import org.jdom.Document;

import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.io.File;
import java.io.IOException;

public class MockRemoteServerHitter implements RemoteServerHitter {

    public List<String> urlsPassed = new ArrayList<String>();
    public List<Map<String, List<File>>> fieldsToValuesMapsPosted = new ArrayList<Map<String, List<File>>>();
    public Map<String, Document> urlToDocument = new HashMap<String, Document>();

    public Document hitURL(URL url) {
        String urlString = url.toString();
        urlsPassed.add(urlString);
        return urlToDocument.get(urlString);
    }

    public Document postToURL(URL url, Map<String, List<File>> fieldsToValues) throws IOException {
        String urlString = url.toString();
        urlsPassed.add(urlString);
        fieldsToValuesMapsPosted.add(fieldsToValues);
        return urlToDocument.get(urlString);
    }

}