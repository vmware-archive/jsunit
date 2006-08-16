package net.jsunit.model;

import org.jdom.Document;
import org.jdom.Element;

public class ResultBuilder {

    public Result build(Document document) {
        Element rootElement = document.getRootElement();
        String rootElementName = rootElement.getName();
        if (rootElementName.equals(TestRunResult.NAME))
            return new TestRunResultBuilder().build(document);
        else if (rootElementName.equals(DistributedTestRunResult.NAME))
            return new DistributedTestRunResultBuilder().build(document);
        else if (rootElementName.equals(SecurityViolation.NAME)) {
            return SecurityViolation.valueOf(rootElement.getAttribute("type").getValue());
        }
        return null;
    }

}
