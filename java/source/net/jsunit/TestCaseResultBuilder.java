package net.jsunit;

import org.jdom.Element;

/**
 * @author Edward Hieatt, edward@jsunit.net
 */

public class TestCaseResultBuilder {
    public TestCaseResult build(Element element) {
        TestCaseResult result = new TestCaseResult();
        updateWithHeaders(result, element);
        updateWithMessage(result, element);
        return result;
    }

    private void updateWithHeaders(TestCaseResult result, Element element) {
        result.setName(element.getAttributeValue(TestCaseResultWriter.NAME));
        result.setTimeTaken(Double.parseDouble(element.getAttributeValue(TestCaseResultWriter.TIME)));
    }

    private void updateWithMessage(TestCaseResult result, Element element) {
        for (Object o : element.getChildren()) {
            Element next = (Element) o;
            String type = next.getName();
            String message = next.getAttributeValue(TestCaseResultWriter.MESSAGE);
            if (TestCaseResultWriter.FAILURE.equals(type))
                result.setFailure(message);
            else if (TestCaseResultWriter.ERROR.equals(type))
                result.setError(message);
        }
    }
}
