package net.jsunit;

import org.jdom.Document;

import java.io.IOException;
import java.io.File;
import java.net.URL;
import java.util.Map;
import java.util.List;

public interface RemoteServerHitter {

    public Document hitURL(URL url) throws IOException;

    public Document postToURL(URL url, Map<String, List<File>> fieldsToValues) throws IOException;

}
