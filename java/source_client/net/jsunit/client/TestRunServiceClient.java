package net.jsunit.client;

import net.jsunit.model.BrowserSpecification;
import net.jsunit.model.DistributedTestRunResult;
import net.jsunit.model.TestPage;
import net.jsunit.services.TestRunService;
import net.jsunit.services.TestRunServiceServiceLocator;
import net.jsunit.utility.JsUnitURL;
import org.apache.axis.client.Stub;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class TestRunServiceClient {

    private JsUnitURL serviceURL;
    private List<BrowserSpecification> browserSpecs = new ArrayList<BrowserSpecification>();
    private String username;
    private String password;
    private TestRunServiceServiceLocator locator;

    public TestRunServiceClient(String serviceURL, String username, String password) {
        this(serviceURL, new TestRunServiceServiceLocator(), username, password);
    }

    public TestRunServiceClient(String serviceURL, TestRunServiceServiceLocator locator, String username, String password) {
        this.locator = locator;
        this.serviceURL = new JsUnitURL(serviceURL);
        this.password = password;
        this.username = username;
    }

    public DistributedTestRunResult send(File jsUnitDirectory, File testPageFile) throws Exception {
        TestPage[] testPages = new TestPage(testPageFile).asTestPages(jsUnitDirectory);
        TestRunService service = locator.getTestRunService(serviceURL.asJavaURL());
        ((Stub) service).setUsername(username);
        ((Stub) service).setPassword(password);
        return service.runTests(testPages, browserSpecs.toArray(new BrowserSpecification[browserSpecs.size()]));
    }

    public void addBrowserSpec(BrowserSpecification spec) {
        browserSpecs.add(spec);
    }

}
