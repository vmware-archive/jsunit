package net.jsunit.configuration;

import java.net.URL;

public class RemoteConfiguration extends Configuration {
    private URL remoteURL;

    public RemoteConfiguration(URL remoteURL, ConfigurationSource source) {
        super(source);
        this.remoteURL = remoteURL;
    }

    public URL getRemoteURL() {
        return remoteURL;
    }
}
