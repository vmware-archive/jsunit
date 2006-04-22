package net.jsunit.action;

import net.jsunit.InvalidBrowserIdException;
import net.jsunit.SkinSource;
import net.jsunit.TestRunManager;
import net.jsunit.XmlRenderable;
import net.jsunit.configuration.Configuration;
import net.jsunit.results.Skin;
import net.jsunit.utility.StringUtility;

public class TestRunnerAction extends JsUnitBrowserTestRunnerAction implements RequestSourceAware, SkinAware, TestPageURLAware {

    public static final String TRANSFORM = "transform";

    private TestRunManager manager;
    private String url;
    private String remoteAddress;
    private String remoteHost;
    private String browserId;
    private boolean badBrowserId = false;
    private Skin skin;

    public String execute() throws Exception {
        long startTime = System.currentTimeMillis();
        runner.logStatus(requestReceivedMessage());
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
        long millis = System.currentTimeMillis() - startTime;
        runner.logStatus("Done running tests (" + (millis / 1000d) + ") seconds)");
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

    public Configuration getServerConfiguration() {
        return runner.getConfiguration();
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

    public String getUrl() {
        return url;
    }

}
