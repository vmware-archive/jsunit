package net.jsunit.action;

import net.jsunit.XmlRenderable;
import net.jsunit.model.BrowserResult;
import org.jdom.Element;

public class ResultDisplayerAction extends JsUnitBrowserTestRunnerAction {

    private String id;
    private BrowserResult result;
    private Integer browserId;

    public void setId(String id) {
        this.id = id;
    }

    public void setBrowserId(Integer browserId) {
        this.browserId = browserId;
    }

    public String execute() throws Exception {
        if (id != null && browserId != null)
            result = runner.findResultWithId(id, browserId);
        return SUCCESS;
    }

    public XmlRenderable getXmlRenderable() {
        if (result != null)
            return result;
        return new XmlRenderable() {
            public Element asXml() {
                Element errorElement = new Element("error");
                String message;
                if (id != null && browserId != null)
                    message = "No Test Result has been submitted with ID '" + id + "' for browser ID '" + browserId + "'";
                else
                    message = "A Test Result ID and a browser ID must both be given";
                errorElement.setText(message);
                return errorElement;
            }
        };
    }

}