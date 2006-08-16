package net.jsunit;

import net.jsunit.utility.StreamUtility;
import net.jsunit.utility.XmlUtility;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.multipart.FilePart;
import org.apache.commons.httpclient.methods.multipart.MultipartRequestEntity;
import org.apache.commons.httpclient.methods.multipart.Part;
import org.jdom.Document;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class RemoteMachineServerHitter implements RemoteServerHitter {

    public Document hitURL(URL url) throws IOException {
        String xmlResultString = doGet(url);
        return XmlUtility.asXmlDocument(xmlResultString);
    }

    public Document postToURL(URL url, Map<String, List<File>> fieldsToFiles) throws IOException {
        String xmlResultString = doFilePost(url, fieldsToFiles);
        return XmlUtility.asXmlDocument(xmlResultString);
    }

    private String doGet(URL url) throws IOException {
        URLConnection connection = openConnection(url);
        return StreamUtility.readAllFromStream(connection.getInputStream());
    }

    private URLConnection openConnection(URL url) throws IOException {
        URLConnection connection = url.openConnection();
        connection.setUseCaches(false);
        return connection;
    }

    private String doFilePost(URL url, Map<String, List<File>> fieldNamesToFiles) {
        PostMethod filePost = new PostMethod(url.toString());
        try {
            List<Part> parts = new ArrayList<Part>();
            for (String key : fieldNamesToFiles.keySet()) {
                List<File> files = fieldNamesToFiles.get(key);
                for (File file : files)
                    parts.add(new FilePart(key, file.getName(), file));
            }
            filePost.setRequestEntity(new MultipartRequestEntity(parts.toArray(new Part[parts.size()]), filePost.getParams()));
            HttpClient client = new HttpClient();
            client.getHttpConnectionManager().getParams().setConnectionTimeout(5000);
            int status = client.executeMethod(filePost);
            if (status == HttpStatus.SC_OK) {
                InputStream stream = filePost.getResponseBodyAsStream();
                return StreamUtility.readAllFromStream(stream);
            } else {
                throw new RuntimeException(HttpStatus.getStatusText(status));
            }
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        } finally {
            filePost.releaseConnection();
        }
    }

}
