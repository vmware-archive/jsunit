package net.jsunit.action;

import com.opensymphony.xwork.Action;
import net.jsunit.JsUnitServer;
import net.jsunit.StatusMessage;

import java.util.List;

public class ServerStatusAction implements Action, JsUnitServerAware {
    private JsUnitServer server;

    public String execute() throws Exception {
        return SUCCESS;
    }

    public void setJsUnitServer(JsUnitServer server) {
        this.server = server;
    }

    public List<StatusMessage> getStatusMessages() {
        return server.getStatusMessages();
    }
}
