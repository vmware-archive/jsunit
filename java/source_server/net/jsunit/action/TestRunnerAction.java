package net.jsunit.action;

import net.jsunit.InvalidBrowserIdException;
import net.jsunit.SkinSource;
import net.jsunit.TestRunManager;
import net.jsunit.XmlRenderable;
import net.jsunit.configuration.Configuration;
import net.jsunit.results.Skin;
import net.jsunit.upload.TestPage;
import net.jsunit.upload.TestPageGenerator;
import net.jsunit.utility.StringUtility;
import net.jsunit.utility.SystemUtility;

import javax.xml.transform.TransformerException;
import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;

public class TestRunnerAction extends JsUnitBrowserTestRunnerAction implements RequestSourceAware, SkinAware {

    public static final String TRANSFORM = "transform";

    private TestRunManager manager;
    private String url;
    private String remoteAddress;
    private String remoteHost;
    private String browserId;
    private boolean badBrowserId = false;
    private Skin skin;
    private TestPage uploadedTestPage;

    public String execute() throws Exception {
        runner.logStatus(requestReceivedMessage());
        if (uploadedTestPage != null)
            uploadedTestPage.write();
        //noinspection SynchronizeOnNonFinalField
        synchronized (runner) {
            manager = new TestRunManager(runner, url);
            if (!StringUtility.isEmpty(browserId)) {
                try {
                    manager.limitToBrowserWithId(Integer.parseInt(browserId));
                } catch (InvalidBrowserIdException e) {
                    badBrowserId = true;
                    return ERROR;
                } catch (NumberFormatException e) {
                    badBrowserId = true;
                    return ERROR;
                }
            }
            manager.runTests();
        }
        runner.logStatus("Done running tests");
        return skin != null ? TRANSFORM : SUCCESS;
    }

    private String requestReceivedMessage() {
        return new RequestReceivedMessage(remoteHost, remoteAddress, url).generateMessage();
    }

    public XmlRenderable getXmlRenderable() {
        if (badBrowserId) {
            return new ErrorXmlRenderable("Invalid browser ID: " + browserId);
        }
        return manager.getTestRunResult();
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public void setRequestIPAddress(String ipAddress) {
        remoteAddress = ipAddress;
    }

    public void setRequestHost(String host) {
        remoteHost = host;
    }

    public void setBrowserId(String browserId) {
        this.browserId = browserId;
    }

    public void setSkin(Skin skin) {
        this.skin = skin;
    }

    public Skin getSkin() {
        return skin;
    }

    public SkinSource getSkinSource() {
        return runner;
    }

    public void setFragment(String fragment) throws FileNotFoundException, TransformerException, MalformedURLException, UnsupportedEncodingException {
        this.uploadedTestPage = new TestPageGenerator().generateFrom(fragment);
        Configuration configuration = runner.getConfiguration();
        URL runnerURL = new URL("http", SystemUtility.hostname(), configuration.getPort(), "/jsunit/testRunner.html");
        URL testPageURL = new URL("http", SystemUtility.hostname(), configuration.getPort(), "/jsunit/uploaded/" + uploadedTestPage.getFilename());
        this.url = runnerURL.toString() + "?testPage=" + URLEncoder.encode(testPageURL.toString() + "&resultId=" + uploadedTestPage.getId(), "UTF-8");
    }

    public TestPage getUploadedTestPage() {
        return uploadedTestPage;
    }

    public String getUrl() {
        return url;
    }

}
