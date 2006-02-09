package net.jsunit;
 
import junit.framework.TestCase;
import net.jsunit.model.ResultType;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.input.SAXBuilder;

import java.io.InputStream;
import java.io.StringReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;

/**
 * @author Edward Hieatt, edward@jsunit.net
 */

public class DistributedTest extends TestCase {
    public static final String REMOTE_MACHINE_URLS = "remoteMachineURLs";

    private List<String> remoteMachineURLs;

    public DistributedTest(String name) {
        super(name);
        String urlsString = System.getProperty(REMOTE_MACHINE_URLS);
        remoteMachineURLs = Utility.listFromCommaDelimitedString(urlsString);
    }

    public void testCollectResults() {
        for (String remoteMachineName : remoteMachineURLs) {
            String result = submitRequestTo(remoteMachineName);
            Element resultElement = null;
            try {
                Document document = new SAXBuilder().build(new StringReader(result));
                resultElement = document.getRootElement();
                if (!"testRunResult".equals(resultElement.getName()))
                    fail("Unrecognized response from " + remoteMachineName + ": " + result);
            } catch (Exception e) {
                e.printStackTrace();
                fail("Could not parse XML response from " + remoteMachineName + ": " + result);
            }
            assertEquals(remoteMachineName + " failed", ResultType.SUCCESS.name(), resultElement.getAttribute("type").getValue());
        }
    }

    private String submitRequestTo(String remoteMachineName) {
        byte buffer[];
        InputStream in;
        try {
            URL url = new URL(remoteMachineName + "/jsunit/runner");
            URLConnection connection = url.openConnection();
            in = connection.getInputStream();
            buffer = new byte[in.available()];
            in.read(buffer);
            in.close();
            return new String(buffer);
        } catch (Exception e) {
            e.printStackTrace();
            fail("Could not submit request to " + remoteMachineName);
        }
        return null;
    }

}