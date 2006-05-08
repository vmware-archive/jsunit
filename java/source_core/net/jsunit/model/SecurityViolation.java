package net.jsunit.model;

import org.jdom.Element;

public enum SecurityViolation implements Result {
    FAILED_CAPTCHA {
        public String displayString() {
            return "Sorry, you did not enter the correct CAPTCHA text.  Please try again.";
        }
    },
    OUTDATED_CAPTCHA {
        public String displayString() {
            return "Sorry, the CAPTCHA you are using has expired.";
        }

        public Element asXml() {
            Element element = super.asXml();
            element.setAttribute("resetRequired", "true");
            return element;
        }
    };

    public static final String NAME = "securityViolation";

    public Element asXml() {
        return new Element(NAME).setAttribute("type", name()).setText(displayString());
    }

    public int getErrorCount() {
        return 0;
    }

    public int getFailureCount() {
        return 0;
    }

    public int getTestCount() {
        return 0;
    }

    public ResultType getResultType() {
        return ResultType.SECURITY_VIOLATION;
    }

    public boolean wasSuccessful() {
        return false;
    }

    public void addErrorStringTo(StringBuffer buffer) {
        buffer.append(displayString());
    }
}