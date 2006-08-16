package net.jsunit.configuration;

import org.jdom.Element;

import java.net.URL;
import java.util.List;

public interface Configuration {
    Element asXml();

    int getPort();

    ServerType getServerType();

    List<URL> getRemoteMachineURLs();

    boolean shouldIgnoreUnresponsiveRemoteMachines();

    URL getTestURL();
}
