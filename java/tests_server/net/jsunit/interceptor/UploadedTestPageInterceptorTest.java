package net.jsunit.interceptor;

import com.opensymphony.webwork.ServletActionContext;
import com.opensymphony.webwork.dispatcher.multipart.MultiPartRequestWrapper;
import com.opensymphony.xwork.Action;
import junit.framework.TestCase;
import net.jsunit.DummyHttpRequest;
import net.jsunit.action.TestPageURLAware;
import net.jsunit.configuration.Configuration;
import net.jsunit.configuration.DummyConfigurationSource;
import net.jsunit.utility.FileUtility;
import net.jsunit.utility.SystemUtility;

import java.io.File;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UploadedTestPageInterceptorTest extends TestCase {

    private MockAction action;
    private UploadedTestPageInterceptor interceptor;
    private MockActionInvocation invocation;
    private List<File> createdFiles = new ArrayList<File>();

    protected void setUp() throws Exception {
        super.setUp();
        action = new MockAction();
        interceptor = new UploadedTestPageInterceptor();
        invocation = new MockActionInvocation(action);
    }

    public void testUploadTestPageWithNoReferencedJsFiles() throws Exception {
        File uploadedFile = new File("scratch", "dummy" + System.currentTimeMillis() + ".html");
        createdFiles.add(uploadedFile);
        String content = contentForTestPage("assertTrue(true);");
        FileUtility.write(uploadedFile, content);
        DummyMultiPartRequestWrapper wrapper = new DummyMultiPartRequestWrapper();
        wrapper.fieldNamesToFiles.put("testPageFile", new File[]{uploadedFile});
        ServletActionContext.setRequest(wrapper);
        interceptor.intercept(invocation);
        assertTrue(invocation.wasInvokeCalled);

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
        assertFalse(uploadedFile.exists());
    }

    public void testUploadTestPageWithReferencedJsFiles() throws Exception {
        File uploadedFile = new File("scratch", "dummy" + System.currentTimeMillis() + ".html");
        File referencedJsFile1 = new File("scratch", "someJs" + System.currentTimeMillis() + ".js");
        File referencedJsFile2 = new File("scratch", "someOtherJs" + System.currentTimeMillis() + ".js");
        String content = contentForTestPage(
                "assertTrue(trueFunction());assertFalse(anotherTrueFunction());",
                referencedJsFile1, referencedJsFile2);
        FileUtility.write(uploadedFile, content);
        FileUtility.write(referencedJsFile1, "function trueFunction() {return true;}");
        FileUtility.write(referencedJsFile2, "function anotherTrueFunction() {return true;}");
        createdFiles.add(uploadedFile);
        createdFiles.add(referencedJsFile1);
        createdFiles.add(referencedJsFile2);
        DummyMultiPartRequestWrapper wrapper = new DummyMultiPartRequestWrapper();
        wrapper.fieldNamesToFiles.put("testPageFile", new File[]{uploadedFile});
        wrapper.fieldNamesToFiles.put("referencedJsFiles", new File[]{referencedJsFile1, referencedJsFile2});
        wrapper.fieldNamesToFileNames.put("referencedJsFiles", new String[]{referencedJsFile1.getName(), referencedJsFile2.getName()});

        ServletActionContext.setRequest(wrapper);
        interceptor.intercept(invocation);
        assertTrue(invocation.wasInvokeCalled);

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
        assertFalse(uploadedFile.exists());
        assertFalse(referencedJsFile1.exists());
        assertFalse(referencedJsFile2.exists());
    }

    private String contentForTestPage(String testFunctionSource, File... referencedJsFiles) {
        StringBuffer buffer = new StringBuffer();
        buffer.append("<html>");
        buffer.append("<head>");
        buffer.append("<script language=\"javascript\" src=\"../my/dir/jsunit/app/jsUnitCore.js\"></script>");
        for (File referencedJsFile : referencedJsFiles)
            buffer.append("<script language=\"javascript\" src=\"").append(referencedJsFile.getAbsolutePath()).append("\"></script>");
        buffer.append("<script language=\"javascript\">function testSimple() {").append(testFunctionSource).append("}</script>");
        buffer.append("</head>");
        buffer.append("</html>");
        return buffer.toString();
    }

    public void testNotMulitpart() throws Exception {
        ServletActionContext.setRequest(new DummyHttpRequest(new HashMap<String, String[]>()));
        interceptor.intercept(invocation);
        assertTrue(invocation.wasInvokeCalled);
    }

    protected void tearDown() throws Exception {
        for (File file : createdFiles) {
            if (file.exists())
                file.delete();
        }
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
        private Map<String, File[]> fieldNamesToFiles = new HashMap<String, File[]>();
        private Map<String, String[]> fieldNamesToFileNames = new HashMap<String, String[]>();

        public DummyMultiPartRequestWrapper() {
            super(new DummyHttpRequest(new HashMap<String, String[]>()), "scratch", 1024);
        }

        public File[] getFiles(String fieldName) {
            return fieldNamesToFiles.get(fieldName);
        }

        public String[] getFileNames(String fieldName) {
            return fieldNamesToFileNames.get(fieldName);
        }
    }
}
