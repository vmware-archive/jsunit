package net.jsunit;

import net.jsunit.configuration.Configuration;
import net.jsunit.configuration.ServerType;
import net.jsunit.results.Skin;

import java.util.Date;
import java.util.List;

public interface JsUnitServer extends XmlRenderable {
    Configuration getConfiguration();

    ServerType serverType();

    boolean isAggregateServer();

    Date getStartDate();

    long getTestRunCount();

    List<Skin> getSkins();

    List<StatusMessage> getStatusMessages();

}
