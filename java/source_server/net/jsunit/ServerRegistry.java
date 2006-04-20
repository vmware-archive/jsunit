package net.jsunit;

public class ServerRegistry {

    private static JsUnitServer server;

    public static void registerServer(JsUnitServer server) {
        ServerRegistry.server = server;
    }

    public static JsUnitServer getServer() {
        return server;
    }

}
