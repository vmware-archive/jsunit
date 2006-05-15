package net.jsunit.client;

import junit.extensions.ActiveTestSuite;
import junit.framework.TestResult;
import net.jsunit.PlatformType;
import net.jsunit.model.BrowserSpecification;
import net.jsunit.model.BrowserType;
import net.jsunit.model.DistributedTestRunResult;
import net.jsunit.utility.XmlUtility;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class ClientTestSuite extends ActiveTestSuite {
    private TestRunClient client;
    private File testPage;
    private List<RemoteTestRunTest> tests = new ArrayList<RemoteTestRunTest>();

    public ClientTestSuite(String serviceURL, String username, String password, File testPage) {
        super("JsUnit client suite");
        this.testPage = testPage;
        client = new TestRunClient(serviceURL);
        client.setUsername(username);
        client.setPassword(password);
    }

    public void run(TestResult testResult) {
        sendRequest();
        super.run(testResult);
    }

    private void sendRequest() {
        new Thread() {
            public void run() {
                try {
                    DistributedTestRunResult distributedResult = client.send(testPage);
                    for (RemoteTestRunTest test : tests)
                        test.notifyResult(distributedResult);
                    if (!distributedResult.wasSuccessful())
                        System.err.println(XmlUtility.asPrettyString(distributedResult.asXmlDocument()));
                } catch (Exception e) {
                    for (RemoteTestRunTest test : tests)
                        test.notifyError(e);
                }
            }
        }.start();
    }

    public void addBrowser(PlatformType platformType, BrowserType browserType) {
        BrowserSpecification spec = new BrowserSpecification(platformType, browserType);
        RemoteTestRunTest jUnitTest = new RemoteTestRunTest(spec);
        addTest(jUnitTest);
        client.addBrowserSpec(spec);
    }

    private void addTest(RemoteTestRunTest test) {
        super.addTest(test);
        tests.add(test);
        setName("JsUnit client suite (" + tests.size() + " browsers)");
    }

}
