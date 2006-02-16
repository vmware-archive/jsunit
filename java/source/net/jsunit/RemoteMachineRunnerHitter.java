package net.jsunit;

import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

import org.jdom.Document;

public class RemoteMachineRunnerHitter implements RemoteRunnerHitter {

	public Document hitRemoteRunner(URL url) {
		String xml;
		try {
			xml = submitRequestTo(new URL(url.toString()+"/jsunit/runner"));
		} catch (Exception e) {
			xml = "<testResult/>";
		}
		return Utility.asXmlDocument(xml);
	}
	
    private String submitRequestTo(URL url) throws Exception {
        byte buffer[];
        URLConnection connection = url.openConnection();
        InputStream in = connection.getInputStream();
        buffer = new byte[in.available()];
        in.read(buffer);
        in.close();
        return new String(buffer);
    }

}
