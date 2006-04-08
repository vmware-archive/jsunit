package net.jsunit.action;

import com.opensymphony.xwork.Action;
import net.jsunit.JsUnitAggregateServer;
import net.jsunit.RemoteServerHitter;

public abstract class JsUnitAggregateServerAction
        implements Action,
        XmlProducer,
        RemoteRunnerHitterAware,
        JsUnitAggregateServerAware {

    protected JsUnitAggregateServer server;
    protected RemoteServerHitter hitter;

    public void setAggregateServer(JsUnitAggregateServer server) {
        this.server = server;
    }

    public void setRemoteServerHitter(RemoteServerHitter hitter) {
        this.hitter = hitter;
    }

}
