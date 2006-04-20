package net.jsunit;

import net.jsunit.model.Browser;
import net.jsunit.model.BrowserResult;
import net.jsunit.utility.XmlUtility;
import org.jdom.Document;

public class LogDisplayerPageFunctionalTest extends FunctionalTestCase {

    protected boolean needsRealResultRepository() {
        return true;
    }

    public void setUp() throws Exception {
        super.setUp();
        webTester.beginAt("/logDisplayerPage");
    }

    public void testInitialConditions() throws Exception {
        assertOnLogDisplayerPage();
    }

    public void testDisplayerForm() throws Exception {
        server.launchBrowserTestRun(new BrowserLaunchSpecification(new Browser(Browser.DEFAULT_SYSTEM_BROWSER, 0)));
        BrowserResult browserResult = new BrowserResult();
        String id = String.valueOf(System.currentTimeMillis());
        browserResult.setId(id);
        server.accept(browserResult);
        webTester.setFormElement("id", id);
        webTester.selectOption("skinId", "None (raw XML)");
        webTester.selectOption("browserId", Browser.DEFAULT_SYSTEM_BROWSER);
        webTester.submit();
        assertEquals(XmlUtility.asString(new Document(browserResult.asXml())), webTester.getDialog().getResponseText());
    }

}
