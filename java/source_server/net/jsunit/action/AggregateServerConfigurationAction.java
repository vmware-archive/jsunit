package net.jsunit.action;

import net.jsunit.XmlRenderable;

public class AggregateServerConfigurationAction extends JsUnitAggregateServerAction {

    public String execute() throws Exception {
        return SUCCESS;
    }

    public XmlRenderable getXmlRenderable() {
        return server;
    }

}
