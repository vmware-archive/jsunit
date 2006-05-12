package net.jsunit.interceptor;

import com.opensymphony.xwork.Action;
import net.jsunit.action.TestPageURLAware;
import net.jsunit.configuration.Configuration;
import net.jsunit.uploaded.UploadedTestPage;

import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;

public abstract class AbstractUploadInterceptor extends JsUnitInterceptor {

    protected void setUrlOfTestPageOn(Action targetAction, UploadedTestPage uploadedTestPage) throws MalformedURLException, UnsupportedEncodingException {
        TestPageURLAware aware = (TestPageURLAware) targetAction;
        Configuration configuration = aware.getServerConfiguration();

        aware.setUrl(uploadedTestPage.getURL(configuration));
    }

}
