package net.jsunit;

import net.jsunit.captcha.AesCipher;
import net.jsunit.model.DistributedTestRunResult;
import net.jsunit.model.DistributedTestRunResultBuilder;
import net.jsunit.model.TestRunResult;
import org.jdom.Document;

import java.net.URL;

public class CaptchaFunctionalTest extends AggregateServerFunctionalTestCase {

    protected FunctionalTestAggregateConfigurationSource createConfigurationSource() {
        return new FunctionalTestAggregateConfigurationSource(port) {
            public String useCaptcha() {
                return String.valueOf(true);
            }
        };
    }

    public void setUp() throws Exception {
        super.setUp();
        webTester.beginAt("/fragmentrunnerpage");
        mockHitter.setDocumentRetrievalStrategy(new DocumentRetrievalStrategy() {
            public Document get(URL url) {
                return new TestRunResult().asXmlDocument();
            }
        });
    }

    public void testHitToRunnerNotAllowed() throws Exception {
        webTester.setFormElement("fragment", "assertTrue(true)");
        webTester.submit();
        webTester.gotoFrame("resultsFrame");
        assertOnAccessDeniedPage();
    }

    public void testEnterCaptcha() throws Exception {
        webTester.assertFormElementPresent("captchaKey");
        String captchaKey = webTester.getDialog().getForm().getParameterValue("captchaKey");
        String answer = new AesCipher("1234567890123456").decrypt(captchaKey).split("_")[1];
        webTester.setFormElement("attemptedCaptchaAnswer", answer);
        webTester.setFormElement("fragment", "assertTrue(true)");
        webTester.selectOption("skinId", "None (raw XML)");
        webTester.submit();
        webTester.gotoFrame("resultsFrame");
        Document document = responseXmlDocument();
        DistributedTestRunResult result = new DistributedTestRunResultBuilder().build(document);
        assertEquals(2, result._getTestRunResults().size());
        assertTrue(result.wasSuccessful());
    }

    private void assertOnAccessDeniedPage() {
        webTester.assertTextPresent("Access denied");
    }
}
