package net.jsunit.interceptor;

import com.opensymphony.xwork.Action;
import net.jsunit.uploaded.UploadedTestPageFactory;
import net.jsunit.uploaded.UploadedTestPage;

public class FragmentInterceptor extends AbstractUploadInterceptor {

    protected void execute(Action targetAction) throws Exception {
        String fragment = request().getParameter("fragment");
        if (fragment != null) {
            UploadedTestPage uploadedTestPage = new UploadedTestPageFactory().fromFragment(fragment);
            uploadedTestPage.write();

            setUrlOfTestPageOn(targetAction, uploadedTestPage);
        }
    }

}
