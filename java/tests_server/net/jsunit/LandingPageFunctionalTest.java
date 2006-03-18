package net.jsunit;

import net.jsunit.model.BrowserResult;
import net.jsunit.utility.SystemUtility;
import net.jsunit.utility.XmlUtility;
import org.jdom.Document;

public class LandingPageFunctionalTest extends FunctionalTestCase {

    public void testSlash() throws Exception {
        webTester.beginAt("/");
        assertOnLandingPage();
        webTester.assertTextPresent(SystemUtility.osString());
        webTester.assertTextPresent(SystemUtility.hostname());
        webTester.assertTextPresent(SystemUtility.ipAddress());
        webTester.assertLinkPresentWithText(new FunctionalTestConfigurationSource(PORT).url());
    }

    public void testIndexDotJsp() throws Exception {
        webTester.beginAt("/index.jsp");
        assertOnLandingPage();
    }

    public void testConfigLink() throws Exception {
        webTester.beginAt("/");
        webTester.clickLinkWithText("config");
        assertConfigXml();
    }

    public void testDisplayerForm() throws Exception {
        server.launchBrowserTestRun(new BrowserLaunchSpecification(BrowserLaunchSpecification.DEFAULT_SYSTEM_BROWSER));
        BrowserResult browserResult = new BrowserResult();
        String id = String.valueOf(System.currentTimeMillis());
        browserResult.setId(id);
        server.accept(browserResult);
        webTester.beginAt("/");
        webTester.setWorkingForm("displayerForm");
        webTester.setFormElement("id", id);
        webTester.setFormElement("browserId", "0");
        webTester.submit();
        assertEquals(XmlUtility.asString(new Document(browserResult.asXml())), webTester.getDialog().getResponseText());
    }

    private void assertOnLandingPage() {
        webTester.assertTitleEquals("JsUnit  Server");
    }

}
