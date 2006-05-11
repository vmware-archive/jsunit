package net.jsunit.client;

import net.jsunit.model.Result;
import net.jsunit.model.ResultBuilder;
import net.jsunit.model.ServiceResult;
import net.jsunit.model.TestPage;
import net.jsunit.utility.JsUnitURL;
import net.jsunit.utility.XmlUtility;
import net.jsunit.services.TestRunService;
import net.jsunit.services.TestRunServiceServiceLocator;

import java.io.File;

public class TestRunClient {
    private JsUnitURL serviceURL;
    private String username;
    private String password;

    public TestRunClient(String serviceURL) {
        this.serviceURL = new JsUnitURL(serviceURL);
    }

    public Result send(File testPageFile) throws Exception {
        TestPage testPage = new TestPage(testPageFile);
        TestRunServiceServiceLocator locator = new TestRunServiceServiceLocator();
        TestRunService service = locator.getTestRunService(serviceURL.asJavaURL());
        ServiceResult serviceResult = service.runTests(testPage);
        ResultBuilder builder = new ResultBuilder();
        return builder.build(XmlUtility.asXmlDocument(serviceResult.getXml()));
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

}
