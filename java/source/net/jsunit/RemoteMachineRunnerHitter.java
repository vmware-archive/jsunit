package net.jsunit;

import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

import net.jsunit.model.TestRunResult;

import org.jdom.Document;

public class RemoteMachineRunnerHitter implements RemoteRunnerHitter {

	public Document hitRemoteRunner(URL url) {
		Document result;
		try {
			result = Utility.asXmlDocument(submitRequestTo(new URL(url.toString()+"/jsunit/runner")));
		} catch (Exception e) {
			result = new Document(new TestRunResult().asXml());
		}
		return result;
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
