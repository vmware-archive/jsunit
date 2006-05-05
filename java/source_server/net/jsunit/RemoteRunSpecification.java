package net.jsunit;

import net.jsunit.captcha.CaptchaSpec;
import net.jsunit.configuration.Configuration;
import net.jsunit.model.Browser;

import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.List;
import java.util.ArrayList;
import java.util.Iterator;

public class RemoteRunSpecification {
    private URL remoteBaseURL;
    private List<Browser> remoteBrowsers = new ArrayList<Browser>();

    public RemoteRunSpecification(URL remoteBaseURL) {
        this.remoteBaseURL = remoteBaseURL;
    }

    public URL getRemoteMachineBaseURL() {
        return remoteBaseURL;
    }

    public URL buildFullURL(Configuration localConfiguration, String overrideURL) throws UnsupportedEncodingException, MalformedURLException {
        StringBuffer buffer = new StringBuffer(remoteBaseURL.toString());
        buffer.append("/runner");
        boolean hasFirstParameter = false;
        if (overrideURL != null) {
            buffer.append("?url=").append(URLEncoder.encode(overrideURL, "UTF-8"));
            hasFirstParameter = true;
        } else if (localConfiguration.getTestURL() != null) {
            buffer.append("?url=").append(URLEncoder.encode(localConfiguration.getTestURL().toString(), "UTF-8"));
            hasFirstParameter = true;
        }
        if (localConfiguration.useCaptcha()) {
            CaptchaSpec spec = CaptchaSpec.create(localConfiguration.getSecretKey());
            if (hasFirstParameter)
                buffer.append("&");
            else
                buffer.append("?");
            buffer.append("captchaKey=").append(URLEncoder.encode(spec.getEncryptedKey(), "UTF-8"));
            buffer.append("&attemptedCaptchaAnswer=").append(URLEncoder.encode(spec.getAnswer(), "UTF-8"));
            hasFirstParameter = true;
        }
        appendBrowserParametersToURL(buffer, hasFirstParameter);
        return new URL(buffer.toString());
    }

    private void appendBrowserParametersToURL(StringBuffer buffer, boolean hasFirstParameter) {
        for (Browser browser : remoteBrowsers) {
            buffer.append(hasFirstParameter ? "&" : "?");
            buffer.append("browserId=").append(browser.getId());
            hasFirstParameter = true;
        }
    }

    public void addBrowser(Browser browser) {
        remoteBrowsers.add(browser);
    }

    public List<Browser> getRemoteBrowsers() {
        return remoteBrowsers;
    }

    public boolean isForAllBrowsers() {
        return remoteBrowsers.isEmpty();
    }

    public String getDisplayString() {
        StringBuffer buffer = new StringBuffer();
        buffer.append(remoteBaseURL).append(": ");
        if (isForAllBrowsers())
            buffer.append("all browsers");
        else
            for (Iterator<Browser> it = remoteBrowsers.iterator(); it.hasNext();) {
                Browser browser = it.next();
                buffer.append(browser.getDisplayName());
                if (it.hasNext())
                    buffer.append(", ");
            }
        return buffer.toString();
    }
}
