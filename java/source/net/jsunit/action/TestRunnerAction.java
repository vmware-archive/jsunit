package net.jsunit.action;

import net.jsunit.TestRunManager;
import net.jsunit.XmlRenderable;
import org.jdom.Element;

public class TestRunnerAction extends JsUnitAction {
    private TestRunManager manager;

    public String execute() throws Exception {
        runner.logStatus("Received request to run tests");
        manager = new TestRunManager(runner);
        manager.runTests();
        runner.logStatus("Done running tests");
        return SUCCESS;
    }

    public XmlRenderable getXmlRenderable() {
        return new XmlRenderable() {
            public Element asXml() {
                Element resultElement = new Element("result");
                String resultString = manager.hadProblems() ? "failure" : "success";
                resultElement.setText(resultString);
                return resultElement;
            }
        };
    }

}
