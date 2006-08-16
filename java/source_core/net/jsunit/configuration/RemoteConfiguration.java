package net.jsunit.configuration;

import net.jsunit.RemoteConfigurationSource;

import java.net.URL;

public class RemoteConfiguration extends ServerConfiguration {
    private URL remoteURL;
    private RemoteConfigurationSource remoteSource;

    public RemoteConfiguration(URL remoteURL, RemoteConfigurationSource source) {
        super(source);
        this.remoteSource = source;
        this.remoteURL = remoteURL;
    }

    public URL getRemoteURL() {
        return remoteURL;
    }

    public String toString() {
        return remoteURL + "\r\n" + super.toString();
    }

    public ServerType getServerType() {
        return ServerType.valueOf(remoteSource.serverType());
    }

    public boolean isAggregate() {
        return getServerType().isAggregate();
    }
}
