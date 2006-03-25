package net.jsunit.action;

import net.jsunit.DistributedTestRunManager;
import net.jsunit.SkinSource;
import net.jsunit.XmlRenderable;
import net.jsunit.results.Skin;

public class DistributedTestRunnerAction extends JsUnitFarmServerAction implements SkinAware {

    private DistributedTestRunManager manager;
    private String overrideURL;
    private Skin skin;

    public String execute() throws Exception {
        String message = "Received request to run farm tests";
        if (overrideURL != null)
            message += " with URL " + overrideURL;
        server.logStatus(message);
        //noinspection SynchronizeOnNonFinalField
        synchronized (server) {
            manager = DistributedTestRunManager.forConfigurationAndURL(hitter, server.getConfiguration(), overrideURL);
            manager.runTests();
        }
        server.logStatus("Done running farm tests");
        return SUCCESS;
    }

    public XmlRenderable getXmlRenderable() {
        return manager.getDistributedTestRunResult();
    }

    public DistributedTestRunManager getTestRunManager() {
        return manager;
    }

    public void setUrl(String overrideURL) {
        this.overrideURL = overrideURL;
    }

    public void setSkin(Skin skin) {
        this.skin = skin;
    }

    public Skin getSkin() {
        return skin;
    }

    public SkinSource getSkinSource() {
        return server;
    }
}
