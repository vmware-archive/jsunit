package net.jsunit;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

import org.jdom.Document;

public class RemoteMachineRunnerHitter implements RemoteRunnerHitter {

	public Document hitRemoteRunner(URL url) throws IOException {
		URL fullURL = new URL(url.toString() + "/jsunit/runner");
		return Utility.asXmlDocument(submitRequestTo(fullURL));
	}
	
    private String submitRequestTo(URL url) throws IOException {
        byte buffer[];
        URLConnection connection = url.openConnection();
        InputStream in = connection.getInputStream();
        buffer = new byte[in.available()];
        in.read(buffer);
        in.close();
        return new String(buffer);
    }

}
