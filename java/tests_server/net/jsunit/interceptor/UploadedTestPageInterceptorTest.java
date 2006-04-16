package net.jsunit.interceptor;

import com.opensymphony.webwork.ServletActionContext;
import com.opensymphony.webwork.dispatcher.multipart.MultiPartRequestWrapper;
import com.opensymphony.xwork.Action;
import junit.framework.TestCase;
import net.jsunit.DummyConfigurationSource;
import net.jsunit.DummyHttpRequest;
import net.jsunit.action.TestPageURLAware;
import net.jsunit.configuration.Configuration;
import net.jsunit.utility.FileUtility;
import net.jsunit.utility.SystemUtility;

import java.io.File;
import java.net.URLEncoder;
import java.util.HashMap;

public class UploadedTestPageInterceptorTest extends TestCase {

    private MockAction action;
    private UploadedTestPageInterceptor interceptor;
    private File uploadedFile;
    private MockActionInvocation invocation;

    protected void setUp() throws Exception {
        super.setUp();
        action = new MockAction();
        interceptor = new UploadedTestPageInterceptor();
        uploadedFile = new File("scratch", "dummy" + System.currentTimeMillis() + ".html");
        FileUtility.write(uploadedFile, "foobar");
        invocation = new MockActionInvocation(action);
    }

    public void testUpload() throws Exception {
        DummyMultiPartRequestWrapper wrapper = new DummyMultiPartRequestWrapper();
        ServletActionContext.setRequest(wrapper);
        interceptor.intercept(invocation);
        assertTrue(invocation.wasInvokeCalled);

        assertEquals("testPageFile", wrapper.fieldName);

        String equalsSignEncoded = URLEncoder.encode("=", "UTF-8");
        int idIndex = action.url.lastIndexOf(equalsSignEncoded);
        long id = Long.parseLong(action.url.substring(idIndex + equalsSignEncoded.length()));
        assertEquals(
                "http://" + SystemUtility.hostname() + ":" + DummyConfigurationSource.PORT + "/jsunit/testRunner.html?testPage=" +
                        URLEncoder.encode("http://" + SystemUtility.hostname() + ":" + DummyConfigurationSource.PORT + "/jsunit/uploaded/uploaded_" + id + ".html&resultId=" + id, "UTF-8"),
                action.url
        );
        File createdTestPage = new File("uploaded", "uploaded_" + id + ".html");
        assertTrue(createdTestPage.exists());
        assertEquals("foobar", FileUtility.read(createdTestPage));
        assertFalse(uploadedFile.exists());
    }

    public void testNotMulitpart() throws Exception {
        ServletActionContext.setRequest(new DummyHttpRequest(new HashMap<String, String[]>()));
        interceptor.intercept(invocation);
        assertTrue(invocation.wasInvokeCalled);
    }

    protected void tearDown() throws Exception {
        if (uploadedFile.exists())
            uploadedFile.delete();
        super.tearDown();
    }

    static class MockAction implements Action, TestPageURLAware {
        private String url;

        public String execute() throws Exception {
            return null;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public Configuration getServerConfiguration() {
            return new Configuration(new DummyConfigurationSource());
        }

    }

    class DummyMultiPartRequestWrapper extends MultiPartRequestWrapper {
        private String fieldName;

        public DummyMultiPartRequestWrapper() {
            super(new DummyHttpRequest(new HashMap<String, String[]>()), "scratch", 1024);
        }

        public File[] getFiles(String fieldName) {
            this.fieldName = fieldName;
            return new File[]{uploadedFile};
        }
    }
}
