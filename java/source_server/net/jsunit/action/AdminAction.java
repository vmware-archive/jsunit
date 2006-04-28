package net.jsunit.action;

import com.opensymphony.xwork.Action;
import net.jsunit.JsUnitServer;

public class AdminAction implements Action, JsUnitServerAware {
    private JsUnitServer server;

    public void setJsUnitServer(JsUnitServer server) {
        this.server = server;
    }

    public JsUnitServer getServer() {
        return server;
    }

    public String execute() throws Exception {
        return SUCCESS;
    }

}
