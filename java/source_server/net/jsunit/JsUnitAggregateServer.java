package net.jsunit;

import net.jsunit.configuration.Configuration;
import net.jsunit.configuration.ServerType;

import java.util.Arrays;
import java.util.List;

public class JsUnitAggregateServer extends AbstractJsUnitServer {

    public JsUnitAggregateServer(Configuration configuration) {
        super(configuration, ServerType.AGGREGATE);
        ServerRegistry.registerAggregateServer(this);
    }

    protected List<String> servletNames() {
        return Arrays.asList(new String[]{
                "index",
                "config",
                "latestversion",
                "runner",
                "serverstatus",
                "testruncount"
        });
    }

    public static void main(String args[]) {
        try {
            JsUnitAggregateServer server = new JsUnitAggregateServer(Configuration.resolve(args));
            server.start();
        } catch (Throwable t) {
            t.printStackTrace();
        }
    }

    public String toString() {
        return "JsUnit Aggregate Server";
    }

    protected String xworkXmlName() {
        return "aggregate_xwork.xml";
    }

    public ServerType serverType() {
        return ServerType.AGGREGATE;
    }

    public void finishedTestRun() {
        testRunCount++;
    }
}
