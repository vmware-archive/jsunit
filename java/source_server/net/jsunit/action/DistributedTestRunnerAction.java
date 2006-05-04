package net.jsunit.action;

import com.opensymphony.xwork.Action;
import net.jsunit.*;
import net.jsunit.captcha.SecurityViolation;
import net.jsunit.configuration.Configuration;
import net.jsunit.results.Skin;

import java.net.URL;
import java.util.List;

public class DistributedTestRunnerAction
        implements RequestSourceAware,
        SkinAware,
        TestPageURLAware,
        Action,
        XmlProducer,
        RemoteRunnerHitterAware,
        JsUnitAggregateServerAware,
        CaptchaAware,
        RemoteMachineURLSelectionAware {

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
    private SecurityViolation securityViolation;
    private List<URL> selectedRemoteMachineURLs;
    private String invalidUrlId;

    public void setAggregateServer(JsUnitAggregateServer server) {
        this.server = server;
    }

    public void setRemoteServerHitter(RemoteServerHitter hitter) {
        this.hitter = hitter;
    }

    public String execute() throws Exception {
        if (securityViolation == null) {
            long startTime = System.currentTimeMillis();
            server.logStatus(new RequestReceivedMessage(remoteHost, remoteIpAddress, overrideURL).generateMessage());
            //noinspection SynchronizeOnNonFinalField
            synchronized (server) {
                runTests();
            }
            server.finishedTestRun();
            server.logStatus("Done running aggregate tests ( " + ((System.currentTimeMillis() - startTime) / 1000d) + " seconds)");
        } else
            server.logStatus("Security violation from IP address " + remoteIpAddress);
        return skin != null ? TRANSFORM : SUCCESS;
    }

    private void runTests() {
        manager = DistributedTestRunManager.forMultipleRemoteMachines(
                hitter, getServerConfiguration(), selectedRemoteMachineURLs, overrideURL
        );
        manager.runTests();
    }

    public XmlRenderable getXmlRenderable() {
        if (securityViolation != null)
            return new SimpleXmlRenderable(securityViolation.asXml());
        if (invalidUrlId != null)
            return new ErrorXmlRenderable("Invalid URL ID: " + invalidUrlId);
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
        return getServerConfiguration().useCaptcha();
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
        return getServerConfiguration().getSecretKey();
    }

    public void setSecurityViolation(SecurityViolation violation) {
        this.securityViolation = violation;
    }

    public void setAttemptedCaptchaAnswer(String attemptedCaptchaAnswer) {
        this.attemptedCaptchaAnswer = attemptedCaptchaAnswer;
    }

    public void setRemoteMachineURLs(List<URL> remoteMachineURLs) {
        this.selectedRemoteMachineURLs = remoteMachineURLs;
    }

    public void setSelectedRemoteMachineURLs(List<URL> urls) {
        this.selectedRemoteMachineURLs = urls;
    }

    public void setInvalidRemoteMachineURLId(String invalidId) {
        this.invalidUrlId = invalidId;
    }

    public URL getRemoteMachineURLById(int id) {
        return getServerConfiguration().getRemoteMachineURLById(id);
    }

    public List<URL> getAllRemoteMachineURLs() {
        return getServerConfiguration().getAllRemoteMachineURLs();
    }
}