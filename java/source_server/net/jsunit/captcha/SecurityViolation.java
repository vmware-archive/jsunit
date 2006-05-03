package net.jsunit.captcha;

import net.jsunit.XmlRenderable;
import org.jdom.Element;

public enum SecurityViolation implements XmlRenderable {
    FAILED_CAPTCHA {
        protected String getMessage() {
            return "Sorry, you did not enter the correct CAPTCHA text.  Please try again.";
        }
    },
    OUTDATED_CAPTCHA {
        protected String getMessage() {
            return "Sorry, the CAPTCHA you are using has expired.";
        }

        public Element asXml() {
            Element element = super.asXml();
            element.setAttribute("resetRequired", "true");
            return element;
        }
    };

    public Element asXml() {
        return new Element("securityViolation").setText(getMessage());
    }

    protected abstract String getMessage();
}