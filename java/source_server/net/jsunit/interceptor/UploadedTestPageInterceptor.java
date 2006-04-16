package net.jsunit.interceptor;

import com.opensymphony.webwork.dispatcher.multipart.MultiPartRequestWrapper;
import com.opensymphony.xwork.Action;
import net.jsunit.upload.TestPage;
import net.jsunit.utility.FileUtility;

import javax.servlet.http.HttpServletRequest;
import java.io.File;

public class UploadedTestPageInterceptor extends AbstractUploadInterceptor {

    protected void execute(Action targetAction) throws Exception {
        HttpServletRequest request = request();
        if (request instanceof MultiPartRequestWrapper) {
            MultiPartRequestWrapper wrapper = (MultiPartRequestWrapper) request;
            File[] files = wrapper.getFiles("testPageFile");
            if (files != null && files.length > 0) {
                File testPage = files[0];
                String contents = FileUtility.read(testPage);
                testPage.delete();
                TestPage page = new TestPage(contents, false);
                page.write();
                setUrlOfTestPageOn(targetAction, page);
            }
        }
    }

}
