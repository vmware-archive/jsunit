package net.jsunit.action;

import com.opensymphony.xwork.Action;
import net.jsunit.JsUnitServer;

public class StatusAction implements Action {
    private JsUnitServer server;

    public String execute() throws Exception {
        return null;
    }

    public void setJsUnitServer(JsUnitServer server) {
        this.server = server;
    }

    public String getStatus() {
        return server.getStatus();
    }
}
