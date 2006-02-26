package net.jsunit;

import net.jsunit.configuration.Configuration;
import net.jsunit.configuration.ServerType;

public interface JsUnitServer extends XmlRenderable {
    Configuration getConfiguration();
    ServerType serverType();
    boolean isFarmServer();
}
