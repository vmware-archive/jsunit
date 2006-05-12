package net.jsunit.action;

import com.opensymphony.xwork.Action;
import net.jsunit.*;
import net.jsunit.configuration.Configuration;
import net.jsunit.configuration.RemoteConfiguration;
import net.jsunit.model.SecurityViolation;
import net.jsunit.results.Skin;

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
        RemoteRunSpecificationAware {

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
    private List<RemoteRunSpecification> remoteRunSpecs;
    private InvalidRemoteMachineUrlBrowserCombination invalidRemoteUrlBrowserCombo;

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
            server.logStatus(new SecurityViolationMessage(remoteIpAddress, securityViolation, captchaKey, attemptedCaptchaAnswer).generateMessage());
        return skin != null ? TRANSFORM : SUCCESS;
    }

    private void runTests() {
        manager = new DistributedTestRunManager(hitter, getServerConfiguration(), overrideURL, remoteRunSpecs);
        manager.runTests();
    }

    public XmlRenderable getXmlRenderable() {
        if (securityViolation != null)
            return new SimpleXmlRenderable(securityViolation.asXml());
        if (invalidRemoteUrlBrowserCombo != null)
            return new ErrorXmlRenderable("Invalid Remote Machine ID/Browser ID: " + invalidRemoteUrlBrowserCombo.getDisplayString());
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

    public void setRemoteRunSpecifications(List<RemoteRunSpecification> remoteRunSpecs) {
        this.remoteRunSpecs = remoteRunSpecs;
    }

    public RemoteConfiguration getRemoteMachineConfigurationById(int id) {
        return server.getRemoteMachineConfigurationById(id);
    }

    public List<RemoteConfiguration> getAllRemoteMachineConfigurations() {
        return server.getAllRemoteMachineConfigurations();
    }

    public void setInvalidRemoteMachineUrlBrowserCombination(InvalidRemoteMachineUrlBrowserCombination combination) {
        this.invalidRemoteUrlBrowserCombo = combination;
    }

}
