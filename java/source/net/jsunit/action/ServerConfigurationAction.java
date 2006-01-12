package net.jsunit.action;

import net.jsunit.XmlRenderable;

public class ServerConfigurationAction extends JsUnitAction {
    public String execute() throws Exception {
        return SUCCESS;
    }

    public XmlRenderable getXmlRenderable() {
        return runner;
    }
}
