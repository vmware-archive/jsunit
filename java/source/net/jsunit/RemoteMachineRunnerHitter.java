package net.jsunit;

import org.jdom.Document;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

public class RemoteMachineRunnerHitter implements RemoteRunnerHitter {

	public Document hitURL(URL url) throws IOException {
        String xmlResultString = submitRequestTo(url);
        return Utility.asXmlDocument(xmlResultString);
	}
	
    private String submitRequestTo(URL url) throws IOException {
        byte buffer[];
        URLConnection connection = url.openConnection();
        InputStream in = connection.getInputStream();
        buffer = new byte[in.available()];
        //noinspection ResultOfMethodCallIgnored
        in.read(buffer);
        in.close();
        return new String(buffer);
    }

}
