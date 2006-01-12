package net.jsunit.action;

import net.jsunit.XmlRenderable;
import net.jsunit.model.BrowserResult;
import org.jdom.Element;

public class ResultDisplayerAction extends JsUnitAction {

    private String id;
    private BrowserResult result;

    public void setId(String id) {
        this.id = id;
    }

    public String execute() throws Exception {
        result = runner.findResultWithId(id);
        return SUCCESS;
    }

    public XmlRenderable getXmlRenderable() {
        if (result != null)
            return result;
        return new XmlRenderable() {
            public Element asXml() {
                Element errorElement = new Element("error");
                String message = (id != null) ? "No Test Result has been submitted with ID " + id : "No ID given";
                errorElement.setText(message);
                return errorElement;
            }
        };
    }

}