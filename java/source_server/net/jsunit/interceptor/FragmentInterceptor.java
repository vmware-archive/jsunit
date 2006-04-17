package net.jsunit.interceptor;

import com.opensymphony.xwork.Action;
import net.jsunit.upload.TestPage;
import net.jsunit.upload.TestPageFactory;

public class FragmentInterceptor extends AbstractUploadInterceptor {

    protected void execute(Action targetAction) throws Exception {
        String fragment = request().getParameter("fragment");
        if (fragment != null) {
            TestPage uploadedTestPage = new TestPageFactory().fromFragment(fragment);
            uploadedTestPage.write();

            setUrlOfTestPageOn(targetAction, uploadedTestPage);
        }
    }

}
