package net.jsunit;

import net.jsunit.configuration.Configuration;
import net.jsunit.configuration.ServerType;
import net.jsunit.results.Skin;
import org.jdom.Element;

import java.util.Date;
import java.util.List;

public class JsUnitServerStub implements JsUnitServer {
    public Configuration getConfiguration() {
        return null;
    }

    public ServerType serverType() {
        return null;
    }

    public boolean isAggregateServer() {
        return false;
    }

    public Date getStartDate() {
        return null;
    }

    public long getTestRunCount() {
        return 0;
    }

    public List<Skin> getSkins() {
        return null;
    }

    public List<StatusMessage> getStatusMessages() {
        return null;
    }

    public void start() throws Exception {
    }

    public void dispose() {
    }

    public void logStatus(String status) {
    }

    public String getSecretKey() {
        return null;
    }

    public Element asXml() {
        return null;
    }
}
