package net.jsunit;

public class ServerRegistry {
    private static JsUnitStandardServer standardServer;
    private static JsUnitAggregateServer aggregateServer;

    public static void registerServer(JsUnitStandardServer server) {
        standardServer = server;
    }

    public static void registerAggregateServer(JsUnitAggregateServer server) {
        aggregateServer = server;
    }

    public static JsUnitStandardServer getStandardServer() {
        return standardServer;
    }

    public static JsUnitAggregateServer getAggregateServer() {
        return aggregateServer;
    }

    public static JsUnitServer getServer() {
        return standardServer != null ? standardServer : aggregateServer;
    }
}
