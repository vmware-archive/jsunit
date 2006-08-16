package net.jsunit.action;

import com.opensymphony.xwork.Action;
import net.jsunit.JsUnitAggregateServer;
import net.jsunit.XmlRenderable;
import org.jdom.Element;

public class AggregateConfigurationAction implements Action, XmlProducer, XmlRenderable, JsUnitAggregateServerAware {
    private JsUnitAggregateServer aggregateServer;

    public String execute() throws Exception {
        return SUCCESS;
    }

    public void setAggregateServer(JsUnitAggregateServer aggregateServer) {
        this.aggregateServer = aggregateServer;
    }

    public JsUnitAggregateServer getAggregateServer() {
        return aggregateServer;
    }

    public Element asXml() {
        return aggregateServer.getConfiguration().asXml();
    }

    public XmlRenderable getXmlRenderable() {
        return this;
    }
}
