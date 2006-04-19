/**
 * 
 */
package net.jsunit;

import org.jdom.Document;

import java.io.IOException;
import java.io.File;
import java.net.URL;
import java.util.Map;
import java.util.List;

public class BlowingUpRemoteServerHitter implements RemoteServerHitter {

    public Document hitURL(URL url) throws IOException {
        throw new IOException();
    }

    public Document postToURL(URL url, Map<String, List<File>> fieldsToValues) throws IOException {
        throw new IOException();
    }
}