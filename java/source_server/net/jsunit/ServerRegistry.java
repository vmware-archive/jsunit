package net.jsunit;

public class ServerRegistry {

    private static JsUnitStandardServer standardServer;
    private static JsUnitAggregateServer aggregateServer;

    public static void registerStandardServer(JsUnitStandardServer server) {
        ServerRegistry.standardServer = server;
    }

    public static void registerAggregateServer(JsUnitAggregateServer aggregateServer) {
        ServerRegistry.aggregateServer = aggregateServer;
    }

    public static JsUnitStandardServer getStandardServer() {
        return standardServer;
    }

    public static JsUnitAggregateServer getAggregateServer() {
        return aggregateServer;
    }

}
