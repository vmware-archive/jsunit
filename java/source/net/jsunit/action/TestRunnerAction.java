package net.jsunit.action;

import junit.framework.TestResult;
import junit.textui.TestRunner;
import net.jsunit.StandaloneTest;
import net.jsunit.Utility;
import net.jsunit.XmlRenderable;
import org.jdom.Element;

public class TestRunnerAction extends JsUnitAction implements StandaloneTestAware {

    private TestResult result;
    private StandaloneTest test;

    public String execute() {
        Utility.log("Received request to run standalone test");
        result = TestRunner.run(test);//TODO: change this to instead just use a testrunmanager, not a standalone test
        Utility.log("Done running standalone test");
        return SUCCESS;
    }

    public void setStandaloneTest(StandaloneTest test) {
        this.test = test;
        this.test.setBrowserTestRunner(runner);
    }

    public XmlRenderable getXmlRenderable() {
        return new XmlRenderable() {
            public Element asXml() {
                Element resultElement = new Element("result");
                String resultString = result.wasSuccessful() ? "success" : "failure";
                resultElement.setText(resultString);
                return resultElement;
            }
        };
    }

}
