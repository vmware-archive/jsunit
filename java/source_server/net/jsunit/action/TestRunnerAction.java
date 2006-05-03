package net.jsunit.action;

import net.jsunit.InvalidBrowserIdException;
import net.jsunit.SkinSource;
import net.jsunit.TestRunManager;
import net.jsunit.XmlRenderable;
import net.jsunit.captcha.SecurityViolation;
import net.jsunit.configuration.Configuration;
import net.jsunit.model.Browser;
import net.jsunit.results.Skin;

import java.util.ArrayList;
import java.util.List;

public class TestRunnerAction
        extends JsUnitBrowserTestRunnerAction
        implements RequestSourceAware, SkinAware, TestPageURLAware, CaptchaAware {

    public static final String TRANSFORM = "transform";

    private TestRunManager manager;
    private String url;
    private String remoteIpAddress;
    private String remoteHost;
    private String[] browserIds;
    private String badBrowserId;
    private Skin skin;
    private String referrer;
    private String captchaKey;
    private String attemptedCaptchaAnswer;
    private SecurityViolation securityViolation;

    public String execute() throws Exception {
        if (securityViolation == null) {
            long startTime = System.currentTimeMillis();
            runner.logStatus(requestReceivedMessage());
            //noinspection SynchronizeOnNonFinalField
            synchronized (runner) {
                manager = new TestRunManager(runner, url);
                try {
                    manager.limitToBrowsers(selectedBrowsers());
                } catch (InvalidBrowserIdException e) {
                    badBrowserId = e.getIdString();
                    return ERROR;
                }
                manager.runTests();
            }
            runner.logStatus("Done running tests (" + ((System.currentTimeMillis() - startTime) / 1000d) + ") seconds)");
        } else
            runner.logStatus("Security violation from IP address " + remoteIpAddress);
        return skin != null ? TRANSFORM : SUCCESS;
    }

    private List<Browser> selectedBrowsers() throws InvalidBrowserIdException {
        List<Browser> result = new ArrayList<Browser>();
        List<Browser> allBrowsers = runner.getBrowsers();
        if (browserIds != null && browserIds.length > 0) {
            for (String idString : browserIds) {
                Browser chosenBrowser = null;
                for (Browser browser : allBrowsers) {
                    try {
                        int id = Integer.parseInt(idString);
                        if (browser.hasId(id))
                            chosenBrowser = browser;
                    } catch (NumberFormatException e) {
                        throw new InvalidBrowserIdException(idString);
                    }
                }
                if (chosenBrowser == null)
                    throw new InvalidBrowserIdException(idString);
                else
                    result.add(chosenBrowser);
            }
        } else {
            result.addAll(allBrowsers);
        }
        return result;
    }

    private String requestReceivedMessage() {
        return new RequestReceivedMessage(remoteHost, remoteIpAddress, url).generateMessage();
    }

    public XmlRenderable getXmlRenderable() {
        if (badBrowserId != null)
            return new ErrorXmlRenderable("Invalid browser ID: " + badBrowserId);
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

    public void setBrowserId(String[] browserIds) {
        this.browserIds = browserIds;
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

}
