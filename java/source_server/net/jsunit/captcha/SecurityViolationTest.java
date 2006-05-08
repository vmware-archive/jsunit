package net.jsunit.captcha;

import junit.framework.TestCase;
import net.jsunit.utility.XmlUtility;
import net.jsunit.model.SecurityViolation;

public class SecurityViolationTest extends TestCase {

    public void testFailedCaptchaXml() throws Exception {
        assertEquals(
            "<securityViolation type=\"FAILED_CAPTCHA\">Sorry, you did not enter the correct CAPTCHA text.  Please try again.</securityViolation>",
            XmlUtility.asString(SecurityViolation.FAILED_CAPTCHA.asXml())
        );
    }

    public void testOutdatedCaptchaXml() throws Exception {
        assertEquals(
            "<securityViolation type=\"OUTDATED_CAPTCHA\" resetRequired=\"true\">Sorry, the CAPTCHA you are using has expired.</securityViolation>",
            XmlUtility.asString(SecurityViolation.OUTDATED_CAPTCHA.asXml())
        );
    }

}
