package net.jsunit.action;

import net.jsunit.JsUnitAggregateServer;

public interface JsUnitAggregateServerAware {
    void setAggregateServer(JsUnitAggregateServer aggregateServer);

    JsUnitAggregateServer getAggregateServer();
}
