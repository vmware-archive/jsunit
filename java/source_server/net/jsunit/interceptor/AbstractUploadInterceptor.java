package net.jsunit.interceptor;

import com.opensymphony.xwork.Action;
import net.jsunit.action.TestPageURLAware;
import net.jsunit.configuration.Configuration;
import net.jsunit.uploaded.UploadedTestPage;
import net.jsunit.utility.SystemUtility;

import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;

public abstract class AbstractUploadInterceptor extends JsUnitInterceptor {

    protected void setUrlOfTestPageOn(Action targetAction, UploadedTestPage uploadedTestPage) throws MalformedURLException, UnsupportedEncodingException {
        TestPageURLAware aware = (TestPageURLAware) targetAction;
        Configuration configuration = aware.getServerConfiguration();

        URL runnerURL = new URL("http", SystemUtility.hostname(), configuration.getPort(), "/jsunit/testRunner.html");
        URL testPageURL = new URL("http", SystemUtility.hostname(), configuration.getPort(), "/jsunit/uploaded/" + uploadedTestPage.getFilename());

        String url = runnerURL.toString() + "?testPage=" + URLEncoder.encode(testPageURL.toString() + "&resultId=" + uploadedTestPage.getId(), "UTF-8");
        aware.setUrl(url);
    }

}
