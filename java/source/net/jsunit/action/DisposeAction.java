package net.jsunit.action;

import org.jdom.Element;

import net.jsunit.XmlRenderable;

public class DisposeAction extends JsUnitAction {

	public String execute() throws Exception {
		runner.dispose();
		return SUCCESS;
	}

	public XmlRenderable getXmlRenderable() {
        return new XmlRenderable() {
            public Element asXml() {
                Element resultElement = new Element("result");
                resultElement.setText("The server has been disposed");
                return resultElement;
            }
        };

	}

}
