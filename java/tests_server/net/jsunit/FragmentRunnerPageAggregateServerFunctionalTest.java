package net.jsunit;

import java.util.List;

public class FragmentRunnerPageAggregateServerFunctionalTest extends AggregateServerFunctionalTestCase {

    public void setUp() throws Exception {
        super.setUp();
        webTester.beginAt("/");
    }

    public void testInitialConditions() throws Exception {
        assertOnFragmentRunnerPage();
    }

    public void testRunFragmentTestAllBrowsers() throws Exception {
        webTester.setFormElement("fragment", "assertTrue(true);");
        webTester.checkCheckbox("urlId_browserId", "0_0");
        webTester.checkCheckbox("urlId_browserId", "0_1");
        webTester.checkCheckbox("urlId_browserId", "1_0");
        webTester.checkCheckbox("urlId_browserId", "1_1");
        webTester.submit();
        String hitToServer1 = urlFor_in(FunctionalTestAggregateConfigurationSource.REMOTE_SERVER_URL_1, last2UrlsPassedToMockHitter());
        String hitToServer2 = urlFor_in(FunctionalTestAggregateConfigurationSource.REMOTE_SERVER_URL_2, last2UrlsPassedToMockHitter());
        assertTrue(hitToServer1, hitToServer1.indexOf("browserId=0") != -1);
        assertTrue(hitToServer1, hitToServer1.indexOf("browserId=1") != -1);
        assertTrue(hitToServer2, hitToServer2.indexOf("browserId=0") != -1);
        assertTrue(hitToServer2, hitToServer2.indexOf("browserId=1") != -1);
    }

    private List<String> last2UrlsPassedToMockHitter() {
        return mockHitter.urlsPassed.subList(mockHitter.urlsPassed.size() - 2, mockHitter.urlsPassed.size());
    }

    public void testRunFragmentTestSpecificBrowsers() throws Exception {
        webTester.setFormElement("fragment", "assertTrue(true);");
        webTester.checkCheckbox("urlId_browserId", "0_1");
        webTester.checkCheckbox("urlId_browserId", "1_0");
        webTester.submit();
        String hitToServer1 = urlFor_in(FunctionalTestAggregateConfigurationSource.REMOTE_SERVER_URL_1, last2UrlsPassedToMockHitter());
        String hitToServer2 = urlFor_in(FunctionalTestAggregateConfigurationSource.REMOTE_SERVER_URL_2, last2UrlsPassedToMockHitter());
        assertEquals(hitToServer1, -1, hitToServer1.indexOf("browserId=0"));
        assertTrue(hitToServer1, hitToServer1.indexOf("browserId=1") != -1);
        assertTrue(hitToServer2, hitToServer2.indexOf("browserId=0") != -1);
        assertEquals(hitToServer2, -1, hitToServer2.indexOf("browserId=1"));
    }

    private String urlFor_in(String baseURL, List<String> urlStrings) {
        for (String urlString : urlStrings)
            if (urlString.indexOf(baseURL) != -1)
                return urlString;
        return null;
    }

}
