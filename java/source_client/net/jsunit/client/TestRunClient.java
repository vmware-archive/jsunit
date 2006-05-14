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

public class TestRunClient {
    private JsUnitURL serviceURL;
    private List<BrowserSpecification> browserSpecs = new ArrayList<BrowserSpecification>();
    private String username;
    private String password;

    public TestRunClient(String serviceURL) {
        this.serviceURL = new JsUnitURL(serviceURL);
    }

    public DistributedTestRunResult send(File testPageFile) throws Exception {
        TestPage testPage = new TestPage(testPageFile);
        TestRunServiceServiceLocator locator = new TestRunServiceServiceLocator();
        TestRunService service = locator.getTestRunService(serviceURL.asJavaURL());
        ((Stub) service).setUsername(username);
        ((Stub) service).setPassword(password);
        return service.runTests(testPage, browserSpecs.toArray(new BrowserSpecification[browserSpecs.size()]));
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void addBrowserSpec(BrowserSpecification spec) {
        browserSpecs.add(spec);
    }

}
