package net.jsunit.action;

import com.opensymphony.xwork.Action;
import net.jsunit.*;
import net.jsunit.configuration.Configuration;
import net.jsunit.results.Skin;

public class DistributedTestRunnerAction
        implements
        RequestSourceAware,
        SkinAware,
        TestPageURLAware,
        Action,
        XmlProducer,
        RemoteRunnerHitterAware,
        JsUnitAggregateServerAware,
        CaptchaAware {

    public static final String TRANSFORM = "transform";

    private DistributedTestRunManager manager;
    private String overrideURL;
    private Skin skin;
    private String remoteIpAddress;
    private String remoteHost;
    private String referrer;
    protected JsUnitAggregateServer server;
    protected RemoteServerHitter hitter;
    private String captchaKey;
    private String attemptedCaptchaAnswer;

    public void setAggregateServer(JsUnitAggregateServer server) {
        this.server = server;
    }

    public void setRemoteServerHitter(RemoteServerHitter hitter) {
        this.hitter = hitter;
    }

    public String execute() throws Exception {
        long startTime = System.currentTimeMillis();
        String message = new RequestReceivedMessage(remoteHost, remoteIpAddress, overrideURL).generateMessage();
        server.logStatus(message);
        //noinspection SynchronizeOnNonFinalField
        synchronized (server) {
            manager = DistributedTestRunManager.forMultipleRemoteMachines(
                    hitter, server.getConfiguration(), server.getConfiguration().getRemoteMachineURLs(), overrideURL
            );
            manager.runTests();
        }
        server.finishedTestRun();
        long millis = System.currentTimeMillis() - startTime;
        server.logStatus("Done running aggregate tests ( " + (millis / 1000d) + " seconds)");
        return skin != null ? TRANSFORM : SUCCESS;
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

    public Configuration getServerConfiguration() {
        return server.getConfiguration();
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

    public void setRequestIPAddress(String ipAddress) {
        this.remoteIpAddress = ipAddress;
    }

    public void setRequestHost(String host) {
        this.remoteHost = host;
    }

    public String getRequestIpAddress() {
        return this.remoteIpAddress;
    }

    public String getReferrer() {
        return this.referrer;
    }

    public void setReferrer(String referrer) {
        this.referrer = referrer;
    }

    public boolean isProtectedByCaptcha() {
        return server.getConfiguration().useCaptcha();
    }

    public String getCaptchaKey() {
        return captchaKey;
    }

    public void setCaptchaKey(String captchaKey) {
        this.captchaKey = captchaKey;
    }

    public String getAttemptedCaptchaAnswer() {
        return attemptedCaptchaAnswer;
    }

    public String getSecretKey() {
        return server.getConfiguration().getSecretKey();
    }

    public void setAttemptedCaptchaAnswer(String attemptedCaptchaAnswer) {
        this.attemptedCaptchaAnswer = attemptedCaptchaAnswer;
    }

}