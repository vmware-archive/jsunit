package net.jsunit.action;

import net.jsunit.XmlRenderable;

public class FarmServerConfigurationAction extends JsUnitFarmServerAction {

    public String execute() throws Exception {
        return SUCCESS;
    }

    public XmlRenderable getXmlRenderable() {
        return server;
    }

}
