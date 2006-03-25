package net.jsunit.action;

import com.opensymphony.xwork.Action;
import net.jsunit.JsUnitServer;

public class ServerStatusAction implements Action {
    private JsUnitServer server;

    public String execute() throws Exception {
        return SUCCESS;
    }

    public void setJsUnitServer(JsUnitServer server) {
        this.server = server;
    }

    public String getStatus() {
        return server.getStatus();
    }
}
