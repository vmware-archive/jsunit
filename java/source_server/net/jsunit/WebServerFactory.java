package net.jsunit;

import net.jsunit.configuration.ServerConfiguration;

public interface WebServerFactory {
    public WebServer create(ServerConfiguration configuration);
}
