package net.jsunit.model;

import org.jdom.Document;
import org.jdom.Element;

import java.util.ArrayList;
import java.util.List;

public class DistributedTestRunResultBuilder {

    private BrowserSource remoteBrowserSource;

    public DistributedTestRunResultBuilder(BrowserSource remoteBrowserSource) {
        this.remoteBrowserSource = remoteBrowserSource;
    }

    public DistributedTestRunResult build(Document document) {
        DistributedTestRunResult result = new DistributedTestRunResult();
        Element root = document.getRootElement();
        TestRunResultBuilder individualTestRunResultBuilder = new TestRunResultBuilder(remoteBrowserSource);
        //noinspection unchecked
        List<Element> children = (List<Element>) root.getChildren(TestRunResultBuilder.NAME);
        for (Element testRunResultElement : new ArrayList<Element>(children)) {
            testRunResultElement.detach();
            TestRunResult testRunResult = individualTestRunResultBuilder.build(new Document(testRunResultElement));
            result.addTestRunResult(testRunResult);
        }
        return result;
    }
}
