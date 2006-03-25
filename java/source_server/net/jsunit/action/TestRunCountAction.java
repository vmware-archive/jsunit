package net.jsunit.action;

import com.opensymphony.xwork.Action;
import net.jsunit.JsUnitServer;

public class TestRunCountAction implements Action, JsUnitServerAware {

    private JsUnitServer server;

    public void setJsUnitServer(JsUnitServer server) {
        this.server = server;
    }

    public long getTestRunCount() {
        return server.getTestRunCount();
    }

    public String execute() throws Exception {
        return SUCCESS;
    }
}
