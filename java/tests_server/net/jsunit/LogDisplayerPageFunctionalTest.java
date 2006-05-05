package net.jsunit;

import net.jsunit.model.Browser;
import net.jsunit.model.BrowserResult;
import net.jsunit.utility.XmlUtility;
import org.jdom.Document;

public class LogDisplayerPageFunctionalTest extends StandardServerFunctionalTestCase {

    protected boolean needsRealResultRepository() {
        return true;
    }

    public void setUp() throws Exception {
        super.setUp();
        webTester.beginAt("/logDisplayerPage");
    }

    public void testInitialConditions() throws Exception {
        assertOnLogDisplayerPage();
        webTester.assertRadioOptionSelected("browserId", "0");
    }

    public void testDisplayerForm() throws Exception {
        Browser browser = new Browser(Browser.DEFAULT_SYSTEM_BROWSER, 0);
        standardServer().launchBrowserTestRun(new BrowserLaunchSpecification(browser));
        BrowserResult browserResult = new BrowserResult();
        String id = String.valueOf(System.currentTimeMillis());
        browserResult.setId(id);
        browserResult.setBrowser(browser);
        standardServer().accept(browserResult);
        webTester.setFormElement("id", id);
        webTester.selectOption("skinId", "None (raw XML)");
        webTester.setFormElement("browserId", "0");
        webTester.submit();
        assertEquals(XmlUtility.asString(new Document(browserResult.asXml())), webTester.getDialog().getResponseText());
    }

}
