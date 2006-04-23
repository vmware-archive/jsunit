package net.jsunit.model;

import org.jdom.Document;

public class ResultBuilder {

    public Result build(Document document) {
        String rootElementName = document.getRootElement().getName();
        if (rootElementName.equals(TestRunResult.NAME))
            return new TestRunResultBuilder().build(document);
        else if (rootElementName.equals(DistributedTestRunResult.NAME))
            return new DistributedTestRunResultBuilder().build(document);
        return null;
    }

}
