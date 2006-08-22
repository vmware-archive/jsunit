package net.jsunit.client;

import junit.extensions.ActiveTestSuite;
import junit.framework.TestResult;
import net.jsunit.model.*;
import net.jsunit.utility.XmlUtility;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class ClientTestSuite extends ActiveTestSuite {
    private TestRunServiceClient client;
    private File jsUnitDirectory;
    private File testPage;
    private List<RemoteTestRunTest> tests = new ArrayList<RemoteTestRunTest>();

    public ClientTestSuite(String emailAddress, String password, File jsUnitDirectory, File testPage, ReferencedJsFileResolver resolver) {
        this("http://services.jsunit.net/jsunit/services/TestRunService", emailAddress, password, jsUnitDirectory, testPage, resolver);
    }

    public ClientTestSuite(String serviceURL, String emailAddress, String password, File jsUnitDirectory, File testPage, ReferencedJsFileResolver resolver) {
        super("JsUnit client suite");
        this.jsUnitDirectory = jsUnitDirectory;
        this.testPage = testPage;
        client = new TestRunServiceClient(serviceURL, emailAddress, password, resolver);
    }

    public void run(TestResult testResult) {
        sendRequest();
        super.run(testResult);
    }

    private void sendRequest() {
        new Thread() {
            public void run() {
                try {
                    DistributedTestRunResult distributedResult = client.send(jsUnitDirectory, testPage);
                    System.out.println("Received result:\r\n" + XmlUtility.asPrettyString(distributedResult.asXml()));
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
