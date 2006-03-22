package net.jsunit;

import org.jdom.Document;

import java.io.IOException;
import java.net.URL;

public interface RemoteRunnerHitter {

    public Document hitURL(URL url) throws IOException;

}
