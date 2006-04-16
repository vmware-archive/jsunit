package net.jsunit.interceptor;

import com.opensymphony.xwork.Action;
import net.jsunit.BrowserTestRunner;
import net.jsunit.action.TestPageURLAware;
import net.jsunit.configuration.Configuration;
import net.jsunit.upload.TestPage;
import net.jsunit.upload.TestPageGenerator;
import net.jsunit.utility.SystemUtility;

import java.net.URL;
import java.net.URLEncoder;

public class FragmentInterceptor extends JsUnitInterceptor {

    protected void execute(Action targetAction) throws Exception {
        String fragment = request().getParameter("fragment");
        if (fragment != null) {
            TestPageURLAware aware = (TestPageURLAware) targetAction;
            BrowserTestRunner runner = aware.getBrowserTestRunner();

            TestPage uploadedTestPage = new TestPageGenerator().generateFrom(fragment);
            uploadedTestPage.write();

            Configuration configuration = runner.getConfiguration();
            URL runnerURL = new URL("http", SystemUtility.hostname(), configuration.getPort(), "/jsunit/testRunner.html");
            URL testPageURL = new URL("http", SystemUtility.hostname(), configuration.getPort(), "/jsunit/uploaded/" + uploadedTestPage.getFilename());

            String url = runnerURL.toString() + "?testPage=" + URLEncoder.encode(testPageURL.toString() + "&resultId=" + uploadedTestPage.getId(), "UTF-8");
            aware.setUrl(url);
        }
    }

}
