package net.jsunit.action;

import net.jsunit.SkinSource;
import net.jsunit.TestRunManager;
import net.jsunit.XmlRenderable;
import net.jsunit.model.SecurityViolation;
import net.jsunit.configuration.Configuration;
import net.jsunit.model.Browser;
import net.jsunit.results.Skin;

import java.util.List;

public class TestRunnerAction
        extends JsUnitBrowserTestRunnerAction
        implements RequestSourceAware, SkinAware, TestPageURLAware, CaptchaAware, BrowserSelectionAware {

    public static final String TRANSFORM = "transform";

    private TestRunManager manager;
    private String url;
    private String remoteIpAddress;
    private String remoteHost;
    private String invalidBrowserId;
    private List<Browser> selectedBrowsers;
    private Skin skin;
    private String referrer;
    private String captchaKey;
    private String attemptedCaptchaAnswer;
    private SecurityViolation securityViolation;

    public String execute() throws Exception {
        if (hasSecurityViolation())
            runner.logStatus("Security violation from IP address " + remoteIpAddress);
        else {
            long startTime = System.currentTimeMillis();
            runner.logStatus(requestReceivedMessage());
            //noinspection SynchronizeOnNonFinalField
            synchronized (runner) {
                manager = new TestRunManager(runner, url);
                if (selectedBrowsers != null)
                    manager.limitToBrowsers(selectedBrowsers);
                manager.runTests();
            }
            runner.logStatus("Done running tests (" + ((System.currentTimeMillis() - startTime) / 1000d) + ") seconds)");
        }
        return skin != null ? TRANSFORM : SUCCESS;
    }

    private boolean hasSecurityViolation() {
        return securityViolation != null;
    }

    private String requestReceivedMessage() {
        return new RequestReceivedMessage(remoteHost, remoteIpAddress, url).generateMessage();
    }

    public XmlRenderable getXmlRenderable() {
        if (invalidBrowserId != null)
            return new ErrorXmlRenderable("Invalid browser ID: " + invalidBrowserId);
        if (securityViolation != null)
            return new SimpleXmlRenderable(securityViolation.asXml());
        return manager.getTestRunResult();
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Configuration getServerConfiguration() {
        return runner.getConfiguration();
    }

    public void setRequestIPAddress(String ipAddress) {
        remoteIpAddress = ipAddress;
    }

    public void setRequestHost(String host) {
        remoteHost = host;
    }

    public void setInvalidBrowserId(String invalidId) {
        this.invalidBrowserId = invalidId;
    }

    public void setSelectedBrowsers(List<Browser> browsers) {
        this.selectedBrowsers = browsers;
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

    public String getRequestIpAddress() {
        return this.remoteIpAddress;
    }

    public String getReferrer() {
        return referrer;
    }

    public void setReferrer(String referrer) {
        this.referrer = referrer;
    }

    public boolean isProtectedByCaptcha() {
        return runner.getConfiguration().useCaptcha();
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
        return runner.getConfiguration().getSecretKey();
    }

    public void setSecurityViolation(SecurityViolation violation) {
        this.securityViolation = violation;
    }

    public void setAttemptedCaptchaAnswer(String attemptedCaptchaAnswer) {
        this.attemptedCaptchaAnswer = attemptedCaptchaAnswer;
    }

    public Browser getBrowserById(int id) {
        return getServerConfiguration().getBrowserById(id);
    }

    public List<Browser> getAllBrowsers() {
        return getServerConfiguration().getAllBrowsers();
    }
}
