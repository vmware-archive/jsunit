package net.jsunit.action;

import com.opensymphony.xwork.Action;
import net.jsunit.JsUnitServer;
import net.jsunit.XmlRenderable;

public class ConfigurationAction implements Action, JsUnitServerAware, XmlProducer {
    private JsUnitServer server;

    public String execute() throws Exception {
        return SUCCESS;
    }

    public XmlRenderable getXmlRenderable() {
        return server;
    }

    public void setJsUnitServer(JsUnitServer server) {
        this.server = server;
    }

}
