package net.jsunit.action;

import junit.framework.TestCase;
import net.jsunit.DummyConfigurationSource;
import net.jsunit.MockBrowserTestRunner;
import net.jsunit.configuration.Configuration;
import net.jsunit.upload.TestPage;
import net.jsunit.utility.SystemUtility;

import java.net.URLEncoder;

public class TestRunnerActionFragmentTest extends TestCase {
    private TestRunnerAction action;

    protected void setUp() throws Exception {
        super.setUp();
        action = new TestRunnerAction();
        MockBrowserTestRunner mockRunner = new MockBrowserTestRunner();
        mockRunner.hasReceivedResult = true;
        mockRunner.configuration = new Configuration(new DummyConfigurationSource());
        action.setBrowserTestRunner(mockRunner);
    }

    public void testFragment() throws Exception {
        String fragment = "assertTrue(true)";
        action.setFragment(fragment);
        TestPage page = action.getUploadedTestPage();
        long id = page.getId();
        assertEquals(
                "http://" + SystemUtility.hostname() + ":" + DummyConfigurationSource.PORT + "/jsunit/testRunner.html?testPage=" +
                        URLEncoder.encode("http://" + SystemUtility.hostname() + ":" + DummyConfigurationSource.PORT + "/jsunit/uploaded/generated_" + id + ".html&resultId=" + id, "UTF-8"),
                action.getUrl()
        );

    }

}
