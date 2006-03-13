package net.jsunit;

import net.jsunit.utility.XmlUtility;
import org.jdom.Document;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

public class RemoteMachineRunnerHitter implements RemoteRunnerHitter {

    public Document hitURL(URL url) throws IOException {
        String xmlResultString = submitRequestTo(url);
        return XmlUtility.asXmlDocument(xmlResultString);
    }

    private String submitRequestTo(URL url) throws IOException {
        URLConnection connection = url.openConnection();
        InputStream in = connection.getInputStream();
        ByteArrayOutputStream outStream = new ByteArrayOutputStream();
        while (in.available() > 0) {
            byte[] buffer = new byte[in.available()];
            in.read(buffer);
            outStream.write(buffer);
        }
        in.close();
        return outStream.toString();
    }

}
